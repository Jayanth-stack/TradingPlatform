import React, { useEffect, useState } from 'react';
import {
  Grid,
  Typography,
  Box,
  Skeleton,
  Avatar,
  Divider,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  ListItemSecondaryAction,
  Chip
} from '@mui/material';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import TrendingDownIcon from '@mui/icons-material/TrendingDown';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { useQuery } from '@tanstack/react-query';
import Card from '../../components/ui/Card';
import { useTheme } from '../../components/ui/ThemeProvider';
import useAuthStore from '../../store/authStore';
import coinService from '../../services/coinService';
import walletService from '../../services/walletService';
import orderService from '../../services/orderService';
import { Coin, Order, OrderType, WalletTransaction } from '../../types';
import { formatCurrency } from '@/utils/formatters';
import PriceChangeDisplay from '@/components/ui/PriceChangeDisplay';
import PortfolioChart from '@/components/charts/PortfolioChart';

const Dashboard: React.FC = () => {
  const { user } = useAuthStore();
  useTheme();
  const [totalPortfolioValue, setTotalPortfolioValue] = useState<number>(0);

  // Fetch top trending coins
  const { data: topCoins, isLoading: isCoinsLoading } = useQuery(
    ['top-coins'],
    () => coinService.getTop50Coins(),
    {
      select: (data) => data.slice(0, 5),
    }
  );

  // Fetch user wallet
  const { data: wallet, isLoading: isWalletLoading } = useQuery(
    ['wallet'],
    () => walletService.getWallet()
  );

  // Fetch recent transactions
  const { data: transactions, isLoading: isTransactionsLoading } = useQuery(
    ['wallet-transactions'],
    () => walletService.getWalletTransactions(),
    {
      select: (data) => data.slice(0, 5),
    }
  );

  // Fetch recent orders
  const { data: orders, isLoading: isOrdersLoading } = useQuery(
    ['orders'],
    () => orderService.getUserOrders(),
    {
      select: (data) => data.slice(0, 5),
    }
  );

  // Calculate portfolio value (simplified for demo)
  useEffect(() => {
    if (wallet) {
      setTotalPortfolioValue(wallet.balance);
    }
  }, [wallet]);

  return (
    <Box sx={{ py: 2 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" fontWeight="700" sx={{ mb: 1 }}>
          Welcome back, {user?.fullName?.split(' ')[0] || 'User'}!
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Here's an overview of your portfolio and market trends
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {/* Portfolio Value Card */}
        <Grid item xs={12} md={8}>
          <Card
            title="Portfolio Value"
            subtitle="Your total assets value"
          >
            {isWalletLoading ? (
              <Skeleton variant="rectangular" height={250} />
            ) : (
              <>
                <Box sx={{ mb: 3 }}>
                  <Typography variant="h3" component="div" fontWeight="700">
                    {formatCurrency(totalPortfolioValue)}
                  </Typography>
                  <PriceChangeDisplay value={2.5} />
                </Box>
                <PortfolioChart />
              </>
            )}
          </Card>
        </Grid>

        {/* Wallet Card */}
        <Grid item xs={12} sm={6} md={4}>
          <Card
            title="Wallet Balance"
            headerAction={
              <Avatar
                sx={{
                  bgcolor: 'primary.main',
                  width: 36,
                  height: 36,
                }}
              >
                <AccountBalanceWalletIcon fontSize="small" />
              </Avatar>
            }
          >
            {isWalletLoading ? (
              <Skeleton variant="rectangular" height={100} />
            ) : (
              <>
                <Typography variant="h4" component="div" fontWeight="700" sx={{ mb: 3 }}>
                  {formatCurrency(wallet?.balance || 0)}
                </Typography>
                
                <Divider sx={{ my: 2 }} />
                
                <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 1 }}>
                  Recent Transactions
                </Typography>
                
                {isTransactionsLoading ? (
                  <Skeleton variant="rectangular" height={150} />
                ) : (
                  <List disablePadding>
                    {(transactions || []).map((transaction: WalletTransaction) => (
                      <ListItem
                        key={transaction.id}
                        disablePadding
                        sx={{ py: 1 }}
                      >
                        <ListItemText
                          primary={transaction.description}
                          secondary={new Date(transaction.createdAt).toLocaleDateString()}
                          primaryTypographyProps={{ variant: 'body2' }}
                          secondaryTypographyProps={{ variant: 'caption' }}
                        />
                        <ListItemSecondaryAction>
                          <Typography
                            variant="body2"
                            color={
                              transaction.transactionType === 'DEPOSIT' || transaction.transactionType === 'SALE'
                                ? 'success.main'
                                : 'error.main'
                            }
                            fontWeight="600"
                          >
                            {transaction.transactionType === 'DEPOSIT' || transaction.transactionType === 'SALE'
                              ? '+'
                              : '-'}
                            {formatCurrency(transaction.amount)}
                          </Typography>
                        </ListItemSecondaryAction>
                      </ListItem>
                    ))}
                  </List>
                )}
              </>
            )}
          </Card>
        </Grid>

        {/* Recent Orders Card */}
        <Grid item xs={12} sm={6} md={6}>
          <Card
            title="Recent Orders"
            headerAction={
              <Avatar
                sx={{
                  bgcolor: 'secondary.main',
                  width: 36,
                  height: 36,
                }}
              >
                <ShoppingCartIcon fontSize="small" />
              </Avatar>
            }
          >
            {isOrdersLoading ? (
              <Skeleton variant="rectangular" height={250} />
            ) : (
              <List disablePadding>
                {(orders || []).map((order: Order) => (
                  <ListItem
                    key={order.id}
                    disablePadding
                    divider
                    sx={{ py: 1.5 }}
                  >
                    <ListItemText
                      primary={`${order.orderType === OrderType.BUY ? 'Buy' : 'Sell'} ${order.coinId}`}
                      secondary={new Date(order.createdAt).toLocaleDateString()}
                      primaryTypographyProps={{ variant: 'body2', fontWeight: 600 }}
                      secondaryTypographyProps={{ variant: 'caption' }}
                    />
                    <ListItemSecondaryAction>
                      <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end' }}>
                        <Typography variant="body2" fontWeight="600">
                          {formatCurrency(order.totalAmount)}
                        </Typography>
                        <Chip
                          label={order.status}
                          size="small"
                          color={
                            order.status === 'COMPLETED'
                              ? 'success'
                              : order.status === 'PENDING'
                              ? 'warning'
                              : 'error'
                          }
                          sx={{ mt: 0.5, height: 20 }}
                        />
                      </Box>
                    </ListItemSecondaryAction>
                  </ListItem>
                ))}
              </List>
            )}
          </Card>
        </Grid>

        {/* Market Trends Card */}
        <Grid item xs={12} md={6}>
          <Card
            title="Market Trends"
            subtitle="Top performing assets"
          >
            {isCoinsLoading ? (
              <Skeleton variant="rectangular" height={250} />
            ) : (
              <List disablePadding>
                {(topCoins || []).map((coin: Coin) => (
                  <ListItem
                    key={coin.id}
                    disablePadding
                    divider
                    sx={{ py: 1.5 }}
                  >
                    <ListItemAvatar>
                      <Avatar src={coin.image} alt={coin.name} sx={{ width: 32, height: 32 }} />
                    </ListItemAvatar>
                    <ListItemText
                      primary={coin.name}
                      secondary={coin.symbol.toUpperCase()}
                      primaryTypographyProps={{ variant: 'body2', fontWeight: 600 }}
                      secondaryTypographyProps={{ variant: 'caption' }}
                    />
                    <ListItemSecondaryAction>
                      <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end' }}>
                        <Typography variant="body2" fontWeight="600">
                          {formatCurrency(coin.currentPrice)}
                        </Typography>
                        <Box sx={{ display: 'flex', alignItems: 'center', mt: 0.5 }}>
                          {coin.priceChangePercentage24h >= 0 ? (
                            <TrendingUpIcon
                              fontSize="inherit"
                              sx={{ color: 'success.main', fontSize: 16, mr: 0.5 }}
                            />
                          ) : (
                            <TrendingDownIcon
                              fontSize="inherit"
                              sx={{ color: 'error.main', fontSize: 16, mr: 0.5 }}
                            />
                          )}
                          <Typography
                            variant="caption"
                            color={
                              coin.priceChangePercentage24h >= 0
                                ? 'success.main'
                                : 'error.main'
                            }
                            fontWeight="600"
                          >
                            {Math.abs(coin.priceChangePercentage24h).toFixed(2)}%
                          </Typography>
                        </Box>
                      </Box>
                    </ListItemSecondaryAction>
                  </ListItem>
                ))}
              </List>
            )}
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard; 