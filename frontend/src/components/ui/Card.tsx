import React, { ReactNode } from 'react';
import { Card as MuiCard, CardProps as MuiCardProps, CardContent, Typography, Box } from '@mui/material';
import { styled } from '@mui/material/styles';

interface CardProps extends MuiCardProps {
  title?: string;
  subtitle?: string;
  headerAction?: ReactNode;
  footer?: ReactNode;
  noPadding?: boolean;
}

const StyledCard = styled(MuiCard)(({ theme }) => ({
  borderRadius: '12px',
  overflow: 'hidden',
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
  boxShadow: theme.palette.mode === 'light' 
    ? '0px 2px 4px rgba(0, 0, 0, 0.05)' 
    : '0px 2px 4px rgba(0, 0, 0, 0.5)',
}));

const StyledCardContent = styled(CardContent)<{ noPadding?: boolean }>(({ theme, noPadding }) => ({
  padding: noPadding ? 0 : theme.spacing(3),
  flexGrow: 1,
  display: 'flex',
  flexDirection: 'column',
  '&:last-child': {
    paddingBottom: noPadding ? 0 : theme.spacing(3),
  },
}));

const CardHeader = styled(Box)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  marginBottom: theme.spacing(2),
}));

const CardFooter = styled(Box)(({ theme }) => ({
  marginTop: 'auto',
  paddingTop: theme.spacing(2),
}));

const Card: React.FC<CardProps> = ({ 
  title, 
  subtitle, 
  headerAction, 
  footer, 
  noPadding, 
  children, 
  ...rest 
}) => {
  return (
    <StyledCard {...rest}>
      <StyledCardContent noPadding={noPadding}>
        {(title || headerAction) && (
          <CardHeader>
            <Box>
              {title && <Typography variant="h6" fontWeight="600">{title}</Typography>}
              {subtitle && <Typography variant="body2" color="text.secondary">{subtitle}</Typography>}
            </Box>
            {headerAction && <Box>{headerAction}</Box>}
          </CardHeader>
        )}
        
        {children}
        
        {footer && <CardFooter>{footer}</CardFooter>}
      </StyledCardContent>
    </StyledCard>
  );
};

export default Card; 