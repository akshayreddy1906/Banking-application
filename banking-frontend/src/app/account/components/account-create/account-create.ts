import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { AccountService } from '../../services/account';

@Component({
  selector: 'app-account-create',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule
  ],
  templateUrl: './account-create.html',
  styleUrls: ['./account-create.css']
})
export class AccountCreateComponent {
  private fb = inject(FormBuilder);
  private accountService = inject(AccountService);
  private router = inject(Router);

  accountForm: FormGroup = this.fb.group({
    panCardNumber: ['', [Validators.required, Validators.pattern(/^[A-Z]{5}[0-9]{4}[A-Z]{1}$/)]],
    username: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(6)]],
    accountType: ['', Validators.required]
  });
  errorMessage: string | null = null;

  onSubmit(): void {
    if (this.accountForm.valid) {
      this.accountService.createAccount(this.accountForm.value).subscribe({
        next: () => {
          this.router.navigate(['/accounts']);
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to create account.';
        }
      });
    }
  }
}
