import React, { useState } from 'react';
import { Box, Container, Typography, TextField, Link as MuiLink, InputAdornment, IconButton, Paper } from '@mui/material';
import { styled } from '@mui/material/styles';
import { Link, useNavigate } from 'react-router-dom';
import { useForm, Controller } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import Button from '../../components/ui/Button';
import useAuthStore from '../../store/authStore';

// Validation schema
const loginSchema = z.object({
  email: z.string().email('Invalid email format'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
});

type LoginFormValues = z.infer<typeof loginSchema>;

const StyledPaper = styled(Paper)(({ theme }) => ({
  borderRadius: 16,
  padding: theme.spacing(4),
  [theme.breakpoints.up('sm')]: {
    padding: theme.spacing(6),
  },
  width: '100%',
  maxWidth: 480,
  boxShadow: theme.palette.mode === 'dark' 
    ? '0px 8px 16px rgba(0, 0, 0, 0.3)' 
    : '0px 8px 16px rgba(0, 0, 0, 0.1)',
}));

const Login: React.FC = () => {
  const navigate = useNavigate();
  const { login, error, twoFactorRequired, sessionId, isLoading } = useAuthStore();
  const [showPassword, setShowPassword] = useState(false);

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: '',
      password: '',
    },
  });

  const onSubmit = async (data: LoginFormValues) => {
    await login(data);
    
    // If 2FA is required, redirect to 2FA page
    if (twoFactorRequired && sessionId) {
      navigate('/two-factor-auth');
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        py: 4,
        background: (theme) =>
          theme.palette.mode === 'dark'
            ? 'linear-gradient(120deg, #2c3e50 0%, #1a1a2e 100%)'
            : 'linear-gradient(120deg, #e0f7fa 0%, #bbdefb 100%)',
      }}
    >
      <Container maxWidth="sm" sx={{ px: 2 }}>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            mb: 4,
          }}
        >
          <Typography
            variant="h4"
            component="h1"
            fontWeight="700"
            sx={{ mb: 1, color: 'primary.main' }}
          >
            Trading Platform
          </Typography>
          <Typography variant="body1" sx={{ mb: 4, textAlign: 'center' }}>
            Enter your credentials to access your account
          </Typography>
        </Box>

        <StyledPaper>
          {error && (
            <Typography color="error" variant="body2" sx={{ mb: 2 }}>
              {error}
            </Typography>
          )}

          <form onSubmit={handleSubmit(onSubmit)}>
            <Controller
              name="email"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  label="Email"
                  variant="outlined"
                  fullWidth
                  margin="normal"
                  error={!!errors.email}
                  helperText={errors.email?.message}
                  disabled={isLoading}
                />
              )}
            />

            <Controller
              name="password"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  label="Password"
                  type={showPassword ? 'text' : 'password'}
                  variant="outlined"
                  fullWidth
                  margin="normal"
                  error={!!errors.password}
                  helperText={errors.password?.message}
                  disabled={isLoading}
                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          aria-label="toggle password visibility"
                          onClick={() => setShowPassword(!showPassword)}
                          edge="end"
                        >
                          {showPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                />
              )}
            />

            <Button
              type="submit"
              variant="contained"
              fullWidth
              size="large"
              sx={{ mt: 3 }}
              isLoading={isLoading}
            >
              Login
            </Button>

            <Box sx={{ mt: 3, textAlign: 'center' }}>
              <Typography variant="body2">
                Don't have an account?{' '}
                <MuiLink component={Link} to="/signup" fontWeight="600">
                  Sign up
                </MuiLink>
              </Typography>
            </Box>
          </form>
        </StyledPaper>
      </Container>
    </Box>
  );
};

export default Login;