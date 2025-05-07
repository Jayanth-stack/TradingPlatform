import axios from '../utils/axios';
import { Wallet, WalletTransaction, Withdrawal } from '../types';

interface DepositRequest {
  amount: number;
}

interface WithdrawalRequest {
  amount: number;
}

const walletService = {
  async getWallet(): Promise<Wallet> {
    const response = await axios.get<Wallet>('/api/wallet');
    return response.data;
  },

  async getWalletTransactions(): Promise<WalletTransaction[]> {
    const response = await axios.get<WalletTransaction[]>('/api/wallet/transactions');
    return response.data;
  },

  async deposit(amount: number): Promise<Wallet> {
    const depositData: DepositRequest = { amount };
    const response = await axios.post<Wallet>('/api/wallet/deposit', depositData);
    return response.data;
  },

  async withdraw(amount: number): Promise<Withdrawal> {
    const withdrawalData: WithdrawalRequest = { amount };
    const response = await axios.post<Withdrawal>('/api/withdrawals', withdrawalData);
    return response.data;
  },

  async getWithdrawals(): Promise<Withdrawal[]> {
    const response = await axios.get<Withdrawal[]>('/api/withdrawals');
    return response.data;
  }
};

export default walletService; 