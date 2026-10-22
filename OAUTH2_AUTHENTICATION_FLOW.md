# OAuth2 / OpenID Connect Authentication in This Project

This guide describes the authentication implementation in the current project: how the Angular UI starts Azure sign-in, how Spring Security completes the OpenID Connect (OIDC) flow, how the UI reads the authenticated user, and what the Logout button does.

## At a glance

- **Identity provider:** Microsoft Entra ID (formerly Azure AD), configured through an OIDC issuer URI.
- **Backend:** Spring Boot 4.1.1 and Spring Security OAuth2 Client.
- **Frontend:** Angular 18, served from Spring Boot's `src/main/resources/static` directory after a production build.
- **Application session:** Spring Security stores the authenticated principal in the server-side HTTP session. The browser carries the session cookie, normally `JSESSIONID`.
- **Logout behavior:** Logout clears this application's session and returns to `/`. It intentionally does not visit Microsoft's sign-out endpoint.

OAuth2 is the authorization framework used by the protocol flow. The app uses **OpenID Connect**, an identity layer on top of OAuth2, to authenticate the user and receive an ID token and user claims.

## Login flow

```mermaid
sequenceDiagram
    actor User
    participant UI as Angular UI
    participant App as Spring Boot / Spring Security
    participant Entra as Microsoft Entra ID

    User->>UI: Click Login with Azure
    UI->>App: Navigate to /oauth2/authorization/azure-dev
    App->>App: Build authorization request; add prompt=select_account
    App->>Entra: Redirect browser to Entra authorization endpoint
    Entra->>User: Show account chooser / authenticate as needed
    Entra->>App: Redirect to /login/oauth2/code/azure-dev with authorization code and state
    App->>Entra: Exchange authorization code for tokens
    Entra-->>App: Return tokens and OIDC user claims
    App->>App: Validate response and save authenticated principal in HTTP session
    App-->>UI: Redirect to /
    UI->>App: GET /api/auth/user
    App-->>UI: authenticated=true plus selected profile fields
    UI-->>User: Show authenticated status, profile, and Logout button
```

### 1. Angular starts a full browser navigation

`AuthService.login()` sets `window.location.href` to `/oauth2/authorization/azure-dev`. This is a browser navigation, rather than an Angular `HttpClient` request, because the next step must redirect the browser to the identity provider.

### 2. Spring Security creates the authorization request

`SecurityConfig.authorizationRequestResolver()` wraps Spring Security's `DefaultOAuth2AuthorizationRequestResolver`. It adds this OIDC authorization parameter to requests:

```text
prompt=select_account
```

This asks Microsoft to show the account chooser. It does not require the user to re-enter a password. A device or organization sign-on session may still make authentication quick after an account is selected. Microsoft documents `select_account` and `login` as different prompt modes; `login` requests fresh credential entry. See [Microsoft's OIDC sign-in request parameters](https://learn.microsoft.com/en-us/entra/identity-platform/v2-protocols-oidc#send-the-sign-in-request).

Spring Security also creates and saves the OAuth2 authorization request, including correlation data such as `state`, so it can validate the callback. The registration id `azure-dev` selects the configured client registration.

### 3. Microsoft authenticates the user and returns a code

The browser visits Microsoft Entra ID. After account selection and any required authentication or consent, Entra redirects the browser to Spring Security's callback:

```text
/login/oauth2/code/azure-dev
```

The `openid` scope means this is an OIDC login. Spring Security handles the authorization-code exchange and token validation. The client secret is used by the backend during the token exchange; it is not supposed to be sent to Angular.

### 4. Spring creates the application session

On a successful callback, Spring Security stores the authenticated principal and security context in the HTTP session. It redirects to `/` because `defaultSuccessUrl("/", true)` is configured. The Angular app then starts and calls `/api/auth/user` to get current authentication status.

## How the UI determines the logged-in state

`HomeComponent.ngOnInit()` calls `checkAuth()`, which subscribes to `AuthService.getUserInfo()`:

```text
GET /api/auth/user
```

`AuthController.getUserInfo()` reads the current principal with `@AuthenticationPrincipal OAuth2User` and returns:

- `authenticated: true`, with `name`, `email`, and the user's attributes when a principal exists;
- `authenticated: false` when there is no authenticated OAuth2 user.

The component uses `userInfo.authenticated` to choose between the authenticated profile and Logout button or the unauthenticated status and Login with Azure button. While the request is in progress, it displays `Loading...`. If the request fails, it treats the browser as unauthenticated.

The API response is the UI's source of truth. Angular does not store an access token or decide authentication based on a local flag. Browser cookies are sent automatically for the same-origin API call, allowing Spring to find the session.

## Logout behavior

The Logout button submits a native HTML form:

```text
POST /logout
```

This is handled by Spring Security's logout filter, not an `AuthController` method. On logout, Spring Security clears the authentication, invalidates the HTTP session, clears the `JSESSIONID` cookie, and redirects to `/`. Angular then reloads and calls `/api/auth/user`; the API reports `authenticated: false`, so the Login button appears.

### Local logout versus Microsoft sign-out

This project currently performs a **local application logout**. It ends this app's Spring session but leaves the user's Microsoft Entra browser/device session alone. This is intentional so Logout returns directly to the app's Login button instead of opening Microsoft's sign-out/account-selection pages.

When the user clicks Login again, `prompt=select_account` requests Microsoft's account chooser. The chooser may still complete quickly if the selected account has an active organization or device sign-on session. If the desired behavior changes to require password entry, use `prompt=login` instead. Microsoft explains the effect of device sign-on state and the `login` / `select_account` prompts in its [OIDC protocol documentation](https://learn.microsoft.com/en-us/entra/identity-platform/v2-protocols-oidc).

Spring Security also supports RP-initiated OIDC logout, which redirects the browser to the identity provider's end-session endpoint. That is a different behavior from the local `/logout` flow currently used here. See [Spring Security's OIDC logout documentation](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/logout.html).

## Failure handling

Spring Security is configured with:

```text
failureUrl = /api/auth/login-failure
```

That URL matches `AuthController.loginFailure()` and returns a small JSON response with `authenticated: false`. This handler is reached when Spring processes an OAuth2 login failure callback. An error displayed on a Microsoft-hosted page before the callback is a separate provider-side error; the relevant AADSTS error code and the backend log around the callback are needed to diagnose it.

`/api/auth/login-success` is also defined in `AuthController`, but the configured successful-login flow uses `defaultSuccessUrl("/", true)` and then the UI reads `/api/auth/user`.

## Configuration

OAuth2 client settings are in `src/main/resources/application.yaml` under `spring.security.oauth2.client`:

- Registration id: `azure-dev`
- Provider id: `azure`
- Scopes: `openid`, `profile`, and `email`
- Provider metadata: discovered from the configured issuer URI
- Client id and client secret: identify the registered web application and authenticate the backend during code exchange

For local development, register this callback URI in the Entra app registration:

```text
http://localhost:8080/login/oauth2/code/azure-dev
```

For another hostname, port, or HTTPS setup, register the callback URI that matches that environment. The project uses the root path as the post-login destination.

### Credential handling

The current YAML contains a client secret directly in the project configuration. Do not copy the secret into documentation, frontend code, logs, screenshots, or source control. For shared or deployed environments, store it in an environment variable or a secret manager and reference that value from Spring configuration. If a real secret has been committed or shared, rotate it in the Entra app registration.

## Important implementation files

| File | Responsibility |
|---|---|
| `src/main/java/com/example/oauth2_demo/config/SecurityConfig.java` | Public/protected request rules, OAuth2 login, account chooser parameter, callback success/failure destinations, and local logout |
| `src/main/java/com/example/oauth2_demo/controller/AuthController.java` | Current-user, login status, and health API responses |
| `src/main/resources/application.yaml` | OAuth2 client registration, issuer discovery, and server port |
| `frontend/src/app/auth.service.ts` | Browser navigation to login, user-info API call, and native POST logout form |
| `frontend/src/app/home/home.component.ts` | Loading/authenticated/unauthenticated UI state and button visibility |
| `frontend/angular.json` | Frontend build output to `src/main/resources/static` |
| `pom.xml` | Spring Boot and Spring Security dependencies |

## Running and rebuilding

From the frontend directory, rebuild the Angular app so Spring Boot serves the latest assets:

```bash
npm run build
```

From the project root, start the backend:

```bash
./mvnw spring-boot:run
```

The local app is served at `http://localhost:8080`. After changing the Spring Security configuration, restart the backend. After changing Angular code, rebuild the frontend assets.

## Security notes and next steps

- The project currently disables CSRF protection in `SecurityConfig`. This lets the native POST logout form work without a CSRF token, but a deployed browser application should normally keep CSRF protection enabled and include a valid token on state-changing requests.
- Keep the OAuth client secret backend-only and move it out of committed configuration.
- `/api/auth/user` and other `/api/**` routes are permitted through the URL authorization rules so the UI can query authentication state. The controller returns profile information only when the request has an authenticated principal; add method-level authorization if protected API operations are introduced.
- The endpoint returns the full provider attribute map for demo purposes. Production applications should return only profile fields the UI actually needs.
- Logout is app-local by design. If future requirements call for ending the Microsoft session too, implement and test provider-initiated logout separately, including the Azure app-registration URLs and expected account-selection behavior.

## References

- [Spring Security: OAuth2 Login](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/)
- [Spring Security: OIDC Logout](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/logout.html)
- [Microsoft identity platform: OpenID Connect](https://learn.microsoft.com/en-us/entra/identity-platform/v2-protocols-oidc)
