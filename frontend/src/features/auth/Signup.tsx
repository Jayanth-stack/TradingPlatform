import React from 'react';
import { Box, Typography, TextField, Button, Paper, Link } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

const Signup: React.FC = () => {
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
          Sign Up
        </Typography>
        
        <TextField
          label="Full Name"
          fullWidth
          margin="normal"
        />
        
        <TextField
          label="Email"
          fullWidth
          margin="normal"
          type="email"
        />
        
        <TextField
          label="Password"
          fullWidth
          margin="normal"
          type="password"
        />
        
        <Button 
          variant="contained" 
          fullWidth 
          sx={{ mt: 3, mb: 2 }}
        >
          Sign Up
        </Button>
        
        <Box sx={{ textAlign: 'center' }}>
          <Typography variant="body2">
            Already have an account?{' '}
            <Link component={RouterLink} to="/login">
              Login
            </Link>
          </Typography>
        </Box>
      </Paper>
    </Box>
  );
};

export default Signup; 