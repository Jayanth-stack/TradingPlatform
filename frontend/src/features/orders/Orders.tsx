import React from 'react';
import {
  Box,
  Typography,
  Tabs,
  Tab,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip
} from '@mui/material';

const Orders: React.FC = () => {
  const [tabValue, setTabValue] = React.useState(0);

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  // Mock orders data
  const orders = [
    {
      id: '1',
      type: 'BUY',
      coin: 'Bitcoin',
      amount: '0.125 BTC',
      price: '$45,670.25',
      total: '$5,708.78',
      date: 'May 12, 2025',
      status: 'COMPLETED'
    },
    {
      id: '2',
      type: 'SELL',
      coin: 'Ethereum',
      amount: '2.5 ETH',
      price: '$1,870.32',
      total: '$4,675.80',
      date: 'May 10, 2025',
      status: 'COMPLETED'
    },
    {
      id: '3',
      type: 'BUY',
      coin: 'Cardano',
      amount: '1,000 ADA',
      price: '$0.54',
      total: '$540.00',
      date: 'May 8, 2025',
      status: 'PENDING'
    },
    {
      id: '4',
      type: 'BUY',
      coin: 'Solana',
      amount: '10 SOL',
      price: '$136.25',
      total: '$1,362.50',
      date: 'May 5, 2025',
      status: 'CANCELLED'
    }
  ];

  return (
    <Box sx={{ py: 2 }}>
      <Typography variant="h4" fontWeight="700" sx={{ mb: 3 }}>
        My Orders
      </Typography>

      <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 3 }}>
        <Tabs value={tabValue} onChange={handleTabChange}>
          <Tab label="All Orders" />
          <Tab label="Open Orders" />
          <Tab label="Order History" />
        </Tabs>
      </Box>

      <TableContainer component={Paper} sx={{ borderRadius: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Type</TableCell>
              <TableCell>Coin</TableCell>
              <TableCell>Amount</TableCell>
              <TableCell>Price</TableCell>
              <TableCell>Total</TableCell>
              <TableCell>Date</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map((order) => (
              <TableRow key={order.id}>
                <TableCell>
                  <Typography
                    color={order.type === 'BUY' ? 'success.main' : 'error.main'}
                    fontWeight="600"
                  >
                    {order.type}
                  </Typography>
                </TableCell>
                <TableCell>{order.coin}</TableCell>
                <TableCell>{order.amount}</TableCell>
                <TableCell>{order.price}</TableCell>
                <TableCell>{order.total}</TableCell>
                <TableCell>{order.date}</TableCell>
                <TableCell>
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
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default Orders; 