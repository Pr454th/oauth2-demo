import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);

  return authService.getUserInfo().pipe(
    map((user) => {
      if (user.authenticated) {
        return true;
      }

      authService.login();
      return false;
    }),
    catchError((error: unknown) => {
      console.error('Unable to verify authentication status.', error);
      authService.login();
      return of(false);
    })
  );
};
