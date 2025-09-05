export interface Customer {
  contactId?: number;
  name: string;
  email: string;
  mobileNumber: string;
  address: string;
  age: number;
  gender: 'Male' | 'Female' | 'Other';
  panCardNumber: string;
  aadhaarCardNumber: string;
}

export interface Account {
  accountNumber?: number;
  panCardNumber: string;
  username: string;
  accountType: 'SAVINGS' | 'CURRENT';
  balance: number;
}
