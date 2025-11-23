# Trading Platform - Deployment Guide

## Pre-Deployment Checklist

Before deploying to any environment, ensure:

- [ ] All critical issues from `IMPROVEMENT_PLAN.md` are fixed
- [ ] JWT token handling is working (test with Postman)
- [ ] CORS is properly configured
- [ ] 2FA flow is complete
- [ ] All required backend endpoints exist
- [ ] Environment variables are externalized
- [ ] Secrets are not hardcoded
- [ ] Tests pass locally
- [ ] Docker images build successfully
- [ ] Sensitive data is removed from version control

---

## Phase 1: Local Development (Current Stage)

### 1. Start with Docker Compose

```bash
# From project root
docker-compose up -d

# Verify services are running
docker-compose ps

# View logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql
```

### 2. Test the application

**Backend Health Check:**
```bash
curl http://localhost:8080/actuator/health
# Should return: {"status":"UP"}
```

**Frontend Access:**
```
Open browser: http://localhost
Should see login page
```

**Test Auth Flow:**
1. Go to /signup
2. Create account (email, password, name)
3. Check database for new user
4. Go to /login
5. Login with credentials
6. Should redirect to /dashboard

### 3. Test API endpoints with Postman

**Import into Postman:**
```
POST http://localhost:8080/auth/signup
Body:
{
  "fullName": "Test User",
  "email": "test@example.com",
  "password": "TestPassword123"
}

Response should include:
{
  "status": true,
  "message": "Signup successful",
  "jwt": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Store JWT and test authenticated endpoint:**
```
GET http://localhost:8080/api/users/profile
Headers:
Authorization: Bearer <JWT_FROM_RESPONSE>

Should return user profile
```

### 4. Clean up before committing

```bash
# Stop containers
docker-compose down

# Remove any temporary files
git status

# Make sure no secrets in code
grep -r "password=" --include="*.java" --include="*.js" --include="*.ts"
grep -r "root" docker-compose.yml  # Should move to .env
```

---

## Phase 2: Environment Configuration

### Create Environment Files

**File: `backend/src/main/resources/application.properties`** (Default/Development)
```properties
# Server
server.port=8080
server.servlet.context-path=/

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/tradingplatform
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=${JWT_SECRET:your-secret-key-change-in-production}
jwt.expiration=${JWT_EXPIRATION:3600000}

# CORS
cors.allowed.origins=http://localhost,http://localhost:3000,http://localhost:5173

# Email (update with real credentials)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your-app-password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Logging
logging.level.root=INFO
logging.level.com.jayanth.tradingplatform=DEBUG
```

**File: `backend/src/main/resources/application-dev.properties`** (Development overrides)
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/tradingplatform_dev
spring.datasource.username=dev_user
spring.datasource.password=dev_password
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
logging.level.com.jayanth.tradingplatform=DEBUG
```

**File: `backend/src/main/resources/application-prod.properties`** (Production)
```properties
server.port=8080
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.root=WARN
logging.level.com.jayanth.tradingplatform=INFO

# Production security
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.same-site=strict
```

**File: `docker-compose.yml`** (Update with environment file)
```yaml
services:
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "80:80"
    depends_on:
      - backend
    environment:
      - VITE_API_URL=http://localhost:8080
    env_file:
      - .env.frontend  # Create this

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    platform: linux/amd64
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/tradingplatform
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=root
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      - SPRING_JPA_SHOW_SQL=true
      - SERVER_PORT=8080
    env_file:
      - .env.backend  # Create this

  mysql:
    image: mysql:8.0
    platform: linux/amd64
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=tradingplatform
    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  mysql-data:
```

**File: `.env.example`** (Template - commit this, not actual .env)
```
# Backend
JWT_SECRET=your-super-secret-key-change-in-production
JWT_EXPIRATION=3600000
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-specific-password

# Frontend
VITE_API_URL=http://localhost:8080
VITE_APP_NAME=Trading Platform
```

**File: `.gitignore`** (Update to exclude .env files)
```
# Environment files
.env
.env.local
.env.*.local
.env.backend
.env.frontend
.env.production

# IDE
.idea/
.vscode/
*.swp
*.swo

# Build
target/
dist/
node_modules/
build/

# OS
.DS_Store
Thumbs.db

# Logs
*.log
logs/
```

---

## Phase 3: GitHub Secrets Configuration

### Set up GitHub Secrets

Go to: **Settings → Secrets and variables → Actions**

Add these secrets:
```
DOCKER_USERNAME     = your-github-username
DOCKER_PASSWORD     = your-github-token (Personal Access Token)

DATABASE_URL        = jdbc:mysql://prod-db-host:3306/tradingplatform
DATABASE_USERNAME   = prod_user
DATABASE_PASSWORD   = your-secure-password

JWT_SECRET          = your-production-jwt-secret
MAIL_USERNAME       = your-production-email
MAIL_PASSWORD       = your-production-app-password

RENDER_API_TOKEN    = (if using Render)
RAILWAY_API_TOKEN   = (if using Railway)

# AWS credentials (if using AWS)
AWS_ACCESS_KEY_ID   = your-access-key
AWS_SECRET_ACCESS_KEY = your-secret-key
AWS_REGION          = us-east-1
```

---

## Phase 4: Staging Deployment

### Option A: Render.com (Easiest)

1. **Create render.yaml file**

**File: `render.yaml`**
```yaml
services:
  - type: web
    name: trading-platform-backend
    runtime: docker
    dockerfilePath: ./backend/Dockerfile
    dockerContext: ./backend
    port: 8080
    healthCheckPath: /actuator/health
    autoDeploy: true
    envVars:
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: DATABASE_URL
        fromDatabase:
          name: trading-platform-db
          property: connectionString
    secrets:
      - key: JWT_SECRET
      - key: MAIL_USERNAME
      - key: MAIL_PASSWORD

  - type: web
    name: trading-platform-frontend
    runtime: docker
    dockerfilePath: ./frontend/Dockerfile
    dockerContext: ./frontend
    port: 3000
    buildCommand: npm run build
    startCommand: npm run preview
    autoDeploy: true
    envVars:
      - key: VITE_API_URL
        value: https://trading-platform-backend.onrender.com

  - type: pserv
    name: trading-platform-db
    runtime: postgres
    ipAllowList: []
    plan: free
```

2. **Deploy to Render**
```bash
# Push to GitHub
git add .
git commit -m "Add Render deployment config"
git push origin main

# Go to Render.com → New → Blueprint
# Connect GitHub repo
# Select this repo
# Create
```

3. **Monitor logs**
```
In Render dashboard:
- Click on service
- Go to Logs tab
- Watch deployment progress
```

### Option B: Railway.app

1. **Install Railway CLI**
```bash
npm i -g @railway/cli
```

2. **Login and initialize**
```bash
railway login
railway init
```

3. **Configure services**
```bash
# Add database plugin
railway add --plugin postgres

# Set environment variables
railway variables
# Add: JWT_SECRET, MAIL_USERNAME, MAIL_PASSWORD, etc.
```

4. **Deploy**
```bash
railway up
```

---

## Phase 5: Production Deployment

### Pre-production Checklist

- [ ] Database backups configured
- [ ] SSL/TLS certificates obtained
- [ ] Domain name pointing to deployment
- [ ] Environment variables set securely
- [ ] Error monitoring configured
- [ ] Logging configured
- [ ] Rate limiting configured
- [ ] Database indexes created
- [ ] Performance tested under load

### AWS Deployment (Using Terraform)

1. **Update terraform/variables.tf**
```hcl
variable "environment" {
  default = "production"
}

variable "app_name" {
  default = "trading-platform"
}

variable "region" {
  default = "us-east-1"
}

variable "backend_image" {
  description = "Backend Docker image"
}

variable "frontend_image" {
  description = "Frontend Docker image"
}

variable "database_password" {
  description = "RDS password"
  sensitive = true
}

variable "jwt_secret" {
  description = "JWT secret"
  sensitive = true
}
```

2. **Update GitHub Actions for AWS**

Add to `.github/workflows/ci-cd.yml`:
```yaml
deploy-production:
  needs: docker-build
  if: github.ref == 'refs/tags/v*'
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v3
    
    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v2
      with:
        aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
        aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        aws-region: us-east-1
    
    - name: Deploy with Terraform
      working-directory: ./terraform
      env:
        TF_VAR_backend_image: ghcr.io/${{ github.repository }}/backend:${{ github.ref_name }}
        TF_VAR_frontend_image: ghcr.io/${{ github.repository }}/frontend:${{ github.ref_name }}
        TF_VAR_jwt_secret: ${{ secrets.JWT_SECRET }}
        TF_VAR_database_password: ${{ secrets.DATABASE_PASSWORD }}
      run: |
        terraform init
        terraform plan
        terraform apply -auto-approve
```

3. **Deploy**
```bash
# Create a release tag
git tag v1.0.0
git push origin v1.0.0

# GitHub Actions will automatically run deployment
# Monitor in Actions tab
```

---

## Monitoring & Maintenance

### Setup Health Checks

**Backend Health Endpoint** (Add to application.properties):
```properties
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=when-authorized
management.info.env.enabled=true
```

**Monitor with:**
```bash
# Manual health check
curl https://your-api-domain/actuator/health

# Docker health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
```

### Setup Logging

**Centralized Logging** (Optional but recommended):
- ELK Stack (Elasticsearch, Logstash, Kibana)
- CloudWatch (AWS)
- Datadog
- New Relic
- Sentry (Error tracking)

### Backup Strategy

**Database Backups:**
```bash
# Automated daily backups
# Render: Automatic
# Railway: Configure in dashboard
# AWS RDS: Configure backup retention period
```

**Image Backups:**
- Docker images stored in GitHub Container Registry
- Always keep latest 5 versions

---

## Troubleshooting Deployment

### Issue: "Port already in use"
```bash
# Find what's using port 8080
lsof -i :8080

# Kill process
kill -9 <PID>

# Or use different port in docker-compose
```

### Issue: "Database connection refused"
```bash
# Check MySQL is running
docker-compose ps

# Restart database
docker-compose restart mysql

# Check connection string in environment variables
```

### Issue: "Frontend can't reach backend"
```bash
# Check CORS configuration
# Verify VITE_API_URL environment variable
# Check firewall rules allow cross-origin requests
```

### Issue: "JWT token not working"
```bash
# Verify JWT_SECRET is set
# Check token expiration time
# Verify Authorization header format: "Bearer <token>"
```

### Issue: "Docker image pull fails"
```bash
# Login to GitHub Container Registry
docker login ghcr.io -u username -p token

# Verify image exists
docker image ls | grep trading-platform
```

---

## Rollback Procedure

If deployment fails in production:

```bash
### Using Docker/Kubernetes
kubectl rollout undo deployment/trading-platform-backend
kubectl rollout undo deployment/trading-platform-frontend

### Using Render
# Click on service → Deployments → Previous deployment → Redeploy

### Using Railway
railway logs
# Fix issue
git push origin main  # Triggers automatic redeploy
```

---

## Performance Optimization for Production

### Frontend
```
1. Enable compression (gzip)
2. Cache static assets (1 year)
3. Use CDN (CloudFront, Cloudflare)
4. Minify and compress bundles
5. Lazy load images
6. Enable service workers for offline support
```

### Backend
```
1. Enable connection pooling (HikariCP)
2. Add database query caching
3. Implement rate limiting
4. Use async processing for heavy operations
5. Configure proper JVM heap size
6. Enable HTTP compression
```

### Database
```
1. Create indexes on frequently queried columns
2. Archive old data
3. Optimize slow queries
4. Configure proper connection pool size
5. Regular maintenance tasks
```

---

## Post-Deployment

1. **Smoke Tests**
   - Test signup/login flow
   - Test each main feature
   - Verify email notifications work
   - Check 2FA functionality

2. **Monitor**
   - Watch error logs
   - Monitor response times
   - Check CPU/memory usage
   - Monitor database connections

3. **User Communication**
   - Announce deployment
   - Explain new features
   - Provide support contact

4. **Plan Improvements**
   - Collect user feedback
   - Plan next features
   - Schedule maintenance windows
   - Plan for scaling

---

## Quick Deployment Reference

```bash
# Local Testing
docker-compose up -d
# Test at http://localhost

# Staging (Render)
git push origin main
# Automatic deploy to Render

# Production (AWS)
git tag v1.0.1
git push origin v1.0.1
# Automatic deploy to AWS via Terraform

# View Logs
docker-compose logs -f
docker-compose logs -f backend

# Stop Everything
docker-compose down
docker system prune -a  # Clean up old images
```

---

**You're ready to deploy! Start with local → staging → production.** 🚀

