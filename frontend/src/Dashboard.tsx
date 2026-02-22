import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from './UserContext';
import AssetList from './AssetList';

const Dashboard: React.FC = () => {
  const [message, setMessage] = useState('Loading...');
  const { token, logout } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) {
      navigate('/login', { replace: true });
      return;
    }

    const fetchDashboardData = async () => {
      try {
        const response = await fetch('http://localhost:8080/api', {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          logout();
          navigate('/login', { replace: true });
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
  }, [token, logout, navigate]);

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
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
