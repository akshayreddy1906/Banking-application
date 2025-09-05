import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Account } from '../../../account/models/account.model';
import { AccountService } from '../../../account/services/account';

@Component({
  selector: 'app-admin-account',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './admin-account.html',
  styleUrls: ['./admin-account.css']
})
export class AdminAccountComponent implements OnInit {
  private accountService = inject(AccountService);

  accounts: Account[] = [];
  displayedColumns: string[] = ['accountNumber', 'username', 'panCardNumber', 'accountType', 'balance', 'actions'];
  isLoading = true;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.isLoading = true;
    this.accountService.getAllAccounts().subscribe({
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

  deleteAccount(accountNumber: number): void {
    if (confirm('Are you sure you want to delete this account?')) {
      this.accountService.deleteAccount(accountNumber).subscribe({
        next: () => {
          this.loadAccounts();
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to delete account.';
        }
      });
    }
  }
}
