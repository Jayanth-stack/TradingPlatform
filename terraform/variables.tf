variable "aws_region" {
  description = "The AWS region where resources will be created"
  type        = string
  default     = "us-east-1"
}

variable "db_username" {
  description = "Username for the RDS database"
  type        = string
  default     = "admin"
  sensitive   = true
}

variable "db_password" {
  description = "Password for the RDS database"
  type        = string
  sensitive   = true
}

variable "environment" {
  description = "Environment name (e.g., dev, staging, prod)"
  type        = string
  default     = "dev"
}

variable "app_name" {
  description = "Name of the application"
  type        = string
  default     = "trading-platform"
}

variable "frontend_container_port" {
  description = "Port exposed by the frontend container"
  type        = number
  default     = 80
}

variable "backend_container_port" {
  description = "Port exposed by the backend container"
  type        = number
  default     = 8080
}

variable "db_allocated_storage" {
  description = "Allocated storage for the RDS instance (in GiB)"
  type        = number
  default     = 20
}

variable "db_instance_class" {
  description = "RDS instance class"
  type        = string
  default     = "db.t3.micro"
}

variable "backend_desired_count" {
  description = "Number of backend tasks to run"
  type        = number
  default     = 2
}

variable "frontend_desired_count" {
  description = "Number of frontend tasks to run"
  type        = number
  default     = 2
}

variable "backend_cpu" {
  description = "CPU units for the backend task"
  type        = string
  default     = "512"
}

variable "backend_memory" {
  description = "Memory for the backend task (in MiB)"
  type        = string
  default     = "1024"
}

variable "frontend_cpu" {
  description = "CPU units for the frontend task"
  type        = string
  default     = "256"
}

variable "frontend_memory" {
  description = "Memory for the frontend task (in MiB)"
  type        = string
  default     = "512"
} 