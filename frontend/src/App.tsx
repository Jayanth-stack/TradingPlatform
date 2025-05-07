import React, { lazy, Suspense, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import CssBaseline from '@mui/material/CssBaseline';
import { CircularProgress, Box } from '@mui/material';
import { ThemeProvider } from './components/ui/ThemeProvider';
import AppLayout from './components/layout/AppLayout';
import useAuthStore from './store/authStore';

// Lazy loaded components with code splitting for better performance
const Login = lazy(() => import('./features/auth/Login'));
const Signup = lazy(() => import('./features/auth/Signup')); 
const TwoFactorAuth = lazy(() => import('./features/auth/TwoFactorAuth'));
const Dashboard = lazy(() => import('./features/dashboard/Dashboard'));
const Market = lazy(() => import('./features/market/Market'));
const Trading = lazy(() => import('./features/market/Trading'));
const CoinDetail = lazy(() => import('./features/market/CoinDetail'));
const Wallet = lazy(() => import('./features/wallet/Wallet'));
const Orders = lazy(() => import('./features/orders/Orders'));
const Watchlist = lazy(() => import('./features/watchlist/Watchlist'));
const Profile = lazy(() => import('./features/profile/Profile'));
const NotFound = lazy(() => import('./features/NotFound'));

// Create Query Client for data fetching
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

// Loading component for suspense fallback
const Loader = () => (
  <Box
    sx={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh',
    }}
  >
    <CircularProgress />
  </Box>
);

// Protected route component
const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated, isLoading, fetchCurrentUser } = useAuthStore();

  useEffect(() => {
    // Try to fetch current user if not already authenticated
    if (!isAuthenticated && !isLoading) {
      fetchCurrentUser();
    }
  }, [isAuthenticated, isLoading, fetchCurrentUser]);

  if (isLoading) {
    return <Loader />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};

// Public route component that redirects to dashboard if user is authenticated
const PublicRoute = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated } = useAuthStore();

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  return <>{children}</>;
};

const App: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider>
        <CssBaseline />
        <BrowserRouter>
          <Suspense fallback={<Loader />}>
            <Routes>
              {/* Public Routes */}
              <Route
                path="/login"
                element={
                  <PublicRoute>
                    <Login />
                  </PublicRoute>
                }
              />
              <Route
                path="/signup"
                element={
                  <PublicRoute>
                    <Signup />
                  </PublicRoute>
                }
              />
              <Route
                path="/two-factor-auth"
                element={
                  <PublicRoute>
                    <TwoFactorAuth />
                  </PublicRoute>
                }
              />

              {/* Protected Routes */}
              <Route
                path="/dashboard"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Dashboard />
                    </AppLayout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/market"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Market />
                    </AppLayout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/trading"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Trading />
                    </AppLayout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/coin/:id"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <CoinDetail />
                    </AppLayout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/wallet"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Wallet />
                    </AppLayout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/orders"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Orders />
                    </AppLayout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/watchlist"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Watchlist />
                    </AppLayout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/profile"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Profile />
                    </AppLayout>
                  </ProtectedRoute>
                }
              />

              {/* Default Routes */}
              <Route path="/" element={<Navigate to="/dashboard" replace />} />
              <Route path="*" element={<NotFound />} />
            </Routes>
          </Suspense>
        </BrowserRouter>
      </ThemeProvider>
    </QueryClientProvider>
  );
};

export default App;
