import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Account,
  AccountRequest,
  TransactionResponse,
  BalanceResponse,
} from '../models/account.model';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8765/accounts';

  createAccount(account: AccountRequest): Observable<Account> {
    return this.http.post<Account>(`${this.apiUrl}/`, account);
  }

  getAccount(accountNumber: number): Observable<Account> {
    return this.http.get<Account>(`${this.apiUrl}/${accountNumber}`);
  }

  deleteAccount(accountNumber: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${accountNumber}`);
  }

  deposit(accountNumber: number, amount: number): Observable<TransactionResponse> {
    return this.http.put<TransactionResponse>(`${this.apiUrl}/${accountNumber}/deposit`, {
      amount,
    });
  }

  withdraw(accountNumber: number, amount: number): Observable<TransactionResponse> {
    return this.http.put<TransactionResponse>(`${this.apiUrl}/${accountNumber}/withdraw`, {
      amount,
    });
  }

  getBalance(accountNumber: number): Observable<BalanceResponse> {
    return this.http.get<BalanceResponse>(`${this.apiUrl}/${accountNumber}/balance`);
  }

  getAccountsByPan(panCardNumber: string): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.apiUrl}/customer/${panCardNumber}`);
  }

  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.apiUrl}`);
  }
}
