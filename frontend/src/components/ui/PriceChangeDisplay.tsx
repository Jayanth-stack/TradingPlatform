import React from 'react';
import { Box, Typography } from '@mui/material';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import TrendingDownIcon from '@mui/icons-material/TrendingDown';

interface PriceChangeDisplayProps {
  value: number;
  showIcon?: boolean;
}

const PriceChangeDisplay: React.FC<PriceChangeDisplayProps> = ({
  value,
  showIcon = true,
}) => {
  const isPositive = value >= 0;
  const formattedValue = Math.abs(value).toFixed(2);

  return (
    <Box
      sx={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        color: isPositive ? 'success.main' : 'error.main',
      }}
    >
      {showIcon && (
        <>
          {isPositive ? (
            <TrendingUpIcon fontSize="small" sx={{ mr: 0.5 }} />
          ) : (
            <TrendingDownIcon fontSize="small" sx={{ mr: 0.5 }} />
          )}
        </>
      )}
      <Typography variant="body2" fontWeight="600">
        {isPositive ? '+' : '-'}
        {formattedValue}%
      </Typography>
    </Box>
  );
};

export default PriceChangeDisplay; 