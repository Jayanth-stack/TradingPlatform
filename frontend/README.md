# Trading Platform Frontend

A modern, responsive cryptocurrency trading platform built with React, TypeScript, and Material UI.

## Features

- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices
- **Real-time Data**: View cryptocurrency prices and market data in real-time
- **Interactive Charts**: Visualize price movements with interactive charts
- **Portfolio Management**: Track your cryptocurrency portfolio
- **Order Execution**: Buy and sell cryptocurrencies
- **Watchlist**: Create and manage your personalized watchlist
- **Authentication**: Secure login with two-factor authentication
- **Dark/Light Mode**: Choose between light and dark interface themes

## Technology Stack

- **React 18**: Modern UI library for building user interfaces
- **TypeScript**: Type-safe JavaScript for better developer experience
- **Material UI**: Modern UI component library
- **React Router**: Client-side routing
- **React Query**: Data fetching and caching
- **Zustand**: State management
- **ApexCharts**: Interactive data visualization
- **React Hook Form**: Form handling with validation
- **Vite**: Fast build tool and development server

## Getting Started

### Prerequisites

- Node.js (v14.x or later)
- npm or yarn

### Installation

1. Clone the repository
   ```bash
   git clone <repository-url>
   cd trading-platform
   ```

2. Install dependencies
   ```bash
   cd frontend
   npm install
   # or
   yarn install
   ```

3. Start the development server
   ```bash
   npm run dev
   # or
   yarn dev
   ```

4. Open your browser and navigate to `http://localhost:5173`

## Available Scripts

- `npm run dev` - Start the development server
- `npm run build` - Build the app for production
- `npm run preview` - Preview the production build locally

## Project Structure

```
frontend/
├── public/              # Static assets
├── src/
│   ├── assets/          # Images, fonts, etc.
│   ├── components/      # Reusable components
│   │   ├── charts/      # Chart components
│   │   ├── forms/       # Form components
│   │   ├── layout/      # Layout components
│   │   └── ui/          # UI components
│   ├── features/        # Feature-based modules
│   │   ├── auth/        # Authentication features
│   │   ├── dashboard/   # Dashboard features
│   │   ├── market/      # Market data features
│   │   ├── orders/      # Order management features
│   │   ├── profile/     # User profile features
│   │   ├── wallet/      # Wallet features
│   │   └── watchlist/   # Watchlist features
│   ├── hooks/           # Custom React hooks
│   ├── lib/             # Library configurations
│   ├── services/        # API services
│   ├── store/           # State management
│   ├── types/           # TypeScript type definitions
│   ├── utils/           # Utility functions
│   ├── App.tsx          # Main App component
│   └── index.tsx        # Entry point
├── package.json         # Dependencies and scripts
├── tsconfig.json        # TypeScript configuration
└── vite.config.ts       # Vite configuration
```

## Backend Integration

This frontend is designed to work with the Trading Platform backend API. The API endpoints are configured in the services directory, and proxied through Vite's development server.
