import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Account } from '../../models/account.model';
import { AccountService } from '../../services/account';

@Component({
  selector: 'app-account-list',
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
  templateUrl: './account-list.html',
  styleUrls: ['./account-list.css']
})
export class AccountListComponent {
  private fb = inject(FormBuilder);
  private accountService = inject(AccountService);
  private router = inject(Router);

  searchForm: FormGroup = this.fb.group({
    panCardNumber: ['', [Validators.required, Validators.pattern(/^[A-Z]{5}[0-9]{4}[A-Z]{1}$/)]]
  });
  accounts: Account[] = [];
  displayedColumns: string[] = ['accountNumber', 'username', 'accountType', 'balance', 'actions'];
  isLoading = false;
  errorMessage: string | null = null;
  searched = false;

  searchAccounts(): void {
    if (this.searchForm.valid) {
      this.isLoading = true;
      this.searched = true;
      const pan = this.searchForm.value.panCardNumber;
      this.accountService.getAccountsByPan(pan).subscribe({
        next: (data) => {
          this.accounts = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load accounts.';
          this.isLoading = false;
        }
      });
    }
  }

  viewAccount(accountNumber: number): void {
    this.router.navigate(['/accounts', accountNumber]);
  }
}
