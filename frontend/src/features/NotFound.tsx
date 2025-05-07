import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { Link } from 'react-router-dom';

const NotFound: React.FC = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        height: '100vh',
        textAlign: 'center',
        p: 2,
      }}
    >
      <Typography variant="h1" component="h1" sx={{ mb: 2, fontWeight: 700 }}>
        404
      </Typography>
      <Typography variant="h5" component="h2" sx={{ mb: 4 }}>
        Page Not Found
      </Typography>
      <Button
        component={Link}
        to="/"
        variant="contained"
        color="primary"
        size="large"
      >
        Go to Home
      </Button>
    </Box>
  );
};

export default NotFound;
