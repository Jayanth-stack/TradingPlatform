import React from 'react';
import { Box, Typography } from '@mui/material';

const Profile: React.FC = () => {
  return (
    <Box sx={{ py: 2 }}>
      <Typography variant="h4" fontWeight="700" sx={{ mb: 2 }}>
        Profile
      </Typography>
      <Typography variant="body1">
        Your profile information will be displayed here.
      </Typography>
    </Box>
  );
};

export default Profile;
