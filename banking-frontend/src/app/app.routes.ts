import { Routes } from '@angular/router';
import { LoginComponent } from './auth/components/login/login';
import { RegisterComponent } from './auth/components/register/register';
import { DashboardComponent } from './dashboard/dashboard';
import { authGuard } from './auth/guards/auth-guard';
import { roleGuard } from './auth/guards/role-guard';
import { CustomerListComponent } from './customer/components/customer-list/customer-list';
import { CustomerCreateComponent } from './customer/components/customer-create/customer-create';
import { CustomerDetailComponent } from './customer/components/customer-detail/customer-detail';
import { CustomerEditComponent } from './customer/components/customer-edit/customer-edit';
import { AccountListComponent } from './account/components/account-list/account-list';
import { AccountCreateComponent } from './account/components/account-create/account-create';
import { AccountDetailComponent } from './account/components/account-detail/account-detail';
import { TransactionHistoryComponent } from './account/components/transaction-history/transaction-history';
import { PaymentTransferComponent } from './payment/components/payment-transfer/payment-transfer';
import { PaymentHistoryComponent } from './payment/components/payment-history/payment-history';
import { PaymentDetailComponent } from './payment/components/payment-detail/payment-detail';
import { AdminAccountComponent } from './admin/components/admin-account/admin-account';
import { AuditLogComponent } from './admin/components/audit-log/audit-log';
import { FeedbackComponent } from './feedback/feedback';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'admin/customers',
    component: CustomerListComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'customers/create',
    component: CustomerCreateComponent,
    canActivate: [authGuard]
  },
  {
    path: 'customers/:id',
    component: CustomerDetailComponent,
    canActivate: [authGuard]
  },
  {
    path: 'customers/:id/edit',
    component: CustomerEditComponent,
    canActivate: [authGuard]
  },
  {
    path: 'accounts',
    component: AccountListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'accounts/create',
    component: AccountCreateComponent,
    canActivate: [authGuard]
  },
  {
    path: 'accounts/:accountNumber',
    component: AccountDetailComponent,
    canActivate: [authGuard]
  },
  {
    path: 'accounts/:accountNumber/transactions',
    component: TransactionHistoryComponent,
    canActivate: [authGuard]
  },
  {
    path: 'payments/transfer',
    component: PaymentTransferComponent,
    canActivate: [authGuard]
  },
  {
    path: 'payments/history',
    component: PaymentHistoryComponent,
    canActivate: [authGuard]
  },
  {
    path: 'payments/:paymentId',
    component: PaymentDetailComponent,
    canActivate: [authGuard]
  },
  {
    path: 'admin/accounts',
    component: AdminAccountComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/audit',
    component: AuditLogComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'feedback',
    component: FeedbackComponent,
    canActivate: [authGuard]
  },
];
