import React from 'react';
import { Box, Typography } from '@mui/material';

const Trading: React.FC = () => {
  return (
    <Box sx={{ py: 2 }}>
      <Typography variant="h4" fontWeight="700" sx={{ mb: 2 }}>
        Trading
      </Typography>
      <Typography variant="body1">
        Trading interface will be displayed here.
      </Typography>
    </Box>
  );
};

export default Trading; 