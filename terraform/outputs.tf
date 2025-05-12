output "frontend_url" {
  description = "URL of the frontend load balancer"
  value       = "http://${aws_lb.frontend.dns_name}"
}

output "backend_url" {
  description = "URL of the backend load balancer"
  value       = "http://${aws_lb.backend.dns_name}:8080"
}

output "database_endpoint" {
  description = "Endpoint of the RDS database"
  value       = aws_db_instance.default.endpoint
  sensitive   = true
}

output "ecr_frontend_repository_url" {
  description = "URL of the ECR repository for the frontend"
  value       = aws_ecr_repository.frontend.repository_url
}

output "ecr_backend_repository_url" {
  description = "URL of the ECR repository for the backend"
  value       = aws_ecr_repository.backend.repository_url
}

output "ecs_cluster_name" {
  description = "Name of the ECS cluster"
  value       = aws_ecs_cluster.main.name
}

output "frontend_service_name" {
  description = "Name of the frontend ECS service"
  value       = aws_ecs_service.frontend.name
}

output "backend_service_name" {
  description = "Name of the backend ECS service"
  value       = aws_ecs_service.backend.name
}

output "aws_region" {
  description = "AWS region where the resources are deployed"
  value       = var.aws_region
}

output "deployment_instructions" {
  description = "Next steps after deployment"
  value       = <<-EOT
    ✅ Deployment complete! Your Trading Platform is now running.
    
    📱 Frontend: http://${aws_lb.frontend.dns_name}
    🖥️ Backend API: http://${aws_lb.backend.dns_name}:8080
    
    📝 To update the application:
    1. Build new Docker images
    2. Push to ECR:
       - Frontend: ${aws_ecr_repository.frontend.repository_url}
       - Backend: ${aws_ecr_repository.backend.repository_url}
    3. Force new deployment:
       aws ecs update-service --cluster ${aws_ecs_cluster.main.name} --service ${aws_ecs_service.frontend.name} --force-new-deployment
       aws ecs update-service --cluster ${aws_ecs_cluster.main.name} --service ${aws_ecs_service.backend.name} --force-new-deployment
    
    📊 Monitor your services in the AWS Console:
    - ECS: https://${var.aws_region}.console.aws.amazon.com/ecs/home?region=${var.aws_region}#/clusters/${aws_ecs_cluster.main.name}
    - RDS: https://${var.aws_region}.console.aws.amazon.com/rds/home?region=${var.aws_region}#database:id=${aws_db_instance.default.id}
    - Load Balancers: https://${var.aws_region}.console.aws.amazon.com/ec2/home?region=${var.aws_region}#LoadBalancers
    
    🔄 To destroy all resources when no longer needed:
    terraform destroy -var-file=terraform.tfvars
  EOT
} 