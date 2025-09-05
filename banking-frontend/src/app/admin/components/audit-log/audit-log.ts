import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuditLog } from '../../../account/models/account.model';
import { AuditService } from '../../../audit/services/audit';

@Component({
  selector: 'app-audit-log',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatTableModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './audit-log.html',
  styleUrls: ['./audit-log.css']
})
export class AuditLogComponent implements OnInit {
  private auditService = inject(AuditService);

  logs: AuditLog[] = [];
  displayedColumns: string[] = ['auditId', 'transactionType', 'senderAccount', 'receiverAccount', 'amount', 'status', 'timestamp'];
  isLoading = true;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadLogs();
  }

  loadLogs(): void {
    this.isLoading = true;
    this.auditService.getTransactionLogs().subscribe({
      next: (data) => {
        this.logs = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to load audit logs.';
        this.isLoading = false;
      }
    });
  }
}
