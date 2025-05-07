import React, { useState } from 'react';
import { Box, ButtonGroup, Button } from '@mui/material';
import { styled } from '@mui/material/styles';
import Chart from 'react-apexcharts';
import { useTheme } from '../ui/ThemeProvider';

const ChartWrapper = styled(Box)(({ theme }) => ({
  width: '100%',
  marginTop: theme.spacing(2),
  '& .apexcharts-tooltip': {
    backgroundColor: theme.palette.background.paper,
    borderColor: theme.palette.divider,
    boxShadow: theme.shadows[3],
    color: theme.palette.text.primary,
  },
  '& .apexcharts-legend-text': {
    color: theme.palette.text.secondary,
  },
  '& .apexcharts-xaxis-label, .apexcharts-yaxis-label': {
    fill: theme.palette.text.secondary,
  },
}));

type TimeRange = '1D' | '1W' | '1M' | '3M' | '1Y' | 'ALL';

const PortfolioChart: React.FC = () => {
  const { mode } = useTheme();
  const [timeRange, setTimeRange] = useState<TimeRange>('1M');
  
  // Generate mock data for the chart
  const generateMockData = (range: TimeRange) => {
    let data: number[] = [];
    let categories: string[] = [];
    
    const addDays = (date: Date, days: number): Date => {
      const result = new Date(date);
      result.setDate(result.getDate() + days);
      return result;
    };
    
    const formatDate = (date: Date): string => {
      return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
    };
    
    const today = new Date();
    let baseValue = 10000;
    let volatility = 0.05;
    
    switch (range) {
      case '1D':
        for (let i = 0; i < 24; i++) {
          categories.push(`${i}:00`);
          const change = baseValue * (Math.random() * volatility * 2 - volatility);
          baseValue += change;
          data.push(baseValue);
        }
        break;
      case '1W':
        for (let i = 6; i >= 0; i--) {
          const date = addDays(today, -i);
          categories.push(formatDate(date));
          const change = baseValue * (Math.random() * volatility * 2 - volatility);
          baseValue += change;
          data.push(baseValue);
        }
        break;
      case '1M':
        for (let i = 30; i >= 0; i -= 2) {
          const date = addDays(today, -i);
          categories.push(formatDate(date));
          const change = baseValue * (Math.random() * volatility * 2 - volatility);
          baseValue += change;
          data.push(baseValue);
        }
        break;
      case '3M':
        for (let i = 90; i >= 0; i -= 7) {
          const date = addDays(today, -i);
          categories.push(formatDate(date));
          const change = baseValue * (Math.random() * volatility * 2 - volatility);
          baseValue += change;
          data.push(baseValue);
        }
        break;
      case '1Y':
        for (let i = 12; i >= 0; i--) {
          const date = new Date(today);
          date.setMonth(today.getMonth() - i);
          categories.push(date.toLocaleDateString('en-US', { month: 'short' }));
          const change = baseValue * (Math.random() * volatility * 2 - volatility);
          baseValue += change;
          data.push(baseValue);
        }
        break;
      case 'ALL':
        for (let i = 5; i >= 0; i--) {
          const date = new Date(today);
          date.setFullYear(today.getFullYear() - i);
          categories.push(date.getFullYear().toString());
          const change = baseValue * (Math.random() * volatility * 2 - volatility);
          baseValue += change;
          data.push(baseValue);
        }
        break;
    }
    
    return { data, categories };
  };
  
  const { data, categories } = generateMockData(timeRange);
  
  const series = [
    {
      name: 'Portfolio Value',
      data,
    },
  ];
  
  const options = {
    chart: {
      type: 'area' as const,
      toolbar: {
        show: false,
      },
      zoom: {
        enabled: false,
      },
      background: 'transparent',
    },
    colors: ['#2196f3'],
    fill: {
      type: 'gradient',
      gradient: {
        shadeIntensity: 1,
        opacityFrom: 0.7,
        opacityTo: 0.3,
        stops: [0, 90, 100],
      },
    },
    dataLabels: {
      enabled: false,
    },
    stroke: {
      curve: 'smooth' as const,
      width: 2,
    },
    grid: {
      borderColor: mode === 'dark' ? '#333' : '#e0e0e0',
      strokeDashArray: 3,
    },
    markers: {
      size: 4,
      colors: ['#2196f3'],
      strokeWidth: 0,
    },
    xaxis: {
      categories,
      labels: {
        style: {
          colors: mode === 'dark' ? '#aaa' : '#666',
        },
      },
      axisBorder: {
        show: false,
      },
    },
    yaxis: {
      labels: {
        formatter: (value: number) => {
          return `$${value.toFixed(2)}`;
        },
        style: {
          colors: mode === 'dark' ? '#aaa' : '#666',
        },
      },
    },
    tooltip: {
      x: {
        show: false,
      },
      y: {
        formatter: (value: number) => {
          return `$${value.toFixed(2)}`;
        },
      },
      theme: mode,
    },
    legend: {
      show: false,
    },
  };
  
  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 2 }}>
        <ButtonGroup size="small" aria-label="Time range">
          {(['1D', '1W', '1M', '3M', '1Y', 'ALL'] as TimeRange[]).map((range) => (
            <Button
              key={range}
              onClick={() => setTimeRange(range)}
              variant={timeRange === range ? 'contained' : 'outlined'}
            >
              {range}
            </Button>
          ))}
        </ButtonGroup>
      </Box>
      
      <ChartWrapper>
        <Chart
          options={options}
          series={series}
          type="area"
          height={280}
        />
      </ChartWrapper>
    </Box>
  );
};

export default PortfolioChart; 