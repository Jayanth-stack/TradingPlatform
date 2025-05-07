import axios from '../utils/axios';
import { WatchListItem } from '../types';

const watchlistService = {
  async getWatchlist(): Promise<WatchListItem[]> {
    const response = await axios.get<WatchListItem[]>('/api/watchlist');
    return response.data;
  },

  async addToWatchlist(coinId: string): Promise<WatchListItem> {
    const response = await axios.post<WatchListItem>('/api/watchlist', { coinId });
    return response.data;
  },

  async removeFromWatchlist(coinId: string): Promise<void> {
    await axios.delete(`/api/watchlist/${coinId}`);
  }
};

export default watchlistService; 