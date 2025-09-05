import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Payment } from '../../models/payment.model';
import { PaymentService } from '../../services/payment';

@Component({
  selector: 'app-payment-history',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './payment-history.html',
  styleUrls: ['./payment-history.css']
})
export class PaymentHistoryComponent {
  private fb = inject(FormBuilder);
  private paymentService = inject(PaymentService);

  searchForm: FormGroup = this.fb.group({
    accountNumber: ['', Validators.required]
  });
  payments: Payment[] = [];
  displayedColumns: string[] = ['paymentId', 'senderAccountNumber', 'receiverAccountNumber', 'amount', 'status', 'timestamp'];
  isLoading = false;
  errorMessage: string | null = null;
  searched = false;

  searchHistory(): void {
    if (this.searchForm.valid) {
      this.isLoading = true;
      this.searched = true;
      const accountNumber = this.searchForm.value.accountNumber;
      this.paymentService.getPaymentHistory(accountNumber).subscribe({
        next: (data) => {
          this.payments = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load payment history.';
          this.isLoading = false;
        }
      });
    }
  }
}
