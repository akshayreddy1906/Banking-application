import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { PaymentService } from '../../services/payment';
import { NotificationService } from '../../../shared/services/notification';

@Component({
  selector: 'app-payment-transfer',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './payment-transfer.html',
  styleUrls: ['./payment-transfer.css']
})
export class PaymentTransferComponent {
  private fb = inject(FormBuilder);
  private paymentService = inject(PaymentService);
  private notificationService = inject(NotificationService);

  transferForm: FormGroup = this.fb.group({
    senderAccountNumber: ['', Validators.required],
    receiverAccountNumber: ['', Validators.required],
    amount: ['', [Validators.required, Validators.min(1)]],
    description: ['']
  });

  isLoading = false;

  onSubmit(): void {
    if (this.transferForm.valid) {
      this.isLoading = true;
      const formData = this.transferForm.value;
      
      this.paymentService.transferFunds(formData).subscribe({
        next: (res) => {
          const amount = formData.amount;
          const message = res.message || `Successfully transferred ₹${amount} from account ${formData.senderAccountNumber} to ${formData.receiverAccountNumber}`;
          this.notificationService.showSuccess(message);
          this.transferForm.reset();
          this.isLoading = false;
        },
        error: (err) => {
          this.notificationService.showError(err.error?.message || 'Payment Failed');
          this.isLoading = false;
        }
      });
    }
  }
}
