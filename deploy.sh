#!/bin/bash

# Exit on any error
set -e

# Colors for terminal output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Print header
echo -e "${GREEN}====================================${NC}"
echo -e "${GREEN}  Trading Platform Deployment Tool  ${NC}"
echo -e "${GREEN}====================================${NC}"

# Function to display usage information
function show_usage() {
  echo -e "Usage: ./deploy.sh [OPTION]"
  echo -e "Options:"
  echo -e "  ${YELLOW}local${NC}    - Run the application locally using Docker Compose"
  echo -e "  ${YELLOW}aws${NC}      - Deploy to AWS (requires AWS CLI configured)"
  echo -e "  ${YELLOW}heroku${NC}   - Deploy to Heroku (requires Heroku CLI)"
  echo -e "  ${YELLOW}render${NC}   - Generate deployment files for Render.com"
  echo -e "  ${YELLOW}railway${NC}  - Generate deployment files for Railway.app"
  echo -e "  ${YELLOW}clean${NC}    - Clean all Docker resources"
  echo -e "  ${YELLOW}help${NC}     - Show this help message"
}

# Function to check if Docker is installed and running
function check_docker() {
  echo -e "Checking if Docker is installed and running..."
  if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed. Please install Docker first.${NC}"
    exit 1
  fi
  
  if ! docker info &> /dev/null; then
    echo -e "${RED}Docker is not running. Please start Docker first.${NC}"
    exit 1
  fi
  
  echo -e "${GREEN}Docker is installed and running.${NC}"
}

# Function to check if Docker Compose is installed
function check_docker_compose() {
  echo -e "Checking if Docker Compose is installed..."
  if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo -e "${RED}Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
  fi
  
  echo -e "${GREEN}Docker Compose is installed.${NC}"
}

# Function to deploy locally using Docker Compose
function deploy_local() {
  check_docker
  check_docker_compose
  
  echo -e "${YELLOW}Building and starting containers...${NC}"
  docker-compose up --build -d
  
  echo -e "${GREEN}Application is now running!${NC}"
  echo -e "Frontend: http://localhost"
  echo -e "Backend: http://localhost:8080"
  echo -e "${YELLOW}To view logs: ${NC}docker-compose logs -f"
  echo -e "${YELLOW}To stop: ${NC}docker-compose down"
}

# Function to clean Docker resources
function clean() {
  echo -e "${YELLOW}Stopping containers...${NC}"
  docker-compose down || true
  
  echo -e "${YELLOW}Removing volumes...${NC}"
  docker-compose down -v || true
  
  echo -e "${YELLOW}Pruning unused Docker resources...${NC}"
  docker system prune -f
  
  echo -e "${GREEN}Cleaned up Docker resources.${NC}"
}

# Function to deploy to AWS
function deploy_aws() {
  check_docker
  
  echo -e "${YELLOW}Checking AWS CLI...${NC}"
  if ! command -v aws &> /dev/null; then
    echo -e "${RED}AWS CLI is not installed. Please install it first.${NC}"
    exit 1
  fi
  
  echo -e "${YELLOW}Creating ECR repositories if they don't exist...${NC}"
  aws ecr create-repository --repository-name trading-platform-frontend || true
  aws ecr create-repository --repository-name trading-platform-backend || true
  
  # Get AWS account ID
  AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
  AWS_REGION=$(aws configure get region || echo "us-east-1")
  
  echo -e "${YELLOW}Logging into ECR...${NC}"
  aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com
  
  echo -e "${YELLOW}Building and pushing frontend image...${NC}"
  docker build -t trading-platform-frontend ./frontend
  docker tag trading-platform-frontend:latest $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/trading-platform-frontend:latest
  docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/trading-platform-frontend:latest
  
  echo -e "${YELLOW}Building and pushing backend image...${NC}"
  docker build -t trading-platform-backend ./backend
  docker tag trading-platform-backend:latest $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/trading-platform-backend:latest
  docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/trading-platform-backend:latest
  
  echo -e "${GREEN}Images have been pushed to ECR.${NC}"
  echo -e "${YELLOW}To deploy to ECS/EKS, follow the documentation or create CloudFormation/Terraform templates.${NC}"
}

# Function to deploy to Heroku
function deploy_heroku() {
  check_docker
  
  echo -e "${YELLOW}Checking Heroku CLI...${NC}"
  if ! command -v heroku &> /dev/null; then
    echo -e "${RED}Heroku CLI is not installed. Please install it first.${NC}"
    exit 1
  fi
  
  echo -e "${YELLOW}Logging into Heroku Container Registry...${NC}"
  heroku container:login
  
  echo -e "${YELLOW}Creating Heroku apps if they don't exist...${NC}"
  heroku apps:create trading-platform-frontend || true
  heroku apps:create trading-platform-backend || true
  
  echo -e "${YELLOW}Adding Heroku add-ons for database...${NC}"
  heroku addons:create jawsdb:kitefin --app trading-platform-backend || true
  
  echo -e "${YELLOW}Building and pushing frontend to Heroku...${NC}"
  cd frontend && heroku container:push web --app trading-platform-frontend
  heroku container:release web --app trading-platform-frontend
  cd ..
  
  echo -e "${YELLOW}Building and pushing backend to Heroku...${NC}"
  cd backend && heroku container:push web --app trading-platform-backend
  heroku container:release web --app trading-platform-backend
  cd ..
  
  # Set environment variables
  echo -e "${YELLOW}Setting environment variables...${NC}"
  BACKEND_URL=$(heroku apps:info trading-platform-backend --json | grep web_url | cut -d'"' -f4)
  heroku config:set VITE_API_URL=$BACKEND_URL --app trading-platform-frontend
  
  echo -e "${GREEN}Deployment to Heroku completed!${NC}"
  echo -e "Frontend: https://trading-platform-frontend.herokuapp.com"
  echo -e "Backend: https://trading-platform-backend.herokuapp.com"
}

# Function to prepare for Render.com deployment
function deploy_render() {
  echo -e "${YELLOW}Creating render.yaml for Render.com deployment...${NC}"
  
  cat > render.yaml << 'EOL'
services:
  # Frontend service
  - type: web
    name: trading-platform-frontend
    env: docker
    rootDir: ./frontend
    dockerfilePath: ./Dockerfile
    envVars:
      - key: VITE_API_URL
        fromService:
          name: trading-platform-backend
          type: web
          property: url
    autoDeploy: true

  # Backend service
  - type: web
    name: trading-platform-backend
    env: docker
    rootDir: ./backend
    dockerfilePath: ./Dockerfile
    envVars:
      - key: SPRING_DATASOURCE_URL
        fromDatabase:
          name: trading-platform-db
          property: connectionString
      - key: SPRING_DATASOURCE_USERNAME
        fromDatabase:
          name: trading-platform-db
          property: user
      - key: SPRING_DATASOURCE_PASSWORD
        fromDatabase:
          name: trading-platform-db
          property: password
      - key: SPRING_JPA_HIBERNATE_DDL_AUTO
        value: update
    autoDeploy: true

databases:
  - name: trading-platform-db
    databaseName: tradingplatform
    plan: free
EOL

  echo -e "${GREEN}render.yaml file created!${NC}"
  echo -e "To deploy to Render.com:"
  echo -e "1. Create a new account or login to Render.com"
  echo -e "2. Connect your GitHub repository"
  echo -e "3. Create a Blueprint from this render.yaml file"
  echo -e "4. Deploy the blueprint"
}

# Function to prepare for Railway.app deployment
function deploy_railway() {
  echo -e "${YELLOW}Checking Railway CLI...${NC}"
  if ! command -v railway &> /dev/null; then
    echo -e "${RED}Railway CLI is not installed. Please install it first:${NC}"
    echo -e "npm i -g @railway/cli"
    echo -e "Then login with: railway login"
    exit 1
  fi
  
  echo -e "${YELLOW}Setting up Railway project...${NC}"
  railway init
  
  # Create railway.json files for each service
  echo -e "${YELLOW}Creating railway.json files for each service...${NC}"
  
  mkdir -p railway
  
  # Frontend railway.json
  cat > railway/frontend.json << 'EOL'
{
  "schema": "https://railway.app/railway.schema.json",
  "build": {
    "builder": "DOCKERFILE",
    "dockerfilePath": "../frontend/Dockerfile"
  },
  "deploy": {
    "numReplicas": 1,
    "sleepApplication": false,
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
EOL

  # Backend railway.json
  cat > railway/backend.json << 'EOL'
{
  "schema": "https://railway.app/railway.schema.json",
  "build": {
    "builder": "DOCKERFILE",
    "dockerfilePath": "../backend/Dockerfile"
  },
  "deploy": {
    "numReplicas": 1,
    "sleepApplication": false,
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
EOL

  echo -e "${GREEN}Railway.app deployment files created!${NC}"
  echo -e "To deploy to Railway.app:"
  echo -e "1. Run: railway up -s frontend -f railway/frontend.json"
  echo -e "2. Run: railway up -s backend -f railway/backend.json"
  echo -e "3. Add a MySQL database service from the Railway dashboard"
  echo -e "4. Link the database to your backend service"
}

# Main script logic
case "$1" in
  local)
    deploy_local
    ;;
  aws)
    deploy_aws
    ;;
  heroku)
    deploy_heroku
    ;;
  render)
    deploy_render
    ;;
  railway)
    deploy_railway
    ;;
  clean)
    clean
    ;;
  help|--help|-h)
    show_usage
    ;;
  *)
    echo -e "${RED}Invalid option: $1${NC}"
    show_usage
    exit 1
    ;;
esac 