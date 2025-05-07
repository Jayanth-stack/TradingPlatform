import React, { useEffect, useState } from 'react';
import { useUser } from './UserContext';
import AssetList from './AssetList';

const Dashboard: React.FC = () => {
  const [message, setMessage] = useState('Loading...');
  const { token, logout } = useUser();

  useEffect(() => {
    if (!token) {
      window.location.href = '/login';
      return;
    }

    const fetchDashboardData = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/dashboard', {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          // Token might be invalid or expired
          logout();
          window.location.href = '/login';
          return;
        }

        const data = await response.text();
        setMessage(data);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
        setMessage('Error fetching data. Please try again later.');
      }
    };

    fetchDashboardData();
  }, [token, logout]);

  const handleLogout = () => {
    logout();
  };

  return (
    <div>
      <h2>Dashboard</h2>
      <p>{message}</p>
      <AssetList />
      <button onClick={handleLogout}>Logout</button>
    </div>
  );
};

export default Dashboard;
