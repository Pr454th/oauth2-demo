# OAuth2 Demo with Angular Frontend

Spring Boot application with OAuth2 authentication (Azure AD) and Angular frontend.

## Project Structure

- **Backend**: Spring Boot with OAuth2 client (port 8080)
- **Frontend**: Angular 18 (standalone components) (port 4200)

## Prerequisites

- Java 17+
- Node.js 18+
- Maven 3.8+

## Development Mode (Separated Frontend & Backend)

For development, the frontend and backend run on separate ports:

### 1. Install Frontend Dependencies

```bash
cd frontend
npm install
```

### 2. Start Frontend Dev Server

```bash
cd frontend
npm start
```

This runs the Angular dev server on `http://localhost:4200` with hot reload enabled.

### 3. Start Backend Server

```bash
# From project root (in a separate terminal)
mvn spring-boot:run
```

This runs the Spring Boot backend on `http://localhost:8080`.

### 4. Access the Application

Open your browser at `http://localhost:4200`

The frontend will communicate with the backend via CORS-enabled API calls.

## Production Mode (Combined)

For production deployment, the frontend is bundled and served by the backend:

### 1. Build Frontend

```bash
cd frontend
npm run build
```

This will build the Angular app and output it to `../src/main/resources/static`.

### 2. Run Backend

```bash
# From project root
mvn spring-boot:run
```

Or build and run:

```bash
mvn clean package
java -jar target/oauth2-demo-0.0.1-SNAPSHOT.jar
```

### 3. Access the Application

After running the backend, access the application at:

```
http://localhost:8080
```

## API Endpoints

- `GET /api/auth/user` - Get current user info
- `GET /api/auth/login-success` - Login success callback
- `GET /api/auth/login-failure` - Login failure callback
- `GET /api/auth/health` - Health check
- `POST /logout` - Logout endpoint

## OAuth2 Configuration

OAuth2 configuration is in `src/main/resources/application.yaml`. Update with your Azure AD credentials:

### Development Mode Redirect URIs

Register these redirect URIs in your Azure app registration:
- `http://localhost:8080/login/oauth2/code/azure-dev` (backend callback)

### Production Mode Redirect URIs

Register these redirect URIs in your Azure app registration:
- `http://localhost:8080/login/oauth2/code/azure-dev` (backend callback)
- `https://yourdomain.com/login/oauth2/code/azure-dev` (production backend callback)

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          azure-dev:
            provider: azure
            client-id: YOUR_CLIENT_ID
            client-secret: YOUR_CLIENT_SECRET
            scope:
              - openid
              - profile
              - email
        provider:
          azure:
            issuer-uri: YOUR_ISSUER_URI
```

## Building for Production

To build the complete application (frontend + backend):

```bash
# Build frontend
cd frontend
npm run build
cd ..

# Build backend
mvn clean package
```

The JAR file will be at `target/oauth2-demo-0.0.1-SNAPSHOT.jar`.

## Configuration Notes

- **CORS**: The backend is configured to allow requests from `http://localhost:4200` for development
- **Session Management**: The backend uses session-based authentication with JSESSIONID cookies
- **Login/Logout**: After successful login or logout, users are redirected to the frontend URL (http://localhost:4200 in development)
