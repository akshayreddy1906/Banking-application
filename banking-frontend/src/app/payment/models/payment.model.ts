export interface TransferRequest {
  senderAccountNumber: string;
  receiverAccountNumber: string;
  amount: number;
  description?: string;
}

export interface Payment {
  paymentId: number;
  senderEmail: string;
  senderAccountNumber: string;
  receiverEmail: string;
  receiverAccountNumber: string;
  amount: number;
  timestamp: string;
  status: PaymentStatus;
  description?: string;
}

export interface PaymentResponse {
  paymentId: number;
  status: PaymentStatus;
  message: string;
  timestamp: string;
  transactionId: string;
}

export type PaymentStatus = 'SUCCESS' | 'FAILURE' | 'PENDING';
