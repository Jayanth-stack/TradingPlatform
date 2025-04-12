import React, { createContext, useState, useEffect, ReactNode, useContext } from 'react';

interface UserContextType {
  token: string | null;
  setToken: (token: string | null) => void;
  logout: () => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

interface UserProviderProps {
  children: ReactNode;
}

const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
  const [token, setTokenState] = useState<string | null>(() => {
    // Initialize from cookie
    const getCookie = (name: string) => {
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=\`);
      if (parts.length === 2) return parts.pop()?.split(';').shift() || null;
      return null;
    };
    return getCookie('jwt');
  });

  useEffect(() => {
    // Update cookie when token changes
    if (token) {
      document.cookie = `jwt=${token}; path=/; SameSite=Strict; Secure; httpOnly`;
    } else {
      document.cookie = 'jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/';
    }
  }, [token]);

  const setToken = (newToken: string | null) => {
    setTokenState(newToken);
  };

  const logout = () => {
    setTokenState(null);
  };

  return (
    <UserContext.Provider value={{ token, setToken, logout }}>
      {children}
    </UserContext.Provider>
  );
};

const useUser = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within a UserProvider');
  }
  return context;
};

export { UserProvider, useUser };
