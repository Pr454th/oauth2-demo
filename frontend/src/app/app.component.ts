import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  template: `
    <header class="topbar">
      <a class="brand" routerLink="/dashboard" aria-label="Northstar home">
        <span class="brand-mark">N</span>
        <span>northstar</span>
      </a>

      <nav class="main-nav" aria-label="Main navigation">
        <a routerLink="/dashboard" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">Dashboard</a>
        <a routerLink="/login" routerLinkActive="active">Login</a>
        <a class="nav-logout" routerLink="/logout" routerLinkActive="active">Logout</a>
      </nav>
    </header>

    <main class="page-shell">
      <router-outlet></router-outlet>
    </main>

    <footer class="site-footer">
      <span>© 2026 Northstar</span>
      <span>A simpler way to stay on track.</span>
    </footer>
  `,
  styles: [`
    :host {
      display: flex;
      min-height: 100vh;
      flex-direction: column;
    }

    .topbar {
      display: flex;
      min-height: 76px;
      align-items: center;
      justify-content: space-between;
      padding: 0 max(6vw, 24px);
      border-bottom: 1px solid var(--line);
      background: rgba(255, 255, 255, 0.88);
    }

    .brand {
      display: inline-flex;
      align-items: center;
      gap: 11px;
      color: var(--ink);
      font-size: 18px;
      font-weight: 750;
      letter-spacing: -0.6px;
      text-decoration: none;
    }

    .brand-mark {
      display: grid;
      width: 34px;
      height: 34px;
      place-items: center;
      border-radius: 11px;
      background: var(--green);
      color: white;
      font-size: 17px;
    }

    .main-nav {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .main-nav a {
      padding: 10px 14px;
      border-radius: 9px;
      color: var(--muted);
      font-size: 14px;
      font-weight: 600;
      text-decoration: none;
      transition: background 160ms ease, color 160ms ease;
    }

    .main-nav a:hover,
    .main-nav a.active {
      background: var(--green-pale);
      color: var(--green-dark);
    }

    .main-nav .nav-logout {
      margin-left: 4px;
      border: 1px solid var(--line);
      color: var(--ink);
    }

    .page-shell {
      width: min(1080px, calc(100% - 48px));
      flex: 1;
      margin: 0 auto;
      padding: 52px 0 64px;
    }

    .site-footer {
      display: flex;
      justify-content: space-between;
      padding: 22px max(6vw, 24px);
      border-top: 1px solid var(--line);
      color: var(--muted);
      font-size: 12px;
    }

    @media (max-width: 560px) {
      .topbar {
        min-height: 68px;
        padding: 0 16px;
      }

      .brand {
        gap: 8px;
        font-size: 16px;
      }

      .main-nav {
        gap: 2px;
      }

      .main-nav a {
        padding: 9px 8px;
        font-size: 12px;
      }

      .main-nav .nav-logout {
        margin-left: 0;
      }

      .page-shell {
        width: min(100% - 32px, 1080px);
        padding: 36px 0 48px;
      }

      .site-footer {
        gap: 12px;
        padding: 18px 16px;
      }
    }
  `]
})
export class AppComponent {}
