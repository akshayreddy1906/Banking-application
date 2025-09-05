export interface Account {
  accountNumber?: number;
  panCardNumber: string;
  username: string;
  accountType: 'SAVINGS' | 'CURRENT';
  balance: number;
}

export interface AccountRequest {
  panCardNumber: string;
  username: string;
  password: string;
  accountType: 'SAVINGS' | 'CURRENT';
}

export interface TransactionResponse {
  success: boolean;
  message: string;
  newBalance: number;
  transactionId: string;
}

export interface BalanceResponse {
  accountNumber: number;
  balance: number;
  accountType: string;
}

export interface AuditLog {
  auditId: number;
  paymentId?: number;
  senderAccount: number;
  receiverAccount: number;
  amount: number;
  status: 'SUCCESS' | 'FAILURE';
  transactionType: 'TRANSFER' | 'DEPOSIT' | 'WITHDRAWAL' | 'PAYMENT';
  timestamp: string;
}
