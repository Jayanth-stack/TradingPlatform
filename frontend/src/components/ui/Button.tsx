import React from 'react';
import { Button as MuiButton, ButtonProps as MuiButtonProps, CircularProgress } from '@mui/material';
import { styled } from '@mui/material/styles';

interface ButtonProps extends MuiButtonProps {
  isLoading?: boolean;
}

const StyledButton = styled(MuiButton)(({ theme }) => ({
  fontWeight: 600,
  borderRadius: '8px',
  textTransform: 'none',
  boxShadow: 'none',
  padding: '10px 24px',
  position: 'relative',
  '&:hover': {
    boxShadow: 'none',
  },
}));

const Button: React.FC<ButtonProps> = ({ 
  isLoading, 
  disabled, 
  children, 
  startIcon, 
  ...rest 
}) => {
  return (
    <StyledButton
      disabled={isLoading || disabled}
      startIcon={isLoading ? null : startIcon}
      {...rest}
    >
      {isLoading && (
        <CircularProgress
          size={24}
          sx={{
            position: 'absolute',
            left: '50%',
            marginLeft: '-12px',
            color: 'inherit',
          }}
        />
      )}
      {children}
    </StyledButton>
  );
};

export default Button; 