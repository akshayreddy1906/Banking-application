import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuditLog } from '../../models/account.model';
import { AuditService } from '../../../audit/services/audit';

@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatTableModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './transaction-history.html',
  styleUrls: ['./transaction-history.css']
})
export class TransactionHistoryComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private auditService = inject(AuditService);

  transactions: AuditLog[] = [];
  displayedColumns: string[] = ['auditId', 'transactionType', 'amount', 'status', 'timestamp'];
  isLoading = true;
  errorMessage: string | null = null;
  accountNumber!: string;

  ngOnInit(): void {
    const accNum = this.route.snapshot.paramMap.get('accountNumber');
    if (accNum) {
      this.accountNumber = accNum;
      this.loadTransactions();
    }
  }

  loadTransactions(): void {
    this.isLoading = true;
    this.auditService.getTransactionsByAccount(this.accountNumber).subscribe({
      next: (data) => {
        this.transactions = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to load transaction history.';
        this.isLoading = false;
      }
    });
  }
}
