import React from 'react';
import { Box, Typography, TextField, Button, Paper } from '@mui/material';

const TwoFactorAuth: React.FC = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '100vh',
        p: 2,
      }}
    >
      <Paper 
        sx={{ 
          p: 4, 
          maxWidth: 400, 
          width: '100%', 
          borderRadius: 2 
        }}
      >
        <Typography variant="h4" component="h1" fontWeight="700" sx={{ mb: 3 }}>
          Two-Factor Authentication
        </Typography>
        
        <Typography variant="body1" sx={{ mb: 3 }}>
          Please enter the verification code sent to your email.
        </Typography>
        
        <TextField
          label="Verification Code"
          fullWidth
          margin="normal"
        />
        
        <Button 
          variant="contained" 
          fullWidth 
          sx={{ mt: 3 }}
        >
          Verify
        </Button>
      </Paper>
    </Box>
  );
};

export default TwoFactorAuth; 