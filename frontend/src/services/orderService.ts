import axios from '../utils/axios';
import { Order, OrderType, CreateOrderRequest } from '../types';

const orderService = {
  async createOrder(orderRequest: CreateOrderRequest): Promise<Order> {
    const response = await axios.post<Order>('/api/orders/pay', orderRequest);
    return response.data;
  },

  async getOrderById(orderId: number): Promise<Order> {
    const response = await axios.get<Order>(`/api/orders/${orderId}`);
    return response.data;
  },

  async getUserOrders(orderType?: OrderType, assetSymbol?: string): Promise<Order[]> {
    let url = '/api/orders';
    const headers: Record<string, string> = {};
    
    if (orderType) {
      headers['order_type'] = orderType;
    }
    
    if (assetSymbol) {
      headers['asset_symbol'] = assetSymbol;
    }
    
    const response = await axios.get<Order[]>(url, { headers });
    return response.data;
  }
};

export default orderService; 