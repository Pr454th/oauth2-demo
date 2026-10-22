
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserInfo {
  authenticated: boolean;
  name?: string;
  email?: string;
  attributes?: any;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private backendUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getUserInfo(): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.apiUrl}/user`);
  }

  login() {
    window.location.href = `${this.backendUrl}/oauth2/authorization/azure-dev`;
  }

  logout() {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `${this.backendUrl}/logout`;

    console.log('Submitting logout form to:', form.action);
    console.log('Form method:', form);
    document.body.appendChild(form);
    form.submit();
  }

  health(): Observable<any> {
    return this.http.get(`${this.apiUrl}/health`);
  }
}
