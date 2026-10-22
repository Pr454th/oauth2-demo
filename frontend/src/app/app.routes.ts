import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';
import { HomeComponent } from './home/home.component';
import { LoginRedirectComponent } from './login-redirect/login-redirect.component';
import { LogoutComponent } from './logout/logout.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  { path: 'dashboard', component: HomeComponent, canActivate: [authGuard] },
  { path: 'login', component: LoginRedirectComponent },
  { path: 'logout', component: LogoutComponent },
  { path: '**', redirectTo: 'dashboard' }
];
