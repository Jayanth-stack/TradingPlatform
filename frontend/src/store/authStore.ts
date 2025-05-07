import { create } from 'zustand';
import { User, AuthResponse } from '../types';
import authService, { LoginCredentials, SignupCredentials } from '../services/authService';

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  twoFactorRequired: boolean;
  sessionId: string | null;
  error: string | null;
  
  // Actions
  login: (credentials: LoginCredentials) => Promise<void>;
  signup: (credentials: SignupCredentials) => Promise<void>;
  verify2FA: (otp: string) => Promise<void>;
  fetchCurrentUser: () => Promise<void>;
  logout: () => Promise<void>;
  setError: (error: string | null) => void;
}

const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  isAuthenticated: false,
  isLoading: false,
  twoFactorRequired: false,
  sessionId: null,
  error: null,

  login: async (credentials) => {
    set({ isLoading: true, error: null });
    try {
      const response = await authService.login(credentials);
      
      if (response.twoFactorAuthEnabled) {
        set({ 
          twoFactorRequired: true, 
          sessionId: response.session || null,
          isLoading: false 
        });
      } else {
        set({ 
          isAuthenticated: true, 
          twoFactorRequired: false,
          isLoading: false
        });
        // Fetch user data after successful login
        get().fetchCurrentUser();
      }
    } catch (error) {
      set({ 
        error: error instanceof Error ? error.message : 'Failed to login', 
        isLoading: false 
      });
    }
  },

  signup: async (credentials) => {
    set({ isLoading: true, error: null });
    try {
      await authService.signup(credentials);
      set({ 
        isAuthenticated: true, 
        isLoading: false 
      });
      // Fetch user data after successful signup
      get().fetchCurrentUser();
    } catch (error) {
      set({ 
        error: error instanceof Error ? error.message : 'Failed to signup', 
        isLoading: false 
      });
    }
  },

  verify2FA: async (otp) => {
    const { sessionId } = get();
    if (!sessionId) {
      set({ error: 'Session ID is missing' });
      return;
    }

    set({ isLoading: true, error: null });
    try {
      const response = await authService.verify2FA(otp, sessionId);
      set({ 
        isAuthenticated: true, 
        twoFactorRequired: false,
        sessionId: null,
        isLoading: false 
      });
      // Fetch user data after successful 2FA verification
      get().fetchCurrentUser();
    } catch (error) {
      set({ 
        error: error instanceof Error ? error.message : 'Failed to verify OTP', 
        isLoading: false 
      });
    }
  },

  fetchCurrentUser: async () => {
    set({ isLoading: true });
    try {
      const user = await authService.getCurrentUser();
      set({ user, isLoading: false, isAuthenticated: true });
    } catch (error) {
      set({ 
        user: null, 
        isAuthenticated: false, 
        isLoading: false,
        error: error instanceof Error ? error.message : 'Failed to fetch user'
      });
    }
  },

  logout: async () => {
    set({ isLoading: true });
    try {
      await authService.logout();
      set({ 
        user: null, 
        isAuthenticated: false, 
        isLoading: false 
      });
    } catch (error) {
      set({ 
        isLoading: false,
        error: error instanceof Error ? error.message : 'Failed to logout'
      });
    }
  },

  setError: (error) => set({ error })
}));

export default useAuthStore; 