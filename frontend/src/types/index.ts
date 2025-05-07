export interface User {
  id: number;
  fullName: string;
  email: string;
  role: UserRole;
  twoFactorAuth: TwoFactorAuth;
}

export enum UserRole {
  ROLE_CUSTOMER = 'ROLE_CUSTOMER',
  ROLE_ADMIN = 'ROLE_ADMIN'
}

export interface TwoFactorAuth {
  enabled: boolean;
}

export interface Coin {
  id: string;
  symbol: string;
  name: string;
  image: string;
  currentPrice: number;
  marketCap: number;
  marketCapRank: number;
  fullyDilutedValuation: number;
  totalVolume: number;
  high24h: number;
  low24h: number;
  priceChange24h: number;
  priceChangePercentage24h: number;
  marketCapChange24h: number;
  marketCapChangePercentage24h: number;
  circulatingSupply: number;
  totalSupply: number;
  maxSupply: number;
  ath: number;
  athChangePercentage: number;
  athDate: string;
  atl: number;
  atlChangePercentage: number;
  atlDate: string;
  lastUpdated: string;
}

export interface Asset {
  id: number;
  userId: number;
  coinId: string;
  quantity: number;
  purchasePrice: number;
  purchaseDate: string;
}

export interface WatchListItem {
  id: number;
  userId: number;
  coinId: string;
}

export enum OrderType {
  BUY = 'BUY',
  SELL = 'SELL'
}

export enum OrderStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  CANCELLED = 'CANCELLED'
}

export interface Order {
  id: number;
  userId: number;
  coinId: string;
  quantity: number;
  price: number;
  totalAmount: number;
  orderType: OrderType;
  status: OrderStatus;
  createdAt: string;
}

export interface Wallet {
  id: number;
  userId: number;
  balance: number;
}

export enum WalletTransactionType {
  DEPOSIT = 'DEPOSIT',
  WITHDRAWAL = 'WITHDRAWAL',
  PURCHASE = 'PURCHASE',
  SALE = 'SALE'
}

export interface WalletTransaction {
  id: number;
  walletId: number;
  amount: number;
  transactionType: WalletTransactionType;
  description: string;
  createdAt: string;
}

export enum WithdrawalStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  REJECTED = 'REJECTED'
}

export interface Withdrawal {
  id: number;
  userId: number;
  amount: number;
  status: WithdrawalStatus;
  createdAt: string;
}

export enum PaymentMethod {
  CREDIT_CARD = 'CREDIT_CARD',
  DEBIT_CARD = 'DEBIT_CARD',
  BANK_TRANSFER = 'BANK_TRANSFER'
}

export interface PaymentDetails {
  id: number;
  userId: number;
  cardNumber: string;
  cardHolderName: string;
  expiryDate: string;
  paymentMethod: PaymentMethod;
}

export enum PaymentOrderStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED'
}

export interface PaymentOrder {
  id: number;
  userId: number;
  amount: number;
  status: PaymentOrderStatus;
  createdAt: string;
}

export interface ChartData {
  prices: [number, number][];
  market_caps: [number, number][];
  total_volumes: [number, number][];
}

export interface AuthResponse {
  status: boolean;
  message: string;
  twoFactorAuthEnabled?: boolean;
  session?: string;
  jwt?: string;
}

export interface CreateOrderRequest {
  coinId: string;
  quantity: number;
  orderType: OrderType;
} 