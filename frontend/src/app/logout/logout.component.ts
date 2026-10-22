import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="auth-card">
      <a class="back-link" routerLink="/dashboard"><span aria-hidden="true">←</span> Back to dashboard</a>
      <div class="auth-icon" aria-hidden="true">↗</div>
      <p class="eyebrow">SIGN OUT</p>
      <h1>Leaving so soon?</h1>
      <p class="description">You can sign out of your Northstar workspace now. You can always come back anytime.</p>
      <button class="sign-out-button" type="button" (click)="logout()">Sign out</button>
      <a class="stay-link" routerLink="/dashboard">Stay signed in</a>
    </section>
  `,
  styles: [`
    .auth-card {
      width: min(100%, 440px);
      margin: 10px auto 0;
      padding: 34px;
      border: 1px solid var(--line);
      border-radius: 16px;
      background: white;
      box-shadow: 0 12px 35px rgba(32, 52, 38, 0.045);
      text-align: center;
    }

    .back-link {
      display: flex;
      width: fit-content;
      align-items: center;
      gap: 7px;
      margin-bottom: 35px;
      color: var(--muted);
      font-size: 12px;
      font-weight: 600;
      text-decoration: none;
    }

    .back-link:hover,
    .stay-link:hover {
      color: var(--green-dark);
    }

    .auth-icon {
      display: grid;
      width: 45px;
      height: 45px;
      margin: 0 auto 24px;
      place-items: center;
      border-radius: 13px;
      background: #f5f3ed;
      color: #887b5b;
      font-size: 22px;
      font-weight: 600;
    }

    .eyebrow {
      margin: 0 0 9px;
      color: var(--green-dark);
      font-size: 10px;
      font-weight: 750;
      letter-spacing: 1.5px;
    }

    h1 {
      margin: 0 0 10px;
      color: var(--ink);
      font-size: 27px;
      letter-spacing: -1px;
    }

    .description {
      margin: 0 0 25px;
      color: var(--muted);
      font-size: 13px;
      line-height: 1.65;
    }

    .sign-out-button {
      width: 100%;
      min-height: 48px;
      border: 0;
      border-radius: 9px;
      background: var(--green);
      color: white;
      cursor: pointer;
      font: inherit;
      font-size: 13px;
      font-weight: 650;
      transition: background 160ms ease;
    }

    .sign-out-button:hover {
      background: var(--green-dark);
    }

    .stay-link {
      display: inline-block;
      margin-top: 19px;
      color: var(--muted);
      font-size: 12px;
      font-weight: 600;
      text-decoration: none;
    }

    @media (max-width: 480px) {
      .auth-card {
        padding: 25px 21px;
      }
    }
  `]
})
export class LogoutComponent {
  constructor(private authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }
}
