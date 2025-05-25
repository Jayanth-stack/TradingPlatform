# 🏗️ Trading Platform Infrastructure Guide

This directory contains Terraform configurations for deploying your trading platform infrastructure. Choose the setup that best fits your needs and budget.

## 📋 **Quick Start Options**

### **Option 1: Local Development (FREE)** 🆓
Perfect for development and testing without cloud costs.

```bash
# Use local Docker setup
cd terraform
terraform init
terraform apply -var-file="terraform.tfvars.local" -var="use_local_development=true"
```

### **Option 2: AWS Free Tier (~$15-30/month)** 💰
Minimal AWS setup using free tier resources.

```bash
# Use free tier configuration
cp terraform.tfvars.free-tier terraform.tfvars
terraform init
terraform apply -var-file="terraform.tfvars"
```

### **Option 3: Production AWS (~$100-200/month)** 🚀
Full production setup with high availability.

```bash
# Use production configuration
cp terraform.tfvars.example terraform.tfvars
# Edit terraform.tfvars with your values
terraform init
terraform apply -var-file="terraform.tfvars"
```

---

## 🛠️ **Prerequisites**

### **For Local Development:**
- [Docker](https://docs.docker.com/get-docker/) installed
- [Terraform](https://learn.hashicorp.com/tutorials/terraform/install-cli) installed

### **For AWS Deployment:**
- AWS Account with appropriate permissions
- [AWS CLI](https://aws.amazon.com/cli/) configured
- [Terraform](https://learn.hashicorp.com/tutorials/terraform/install-cli) installed

---

## 💰 **Cost Breakdown**

### **Local Development (FREE)**
- **Cost**: $0/month
- **Infrastructure**: Local Docker containers
- **Suitable for**: Development, testing, learning

### **AWS Free Tier Setup**
| Service | Configuration | Monthly Cost |
|---------|---------------|--------------|
| ECS Fargate | 2 tasks (minimal CPU/memory) | ~$15-25 |
| RDS MySQL | db.t3.micro | FREE (first 12 months) |
| Load Balancer | 1 ALB | ~$16 |
| Data Transfer | Minimal usage | ~$2-5 |
| **Total** |  | **~$33-46/month** |

### **Production Setup**
| Service | Configuration | Monthly Cost |
|---------|---------------|--------------|
| ECS Fargate | 4 tasks (optimized resources) | ~$60-100 |
| RDS MySQL | db.t3.small with backups | ~$25-35 |
| Load Balancers | 2 ALBs | ~$32 |
| Data Transfer | Moderate usage | ~$10-20 |
| CloudWatch | Logging and monitoring | ~$5-10 |
| **Total** |  | **~$132-197/month** |

---

## 🔧 **Configuration Files Explained**

### **Main Files**
- `main.tf` - Core AWS infrastructure definition
- `variables.tf` - Original variable definitions
- `outputs.tf` - Information displayed after deployment
- `local-dev.tf` - Local Docker setup (no AWS needed)

### **Configuration Options**
- `terraform.tfvars.example` - Template for production setup
- `terraform.tfvars.free-tier` - Optimized for minimal AWS costs
- `variables-enhanced.tf` - Extended variables with validations

---

## 🚀 **Step-by-Step Deployment**

### **1. Choose Your Setup**

#### **For Local Development (Recommended for beginners):**
```bash
# Clone and navigate to terraform directory
cd terraform

# Initialize Terraform with Docker provider
terraform init

# Apply local development setup
terraform apply -target="docker_network.trading_platform" -auto-approve
terraform apply -auto-approve

# Access your application
echo "🎉 Your trading platform is running at:"
echo "Frontend: http://localhost:3000"
echo "Backend API: http://localhost:8080"
```

#### **For AWS Free Tier:**
```bash
# 1. Configure AWS credentials
aws configure

# 2. Create your variables file
cp terraform.tfvars.free-tier terraform.tfvars

# 3. Edit terraform.tfvars and change:
# - db_password (make it secure!)
# - aws_region (if needed)

# 4. Initialize and apply
terraform init
terraform plan  # Review what will be created
terraform apply # Type 'yes' when prompted
```

#### **For Production AWS:**
```bash
# 1. Configure AWS credentials with sufficient permissions
aws configure

# 2. Create your variables file
cp terraform.tfvars.example terraform.tfvars

# 3. Edit terraform.tfvars and configure:
vim terraform.tfvars

# 4. Initialize and deploy
terraform init
terraform plan -var-file="terraform.tfvars"
terraform apply -var-file="terraform.tfvars"
```

### **2. Access Your Application**

After deployment, Terraform will output URLs:

```bash
# View all outputs
terraform output

# Specific outputs
terraform output frontend_url
terraform output backend_url
```

### **3. Update Your Application**

#### **For Local Development:**
```bash
# After making code changes
terraform apply -auto-approve
```

#### **For AWS:**
```bash
# Push new Docker images (handled by GitHub Actions)
# or manually update ECS services
aws ecs update-service --cluster trading-platform-cluster --service trading-platform-frontend --force-new-deployment
```

---

## 🛡️ **Security Configuration**

### **Database Security**
- Change default passwords in `terraform.tfvars`
- Database is in private subnets (not accessible from internet)
- Security groups restrict access to application only

### **Application Security**
- Load balancers in public subnets
- Application containers in private subnets
- Security groups control traffic flow

### **Best Practices**
1. **Never commit** `terraform.tfvars` with real passwords
2. Use **strong passwords** for database
3. Enable **backup retention** for production
4. Consider **encryption** for sensitive data

---

## 🔄 **Common Operations**

### **View Infrastructure Status**
```bash
# Check current state
terraform show

# List all resources
terraform state list

# Get specific resource info
terraform state show aws_ecs_service.frontend
```

### **Scale Your Application**
```bash
# Edit terraform.tfvars
backend_desired_count = 3
frontend_desired_count = 2

# Apply changes
terraform apply -var-file="terraform.tfvars"
```

### **Update Configuration**
```bash
# Make changes to .tf files or terraform.tfvars
# Plan changes
terraform plan -var-file="terraform.tfvars"

# Apply changes
terraform apply -var-file="terraform.tfvars"
```

### **Destroy Infrastructure**
```bash
# ⚠️ This will delete EVERYTHING
terraform destroy -var-file="terraform.tfvars"
```

---

## 🐛 **Troubleshooting**

### **Common Issues**

#### **"Insufficient permissions" error**
```bash
# Ensure your AWS user has these policies:
# - AmazonECS_FullAccess
# - AmazonRDS_FullAccess
# - AmazonVPCFullAccess
# - IAMFullAccess
# - AmazonEC2FullAccess
```

#### **"Resource already exists" error**
```bash
# Import existing resource or remove from state
terraform import aws_ecr_repository.frontend trading-platform-frontend
```

#### **Database connection issues**
```bash
# Check security groups and subnet configuration
terraform output database_endpoint
```

#### **ECS tasks not starting**
```bash
# Check ECS console for detailed error messages
# Common issues:
# - Image not found in ECR
# - Insufficient memory/CPU
# - Environment variable errors
```

### **Debugging Steps**
1. Check AWS Console for detailed error messages
2. Review CloudWatch logs
3. Verify ECR images exist
4. Check security group rules
5. Validate environment variables

---

## 📚 **Learning Resources**

### **Terraform Basics**
- [Official Terraform Tutorial](https://learn.hashicorp.com/terraform)
- [Terraform AWS Provider Docs](https://registry.terraform.io/providers/hashicorp/aws/latest/docs)

### **AWS Services Used**
- [Amazon ECS Documentation](https://docs.aws.amazon.com/ecs/)
- [Amazon RDS Documentation](https://docs.aws.amazon.com/rds/)
- [AWS VPC Documentation](https://docs.aws.amazon.com/vpc/)

---

## 📞 **Getting Help**

### **If you need assistance:**
1. Check the troubleshooting section above
2. Review AWS CloudWatch logs
3. Check GitHub Issues in your repository
4. Consult AWS documentation for specific services

---

## 🎯 **Next Steps**

### **After Successful Deployment:**
1. **Set up monitoring** - Configure CloudWatch alarms
2. **Enable HTTPS** - Add SSL certificate
3. **Configure backup** - Set up automated RDS backups
4. **Add auto-scaling** - Configure ECS service auto-scaling
5. **Security hardening** - Review and tighten security groups

### **For Production:**
1. **Domain setup** - Configure Route 53 and custom domain
2. **CDN setup** - Add CloudFront for better performance
3. **WAF setup** - Configure Web Application Firewall
4. **Monitoring** - Set up comprehensive monitoring and alerting 