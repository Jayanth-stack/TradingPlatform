.PHONY: build run deploy-local clean help aws heroku render terraform-init terraform-plan terraform-apply

# Default target
help:
	@echo "Trading Platform Build and Deployment Tool"
	@echo "-------------------------------------------"
	@echo "Available commands:"
	@echo "  make build          - Build Docker images for frontend and backend"
	@echo "  make run            - Run the application locally using Docker Compose"
	@echo "  make deploy-local   - Build and run the application locally"
	@echo "  make clean          - Clean up Docker containers and volumes"
	@echo "  make aws            - Deploy to AWS using the deploy.sh script"
	@echo "  make heroku         - Deploy to Heroku using the deploy.sh script"
	@echo "  make render         - Generate deployment files for Render.com"
	@echo "  make terraform-init - Initialize Terraform"
	@echo "  make terraform-plan - Plan Terraform changes"
	@echo "  make terraform-apply - Apply Terraform changes"

# Build Docker images
build:
	docker-compose build

# Run the application locally
run:
	docker-compose up -d

# Build and run the application locally
deploy-local:
	chmod +x ./deploy.sh && ./deploy.sh local

# Clean up Docker containers and volumes
clean:
	chmod +x ./deploy.sh && ./deploy.sh clean

# Deploy to AWS
aws:
	chmod +x ./deploy.sh && ./deploy.sh aws

# Deploy to Heroku
heroku:
	chmod +x ./deploy.sh && ./deploy.sh heroku

# Generate deployment files for Render.com
render:
	chmod +x ./deploy.sh && ./deploy.sh render

# Initialize Terraform
terraform-init:
	cd terraform && terraform init

# Plan Terraform changes
terraform-plan:
	cd terraform && terraform plan -var-file=terraform.tfvars

# Apply Terraform changes
terraform-apply:
	cd terraform && terraform apply -var-file=terraform.tfvars -auto-approve 