import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer, Account } from '../models/customer.model';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8765/customers';

  createCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(`${this.apiUrl}/`, customer);
  }

  getCustomer(contactId: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${contactId}`);
  }

  updateCustomer(contactId: number, customer: Customer): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}/${contactId}`, customer);
  }

  deleteCustomer(contactId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${contactId}`);
  }

  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}/AllCustomers`);
  }

  getCustomerByPan(panCardNumber: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/pan/${panCardNumber}`);
  }

  getCustomerEmailByPan(panCardNumber: string): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/pan/${panCardNumber}/email`);
  }

  getCustomerAccounts(contactId: number): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.apiUrl}/${contactId}/accounts`);
  }
}
