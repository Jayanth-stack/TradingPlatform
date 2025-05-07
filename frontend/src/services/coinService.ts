import axios from '../utils/axios';
import { Coin, ChartData } from '../types';

const coinService = {
  async getAllCoins(): Promise<Coin[]> {
    const response = await axios.get<Coin[]>('/api/assets');
    return response.data;
  },

  async getCoinsWithPagination(page: number): Promise<Coin[]> {
    const response = await axios.get<Coin[]>(`/api/assets/coins?page=${page}`);
    return response.data;
  },

  async getMarketChart(coinId: string, days: number): Promise<ChartData> {
    const response = await axios.get<ChartData>(`/api/assets/${coinId}/chart?days=${days}`);
    return response.data;
  },

  async searchCoin(keyword: string): Promise<any> {
    const response = await axios.get<any>(`/api/assets/search?q=${keyword}`);
    return response.data;
  },

  async getTop50Coins(): Promise<any> {
    const response = await axios.get<any>('/api/assets/top50coin');
    return response.data;
  },

  async getTradingCoins(page: number): Promise<any> {
    const response = await axios.get<any>(`/api/assets/trading?page=${page}`);
    return response.data;
  },

  async getCoinDetails(coinId: string): Promise<any> {
    const response = await axios.get<any>(`/api/assets/coinDetails/${coinId}`);
    return response.data;
  }
};

export default coinService; 