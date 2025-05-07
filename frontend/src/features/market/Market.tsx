import React, { useState, useEffect } from 'react';
import {
  Grid,
  Typography,
  Box,
  TextField,
  InputAdornment,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
  SelectChangeEvent,
  Pagination,
  IconButton,
  Skeleton,
  Avatar,
  Tooltip
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import SearchIcon from '@mui/icons-material/Search';
import StarIcon from '@mui/icons-material/Star';
import StarBorderIcon from '@mui/icons-material/StarBorder';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import Card from '../../components/ui/Card';
import PriceChangeDisplay from '../../components/ui/PriceChangeDisplay';
import coinService from '../../services/coinService';
import watchlistService from '../../services/watchlistService';
import { Coin, WatchListItem } from '../../types';
import { formatCurrency } from '../../utils/formatters';

const Market: React.FC = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState<number>(1);
  const [search, setSearch] = useState<string>('');
  const [sortBy, setSortBy] = useState<string>('market_cap_desc');
  const [watchlist, setWatchlist] = useState<string[]>([]);
  const [filteredCoins, setFilteredCoins] = useState<Coin[]>([]);
  const [paginationModel, setPaginationModel] = useState({
    pageSize: 10,
    page: 0,
  });

  // Fetch coins
  const { data: coins, isLoading, refetch } = useQuery(
    ['coins', page],
    () => coinService.getCoinsWithPagination(page),
    {
      keepPreviousData: true,
    }
  );
  // Fetch watchlist
  useQuery(['watchlist'], () => watchlistService.getWatchlist(), {
    onSuccess: (data) => {
        // Extract coinIds from watchlist
        const coinIds = data.map((item: WatchListItem) => item.coinId);
        setWatchlist(coinIds);
      },
    }
  );

  // Filter and sort coins based on search term and sort option
  useEffect(() => {
    if (!coins) return;

    let filtered = [...coins];

    // Apply search filter
    if (search) {
      filtered = filtered.filter(
        (coin) =>
          coin.name.toLowerCase().includes(search.toLowerCase()) ||
          coin.symbol.toLowerCase().includes(search.toLowerCase())
      );
    }

    // Apply sorting
    switch (sortBy) {
      case 'price_asc':
        filtered.sort((a, b) => a.currentPrice - b.currentPrice);
        break;
      case 'price_desc':
        filtered.sort((a, b) => b.currentPrice - a.currentPrice);
        break;
      case 'name_asc':
        filtered.sort((a, b) => a.name.localeCompare(b.name));
        break;
      case 'name_desc':
        filtered.sort((a, b) => b.name.localeCompare(a.name));
        break;
      case 'change_asc':
        filtered.sort((a, b) => a.priceChangePercentage24h - b.priceChangePercentage24h);
        break;
      case 'change_desc':
        filtered.sort((a, b) => b.priceChangePercentage24h - a.priceChangePercentage24h);
        break;
      case 'market_cap_asc':
        filtered.sort((a, b) => a.marketCap - b.marketCap);
        break;
      case 'market_cap_desc':
      default:
        filtered.sort((a, b) => b.marketCap - a.marketCap);
        break;
    }

    setFilteredCoins(filtered);
  }, [coins, search, sortBy]);

  // Handle page change
  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
  };

  // Handle sort change
  const handleSortChange = (event: SelectChangeEvent) => {
    setSortBy(event.target.value);
  };

  // Handle search input change
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(event.target.value);
  };

  // Navigate to coin detail page
  const handleCoinClick = (coinId: string) => {
    navigate(`/coin/${coinId}`);
  };

  // Toggle watchlist status
  const handleToggleWatchlist = async (event: React.MouseEvent, coinId: string) => {
    event.stopPropagation();
    try {
      if (watchlist.includes(coinId)) {
        await watchlistService.removeFromWatchlist(coinId);
        setWatchlist(watchlist.filter((id) => id !== coinId));
      } else {
        await watchlistService.addToWatchlist(coinId);
        setWatchlist([...watchlist, coinId]);
      }
    } catch (error) {
      console.error('Error toggling watchlist status:', error);
    }
  };

  // Define columns for the data grid
  const columns: GridColDef[] = [
    {
      field: 'favorite',
      headerName: '',
      width: 50,
      renderCell: (params) => (
        <IconButton onClick={(e) => handleToggleWatchlist(e, params.row.id)}>
          {watchlist.includes(params.row.id) ? (
            <StarIcon sx={{ color: 'warning.main' }} />
          ) : (
            <StarBorderIcon />
          )}
        </IconButton>
      ),
      sortable: false,
    },
    {
      field: 'coin',
      headerName: 'Coin',
      flex: 1,
      minWidth: 180,
      renderCell: (params) => (
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <Avatar src={params.row.image} sx={{ width: 24, height: 24, mr: 1 }} />
          <Box>
            <Typography variant="body2" fontWeight={600}>
              {params.row.name}
            </Typography>
            <Typography variant="caption" color="text.secondary">
              {params.row.symbol.toUpperCase()}
            </Typography>
          </Box>
        </Box>
      ),
      sortable: false,
    },
    {
      field: 'currentPrice',
      headerName: 'Price',
      flex: 1,
      minWidth: 120,
      renderCell: (params) => (
        <Typography variant="body2">{formatCurrency(params.value)}</Typography>
      ),
    },
    {
      field: 'priceChangePercentage24h',
      headerName: '24h Change',
      flex: 1,
      minWidth: 130,
      renderCell: (params) => <PriceChangeDisplay value={params.value} />,
    },
    {
      field: 'marketCap',
      headerName: 'Market Cap',
      flex: 1,
      minWidth: 150,
      renderCell: (params) => (
        <Typography variant="body2">{formatCurrency(params.value)}</Typography>
      ),
    },
    {
      field: 'totalVolume',
      headerName: 'Volume (24h)',
      flex: 1,
      minWidth: 150,
      renderCell: (params) => (
        <Typography variant="body2">{formatCurrency(params.value)}</Typography>
      ),
    },
  ];

  return (
    <Box sx={{ py: 2 }}>
      <Typography variant="h4" fontWeight="700" sx={{ mb: 1 }}>
        Cryptocurrency Market
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
        View real-time prices and market data for thousands of cryptocurrencies
      </Typography>

      <Card>
        <Box
          sx={{
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            alignItems: { xs: 'stretch', md: 'center' },
            justifyContent: 'space-between',
            mb: 3,
            gap: 2,
          }}
        >
          <TextField
            placeholder="Search coins..."
            variant="outlined"
            size="small"
            fullWidth
            value={search}
            onChange={handleSearchChange}
            sx={{ maxWidth: { md: 300 } }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
          />

          <FormControl size="small" sx={{ minWidth: 200 }}>
            <InputLabel id="sort-by-label">Sort By</InputLabel>
            <Select
              labelId="sort-by-label"
              id="sort-by"
              value={sortBy}
              label="Sort By"
              onChange={handleSortChange}
            >
              <MenuItem value="market_cap_desc">Market Cap (High to Low)</MenuItem>
              <MenuItem value="market_cap_asc">Market Cap (Low to High)</MenuItem>
              <MenuItem value="price_desc">Price (High to Low)</MenuItem>
              <MenuItem value="price_asc">Price (Low to High)</MenuItem>
              <MenuItem value="change_desc">24h Change (High to Low)</MenuItem>
              <MenuItem value="change_asc">24h Change (Low to High)</MenuItem>
              <MenuItem value="name_asc">Name (A to Z)</MenuItem>
              <MenuItem value="name_desc">Name (Z to A)</MenuItem>
            </Select>
          </FormControl>
        </Box>

        {isLoading ? (
          <Skeleton variant="rectangular" height={400} />
        ) : (
          <>
            <Box sx={{ height: 600, width: '100%' }}>
              <DataGrid
                rows={filteredCoins}
                columns={columns}
                initialState={{
                  pagination: {
                    paginationModel: { pageSize: 10, page: 0 },
                  },
                }}
                pageSizeOptions={[10]}
                rowHeight={60}
                disableRowSelectionOnClick
                disableColumnMenu
                onRowClick={(params) => handleCoinClick(params.row.id)}
                getRowClassName={() => 'cursor-pointer'}
                sx={{
                  '& .MuiDataGrid-cell:focus': {
                    outline: 'none',
                  },
                  '& .cursor-pointer': {
                    cursor: 'pointer',
                  },
                  border: 'none',
                }}
              />
            </Box>

            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
              <Pagination
                count={10} // Replace with actual total pages
                page={page}
                onChange={handlePageChange}
                color="primary"
              />
            </Box>
          </>
        )}
      </Card>
    </Box>
  );
};

export default Market; 