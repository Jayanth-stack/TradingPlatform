import React, { useState, useEffect } from 'react';
import { useUser } from './UserContext';

interface Asset {
  id: string;
  name: string;
  symbol: string;
  price: number;
  marketCap: number;
  change24h: number;
  high24h: number;
  low24h: number;
}

const AssetList: React.FC = () => {
  const [assets, setAssets] = useState<Asset[]>([]);
  const { token } = useUser();
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchAssets = async () => {
      try {
        const response = await fetch('/api/assets', {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          const message = `An error occurred: ${response.status}`;
          throw new Error(message);
        }

        const data = await response.json();
        setAssets(data);
      } catch (error: any) {
        setError(error.message);
        console.error('Error fetching assets:', error);
      }
    };

    fetchAssets();
  }, [token]);

  return (
    <div>
      <h2>Assets</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <ul>
        {assets.map((asset) => (
          <li key={asset.id}>
            {asset.name} ({asset.symbol}): ${asset.price}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default AssetList;
