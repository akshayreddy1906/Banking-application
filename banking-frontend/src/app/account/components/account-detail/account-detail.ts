import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Account, BalanceResponse, TransactionResponse } from '../../models/account.model';
import { AccountService } from '../../services/account';
import { NotificationService } from '../../../shared/services/notification';

@Component({
  selector: 'app-account-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './account-detail.html',
  styleUrls: ['./account-detail.css']
})
export class AccountDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private accountService = inject(AccountService);
  private notificationService = inject(NotificationService);
  private fb = inject(FormBuilder);

  account: Account | null = null;
  transactionForm: FormGroup;
  isLoading = true;
  errorMessage: string | null = null;

  constructor() {
    this.transactionForm = this.fb.group({
      amount: ['', [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    const accountNumber = this.route.snapshot.paramMap.get('accountNumber');
    if (accountNumber) {
      this.loadAccount(Number(accountNumber));
    }
  }

  loadAccount(accountNumber: number): void {
    this.isLoading = true;
    this.accountService.getAccount(accountNumber).subscribe({
      next: (data) => {
        this.account = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to load account details.';
        this.isLoading = false;
      }
    });
  }

  deposit(): void {
    if (this.transactionForm.valid && this.account) {
      const amount = this.transactionForm.value.amount;
      this.accountService.deposit(this.account.accountNumber!, amount).subscribe({
        next: (res) => this.handleTransaction(res, 'deposit', amount),
        error: (err) => {
          this.notificationService.showError(err.error?.message || 'Deposit failed');
        }
      });
    }
  }

  withdraw(): void {
    if (this.transactionForm.valid && this.account) {
      const amount = this.transactionForm.value.amount;
      this.accountService.withdraw(this.account.accountNumber!, amount).subscribe({
        next: (res) => this.handleTransaction(res, 'withdraw', amount),
        error: (err) => {
          this.notificationService.showError(err.error?.message || 'Withdrawal failed');
        }
      });
    }
  }

  private handleTransaction(response: any, transactionType: 'deposit' | 'withdraw', amount: number): void {
    
    // Check if response has balance property (Account object) or success property (TransactionResponse)
    if (response.balance !== undefined || response.success) {
      const action = transactionType === 'deposit' ? 'Deposited' : 'Withdrawn';
      const newBalance = response.balance || response.newBalance || 0;
      const message = `${action} ₹${amount} successfully! New balance: ₹${newBalance}`;
      this.notificationService.showSuccess(message);
      
      if (this.account) {
        this.account.balance = newBalance;
      }
    } else {
      this.notificationService.showError(response.message || 'Transaction failed');
    }
    this.transactionForm.reset();
  }

  viewTransactionHistory(): void {
    this.router.navigate(['/accounts', this.account?.accountNumber, 'transactions']);
  }
}
