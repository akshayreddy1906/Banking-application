export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  jwt: string;
  userId: number;
  userRole: 'USER' | 'ADMIN';
}

export interface SignupRequest {
  name: string;
  email: string;
  password: string;
}

export interface User {
  id: number;
  name: string;
  email: string;
  role: 'USER' | 'ADMIN';
}
