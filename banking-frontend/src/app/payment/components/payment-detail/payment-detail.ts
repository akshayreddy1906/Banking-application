import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatListModule } from '@angular/material/list';
import { Payment } from '../../models/payment.model';
import { PaymentService } from '../../services/payment';

@Component({
  selector: 'app-payment-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatListModule
  ],
  templateUrl: './payment-detail.html',
  styleUrls: ['./payment-detail.css']
})
export class PaymentDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private paymentService = inject(PaymentService);

  payment: Payment | null = null;
  isLoading = true;
  errorMessage: string | null = null;

  ngOnInit(): void {
    const paymentId = this.route.snapshot.paramMap.get('paymentId');
    if (paymentId) {
      this.loadPayment(Number(paymentId));
    }
  }

  loadPayment(id: number): void {
    this.isLoading = true;
    this.paymentService.getPayment(id).subscribe({
      next: (data) => {
        this.payment = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to load payment details.';
        this.isLoading = false;
      }
    });
  }
}
