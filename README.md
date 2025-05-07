# 📈 Trading Platform

A modern, secure trading platform for cryptocurrency and stock trading with real-time market data, two-factor authentication, and a seamless user experience.

## 🚀 Features

- **💰 Asset Management**: Buy, sell, and track multiple cryptocurrencies and stocks
- **📊 Real-time Data**: Live market data and interactive charts
- **🔒 Secure Authentication**: JWT-based auth with two-factor authentication
- **💼 Portfolio Management**: Track your investments and performance
- **📱 Responsive Design**: Works on desktop and mobile devices
- **💸 Payment Integration**: Secure payment processing
- **👀 Watchlist**: Monitor assets without purchasing

## 🛠️ Tech Stack

### Frontend
- **⚛️ React**: Modern UI library for building interactive interfaces
- **🔄 Redux**: State management
- **📊 Chart.js**: Interactive financial charts
- **🎨 Material UI**: Sleek, responsive components
- **🌐 Axios**: API request handling

### Backend
- **☕ Java Spring Boot**: Robust backend framework
- **🔐 Spring Security**: Authentication and authorization
- **🗃️ JPA/Hibernate**: Database ORM
- **💾 MySQL/PostgreSQL**: Relational database
- **🔑 JWT**: Token-based authentication
- **📨 Email Service**: Verification and notification system
- **📱 OTP**: Two-factor authentication

## 🏗️ Architecture

The application follows a microservices architecture with:

```
📦 Trading Platform
┣ 📂 frontend
┃ ┣ 📂 src
┃ ┃ ┣ 📂 components
┃ ┃ ┣ 📂 pages
┃ ┃ ┣ 📂 redux
┃ ┃ ┣ 📂 services
┃ ┃ ┗ 📂 utils
┃ ┗ 📂 public
┗ 📂 backend
  ┣ 📂 src/main/java
  ┃ ┣ 📂 controller
  ┃ ┣ 📂 service
  ┃ ┣ 📂 repository
  ┃ ┣ 📂 model
  ┃ ┣ 📂 config
  ┃ ┗ 📂 utils
  ┗ 📂 src/main/resources
```

## 🌐 API Overview

### Authentication Endpoints
- **POST /auth/signup**: Register a new user
- **POST /auth/signin**: Login with credentials
- **POST /auth/twofactor/otp/{otp}**: Verify two-factor authentication

### Asset Endpoints
- **GET /assets**: List user's assets
- **GET /assets/{coinId}**: Get specific asset details
- **POST /assets/buy**: Purchase an asset
- **POST /assets/sell**: Sell an asset

### Wallet Endpoints
- **GET /wallet**: Get wallet balance
- **POST /wallet/deposit**: Add funds to wallet
- **POST /wallet/withdraw**: Withdraw funds

### Watchlist Endpoints
- **GET /watchlist**: Get user's watchlist
- **POST /watchlist/add**: Add asset to watchlist
- **DELETE /watchlist/remove/{coinId}**: Remove from watchlist

## 🔒 Security Features

1. **JWT Authentication**: Secure token-based authentication
2. **Two-Factor Authentication**: Extra security layer with OTP
3. **HTTPS**: All communications encrypted
4. **Password Encryption**: Secure password storage
5. **Session Management**: Secure cookie handling

## 🚀 Setup & Installation

### Prerequisites
- Node.js (v14+)
- Java JDK 11+
- Maven
- MySQL/PostgreSQL

### Backend Setup
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

## 📄 API Authentication Flow

1. **User Registration**:
   - Client sends credentials to `/auth/signup`
   - Server validates, creates user, returns JWT token
   - JWT stored as HttpOnly cookie

2. **User Login**:
   - Client sends credentials to `/auth/signin`
   - If 2FA enabled:
     - Server sends OTP to user's email
     - Client prompts user for OTP
     - Client verifies OTP at `/auth/twofactor/otp/{otp}`
   - Server returns JWT token after successful authentication

3. **Authenticated Requests**:
   - All subsequent requests include JWT cookie
   - Server validates token for protected endpoints

## 📚 Documentation

API documentation is available at `/swagger-ui.html` when running the backend server.

## 🔄 Workflow

1. User registers/logs in
2. User deposits funds to wallet
3. User browses market data
4. User purchases assets or adds to watchlist
5. User monitors portfolio performance
6. User sells assets when desired

## 📱 Mobile Responsiveness

The platform is fully responsive and optimized for:
- 📱 Mobile phones
- 📱 Tablets
- 💻 Desktops
- 🖥️ Large displays

## 🚧 Future Enhancements

- [ ] Advanced charting tools
- [ ] Social trading features
- [ ] AI-powered trade suggestions
- [ ] Mobile app versions
- [ ] Additional payment methods

## 📜 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

⭐ **Created with passion by [Your Name]** ⭐ 