# Local Development Infrastructure using Docker
# This configuration runs everything locally using Docker
# No AWS costs, perfect for development and testing

terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0"
    }
  }
}

provider "docker" {}

# Create a custom network for our application
resource "docker_network" "trading_platform" {
  name = "trading-platform-network"
}

# MySQL Database Container
resource "docker_image" "mysql" {
  name = "mysql:8.0"
}

resource "docker_container" "mysql_db" {
  image = docker_image.mysql.image_id
  name  = "trading-platform-db"
  
  env = [
    "MYSQL_ROOT_PASSWORD=rootpassword",
    "MYSQL_DATABASE=tradingplatform",
    "MYSQL_USER=admin",
    "MYSQL_PASSWORD=password123"
  ]
  
  ports {
    internal = 3306
    external = 3306
  }
  
  networks_advanced {
    name = docker_network.trading_platform.name
  }
  
  # Data persistence
  volumes {
    host_path      = "${path.cwd}/mysql-data"
    container_path = "/var/lib/mysql"
  }
}

# Backend Container (will be built from your local code)
resource "docker_image" "backend" {
  name = "trading-platform-backend:latest"
  build {
    context = "../backend"
  }
}

resource "docker_container" "backend" {
  image = docker_image.backend.image_id
  name  = "trading-platform-backend"
  
  env = [
    "SPRING_DATASOURCE_URL=jdbc:mysql://trading-platform-db:3306/tradingplatform?useSSL=false&allowPublicKeyRetrieval=true",
    "SPRING_DATASOURCE_USERNAME=admin",
    "SPRING_DATASOURCE_PASSWORD=password123",
    "SPRING_JPA_HIBERNATE_DDL_AUTO=update",
    "SPRING_PROFILES_ACTIVE=dev"
  ]
  
  ports {
    internal = 8080
    external = 8080
  }
  
  networks_advanced {
    name = docker_network.trading_platform.name
  }
  
  depends_on = [docker_container.mysql_db]
}

# Frontend Container (will be built from your local code)
resource "docker_image" "frontend" {
  name = "trading-platform-frontend:latest"
  build {
    context = "../frontend"
  }
}

resource "docker_container" "frontend" {
  image = docker_image.frontend.image_id
  name  = "trading-platform-frontend"
  
  env = [
    "VITE_API_URL=http://localhost:8080"
  ]
  
  ports {
    internal = 80
    external = 3000
  }
  
  networks_advanced {
    name = docker_network.trading_platform.name
  }
  
  depends_on = [docker_container.backend]
}

# Redis for caching (optional but good for trading platforms)
resource "docker_image" "redis" {
  name = "redis:7-alpine"
}

resource "docker_container" "redis" {
  image = docker_image.redis.image_id
  name  = "trading-platform-redis"
  
  ports {
    internal = 6379
    external = 6379
  }
  
  networks_advanced {
    name = docker_network.trading_platform.name
  }
}

# Outputs for local development
output "local_frontend_url" {
  description = "Local frontend URL"
  value       = "http://localhost:3000"
}

output "local_backend_url" {
  description = "Local backend API URL"
  value       = "http://localhost:8080"
}

output "local_database_url" {
  description = "Local database connection"
  value       = "mysql://admin:password123@localhost:3306/tradingplatform"
  sensitive   = true
}

output "redis_url" {
  description = "Redis cache URL"
  value       = "redis://localhost:6379"
}

output "development_instructions" {
  description = "Instructions for local development"
  value       = <<-EOT
    🚀 Local Development Environment Ready!
    
    📱 Frontend: http://localhost:3000
    🖥️ Backend API: http://localhost:8080
    🗄️ Database: mysql://localhost:3306/tradingplatform
    🔄 Redis Cache: redis://localhost:6379
    
    📝 To make changes:
    1. Edit your code in ../frontend or ../backend
    2. Run: terraform apply -auto-approve
    3. Your containers will be rebuilt with new code
    
    🛑 To stop everything:
    terraform destroy -auto-approve
    
    📊 To view logs:
    docker logs trading-platform-backend
    docker logs trading-platform-frontend
    docker logs trading-platform-db
    
    💾 Database data is persisted in ./mysql-data/
  EOT
} 