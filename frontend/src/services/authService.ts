import axios from '../utils/axios';
import { AuthResponse, User } from '../types';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface SignupCredentials {
  fullName: string;
  email: string;
  password: string;
}

const authService = {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    const response = await axios.post<AuthResponse>('/auth/signin', credentials);
    return response.data;
  },

  async signup(credentials: SignupCredentials): Promise<AuthResponse> {
    const response = await axios.post<AuthResponse>('/auth/signup', credentials);
    return response.data;
  },

  async verify2FA(otp: string, sessionId: string): Promise<AuthResponse> {
    const response = await axios.post<AuthResponse>(`/auth/twofactor/otp/${otp}?id=${sessionId}`);
    return response.data;
  },

  async getCurrentUser(): Promise<User> {
    const response = await axios.get<User>('/api/users/profile');
    return response.data;
  },

  async logout(): Promise<void> {
    document.cookie = 'jwt=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT; Secure; HttpOnly;';
    window.location.href = '/login';
  }
};

export default authService; 