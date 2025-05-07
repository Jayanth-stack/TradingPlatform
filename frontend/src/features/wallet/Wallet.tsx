import React from 'react';
import { Box, Typography, Grid, Paper, Button, List, ListItem, ListItemText, Divider } from '@mui/material';

const Wallet: React.FC = () => {
  return (
    <Box sx={{ py: 2 }}>
      <Typography variant="h4" fontWeight="700" sx={{ mb: 2 }}>
        My Wallet
      </Typography>
      
      <Grid container spacing={3}>
        <Grid item xs={12} md={7}>
          <Paper sx={{ p: 3, borderRadius: 2 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
              <Typography variant="h6">
                Portfolio Balance
              </Typography>
              <Button variant="contained" color="primary">
                Deposit
              </Button>
            </Box>
            
            <Typography variant="h3" fontWeight="700" sx={{ mb: 3 }}>
              $10,256.78
            </Typography>
            
            <Typography variant="h6" sx={{ mb: 2 }}>
              Assets
            </Typography>
            
            <List>
              <ListItem sx={{ px: 0 }}>
                <ListItemText 
                  primary="Bitcoin (BTC)"
                  secondary="0.0512 BTC"
                />
                <Typography variant="body1" fontWeight="600">
                  $2,458.32
                </Typography>
              </ListItem>
              
              <Divider />
              
              <ListItem sx={{ px: 0 }}>
                <ListItemText 
                  primary="Ethereum (ETH)"
                  secondary="1.235 ETH"
                />
                <Typography variant="body1" fontWeight="600">
                  $2,145.67
                </Typography>
              </ListItem>
              
              <Divider />
              
              <ListItem sx={{ px: 0 }}>
                <ListItemText 
                  primary="USD Coin (USDC)"
                  secondary="5,652.79 USDC"
                />
                <Typography variant="body1" fontWeight="600">
                  $5,652.79
                </Typography>
              </ListItem>
            </List>
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={5}>
          <Paper sx={{ p: 3, borderRadius: 2 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>
              Transaction History
            </Typography>
            
            <List>
              <ListItem sx={{ px: 0 }}>
                <ListItemText 
                  primary="Deposit"
                  secondary="May 12, 2025"
                />
                <Typography variant="body1" fontWeight="600" color="success.main">
                  +$1,000.00
                </Typography>
              </ListItem>
              
              <Divider />
              
              <ListItem sx={{ px: 0 }}>
                <ListItemText 
                  primary="Buy Bitcoin"
                  secondary="May 10, 2025"
                />
                <Typography variant="body1" fontWeight="600" color="error.main">
                  -$500.00
                </Typography>
              </ListItem>
              
              <Divider />
              
              <ListItem sx={{ px: 0 }}>
                <ListItemText 
                  primary="Sell Ethereum"
                  secondary="May 8, 2025"
                />
                <Typography variant="body1" fontWeight="600" color="success.main">
                  +$325.45
                </Typography>
              </ListItem>
            </List>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Wallet; 