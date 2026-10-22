import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService, UserInfo } from '../auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="welcome" id="overview">
      <div>
        <p class="eyebrow">YOUR WORKSPACE</p>
        <h1>Good to see you<span class="period">.</span></h1>
        <p class="intro">Everything you need, all in one calm place.</p>
      </div>
      <div class="status-pill" [class.signed-in]="userInfo?.authenticated">
        <span class="status-dot"></span>
        {{ loading ? 'Checking status' : userInfo?.authenticated ? 'Signed in' : 'Not signed in' }}
      </div>
    </section>

    <section class="welcome-card">
      <div class="welcome-icon">✦</div>
      <div class="welcome-copy">
        <h2>{{ userInfo?.authenticated ? 'Welcome back' + (userInfo?.name ? ', ' + userInfo?.name : '') : 'Welcome to your dashboard' }}</h2>
        <p *ngIf="userInfo?.authenticated; else guestMessage">
          You’re all set. Your workspace is ready whenever you are.
        </p>
        <ng-template #guestMessage>
          Sign in to access your account and keep your workspace personal.
        </ng-template>
      </div>
      <a *ngIf="!loading && !userInfo?.authenticated" class="button button-primary" routerLink="/login">Sign in</a>
      <a *ngIf="userInfo?.authenticated" class="button button-secondary" routerLink="/logout">Sign out</a>
    </section>

    <section class="section-heading">
      <div>
        <p class="eyebrow">QUICK ACCESS</p>
        <h2>Explore your space</h2>
      </div>
      <a class="text-link" href="#resources">View all links <span aria-hidden="true">→</span></a>
    </section>

    <section class="link-grid" aria-label="Dashboard links">
      <a class="link-card" href="#account">
        <span class="card-icon green-icon" aria-hidden="true">◉</span>
        <span class="card-copy">
          <strong>My account</strong>
          <span>View your sign-in details</span>
        </span>
        <span class="arrow" aria-hidden="true">↗</span>
      </a>
      <a class="link-card" href="#activity">
        <span class="card-icon blue-icon" aria-hidden="true">◷</span>
        <span class="card-copy">
          <strong>Recent activity</strong>
          <span>See what’s happening</span>
        </span>
        <span class="arrow" aria-hidden="true">↗</span>
      </a>
      <a class="link-card" href="#resources">
        <span class="card-icon amber-icon" aria-hidden="true">▧</span>
        <span class="card-copy">
          <strong>Resources</strong>
          <span>Useful links and shortcuts</span>
        </span>
        <span class="arrow" aria-hidden="true">↗</span>
      </a>
    </section>

    <section class="details-grid">
      <article class="detail-card" id="account">
        <div class="detail-heading">
          <h2>Account</h2>
          <span class="small-icon">◉</span>
        </div>
        <p *ngIf="userInfo?.authenticated; else accountPrompt">
          Signed in as <strong>{{ userInfo?.email || userInfo?.name || 'your account' }}</strong>
        </p>
        <ng-template #accountPrompt>
          <p>Sign in to see your account details here.</p>
        </ng-template>
        <a class="text-link" [routerLink]="userInfo?.authenticated ? '/logout' : '/login'">
          {{ userInfo?.authenticated ? 'Sign out' : 'Go to login' }} <span aria-hidden="true">→</span>
        </a>
      </article>

      <article class="detail-card" id="activity">
        <div class="detail-heading">
          <h2>Recent activity</h2>
          <span class="small-icon blue-icon">◷</span>
        </div>
        <p>Your workspace is ready. New activity will show up here.</p>
        <a class="text-link" href="#overview">Back to overview <span aria-hidden="true">↑</span></a>
      </article>

      <article class="detail-card" id="resources">
        <div class="detail-heading">
          <h2>Helpful links</h2>
          <span class="small-icon amber-icon">▧</span>
        </div>
        <p>Jump to an area of your workspace.</p>
        <div class="resource-links">
          <a href="#account">Account <span aria-hidden="true">→</span></a>
          <a href="#activity">Activity <span aria-hidden="true">→</span></a>
          <a routerLink="/login">Login <span aria-hidden="true">→</span></a>
          <a routerLink="/logout">Logout <span aria-hidden="true">→</span></a>
        </div>
      </article>
    </section>
  `
})
export class HomeComponent implements OnInit {
  userInfo: UserInfo | null = null;
  loading = true;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getUserInfo().subscribe({
      next: (data) => {
        console.log('User info retrieved:', data);
        this.userInfo = data;
        this.loading = false;
      },
      error: () => {
        this.userInfo = { authenticated: false };
        this.loading = false;
      }
    });
  }
}
