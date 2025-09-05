import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Payment, PaymentResponse, PaymentStatus, TransferRequest } from '../models/payment.model';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8765/payments';

  transferFunds(transferRequest: TransferRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(`${this.apiUrl}/transfer`, transferRequest);
  }

  getPayment(paymentId: number): Observable<Payment> {
    return this.http.get<Payment>(`${this.apiUrl}/${paymentId}`);
  }

  getPaymentHistory(accountNumber: string): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.apiUrl}/history/${accountNumber}`);
  }

  getPaymentsByStatus(status: PaymentStatus): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.apiUrl}/status/${status}`);
  }

  getPaymentsByDateRange(startDate: string, endDate: string): Observable<Payment[]> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get<Payment[]>(`${this.apiUrl}/date-range`, { params });
  }

  getPaymentsByAccount(accountNumber: string, limit?: number): Observable<Payment[]> {
    let params = new HttpParams();
    if (limit) {
      params = params.set('limit', limit.toString());
    }
    return this.http.get<Payment[]>(`${this.apiUrl}/account/${accountNumber}`, { params });
  }
}
