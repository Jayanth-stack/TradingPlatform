import React from 'react';
import { Box, Typography, Grid, Paper } from '@mui/material';
import { useParams } from 'react-router-dom';

const CoinDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();

  return (
    <Box sx={{ py: 2 }}>
      <Typography variant="h4" fontWeight="700" sx={{ mb: 2 }}>
        Coin Details: {id}
      </Typography>
      
      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3, borderRadius: 2 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>
              Price Chart
            </Typography>
            <Box sx={{ height: '300px', bgcolor: 'action.hover', borderRadius: 1 }}>
              {/* Chart will be displayed here */}
            </Box>
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3, borderRadius: 2 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>
              Market Info
            </Typography>
            <Typography variant="body1">
              Coin market information will be displayed here.
            </Typography>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default CoinDetail; 