import React from 'react';
import {
  Box,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Avatar
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import VisibilityIcon from '@mui/icons-material/Visibility';
import { useNavigate } from 'react-router-dom';
import PriceChangeDisplay from '../../components/ui/PriceChangeDisplay';

const Watchlist: React.FC = () => {
  const navigate = useNavigate();

  // Mock watchlist data
  const watchlistItems = [
    {
      id: 'bitcoin',
      name: 'Bitcoin',
      symbol: 'BTC',
      image: 'https://assets.coingecko.com/coins/images/1/large/bitcoin.png',
      currentPrice: 45670.25,
      priceChangePercentage24h: 2.45,
      marketCap: 874562145895
    },
    {
      id: 'ethereum',
      name: 'Ethereum',
      symbol: 'ETH',
      image: 'https://assets.coingecko.com/coins/images/279/large/ethereum.png',
      currentPrice: 1870.32,
      priceChangePercentage24h: -1.23,
      marketCap: 225481653424
    },
    {
      id: 'cardano',
      name: 'Cardano',
      symbol: 'ADA',
      image: 'https://assets.coingecko.com/coins/images/975/large/cardano.png',
      currentPrice: 0.54,
      priceChangePercentage24h: 3.78,
      marketCap: 19254786532
    },
    {
      id: 'solana',
      name: 'Solana',
      symbol: 'SOL',
      image: 'https://assets.coingecko.com/coins/images/4128/large/solana.png',
      currentPrice: 136.25,
      priceChangePercentage24h: 5.21,
      marketCap: 58765421358
    }
  ];

  // Format currency
  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(value);
  };

  // Format market cap
  const formatMarketCap = (value: number) => {
    if (value >= 1e12) {
      return `$${(value / 1e12).toFixed(2)}T`;
    } else if (value >= 1e9) {
      return `$${(value / 1e9).toFixed(2)}B`;
    } else if (value >= 1e6) {
      return `$${(value / 1e6).toFixed(2)}M`;
    } else {
      return formatCurrency(value);
    }
  };

  // Handle view coin details
  const handleViewCoin = (coinId: string) => {
    navigate(`/coin/${coinId}`);
  };

  // Handle remove from watchlist
  const handleRemoveFromWatchlist = (coinId: string) => {
    console.log(`Remove ${coinId} from watchlist`);
    // Will implement actual removal later
  };

  return (
    <Box sx={{ py: 2 }}>
      <Typography variant="h4" fontWeight="700" sx={{ mb: 3 }}>
        My Watchlist
      </Typography>

      <TableContainer component={Paper} sx={{ borderRadius: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Coin</TableCell>
              <TableCell align="right">Price</TableCell>
              <TableCell align="right">24h Change</TableCell>
              <TableCell align="right">Market Cap</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {watchlistItems.map((item) => (
              <TableRow key={item.id}>
                <TableCell>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Avatar src={item.image} alt={item.name} sx={{ width: 24, height: 24, mr: 1 }} />
                    <Box>
                      <Typography variant="body1" fontWeight="600">
                        {item.name}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        {item.symbol.toUpperCase()}
                      </Typography>
                    </Box>
                  </Box>
                </TableCell>
                <TableCell align="right">
                  <Typography variant="body1" fontWeight="600">
                    {formatCurrency(item.currentPrice)}
                  </Typography>
                </TableCell>
                <TableCell align="right">
                  <PriceChangeDisplay value={item.priceChangePercentage24h} />
                </TableCell>
                <TableCell align="right">
                  <Typography variant="body1">
                    {formatMarketCap(item.marketCap)}
                  </Typography>
                </TableCell>
                <TableCell align="right">
                  <IconButton
                    color="primary"
                    onClick={() => handleViewCoin(item.id)}
                    size="small"
                  >
                    <VisibilityIcon fontSize="small" />
                  </IconButton>
                  <IconButton
                    color="error"
                    onClick={() => handleRemoveFromWatchlist(item.id)}
                    size="small"
                  >
                    <DeleteIcon fontSize="small" />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default Watchlist;
