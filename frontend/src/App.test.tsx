import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';
import { UserProvider } from './UserContext';

test('redirects unauthenticated users to login', () => {
  render(
    <UserProvider>
      <App />
    </UserProvider>
  );
  const heading = screen.getByText(/login/i);
  expect(heading).toBeInTheDocument();
});
