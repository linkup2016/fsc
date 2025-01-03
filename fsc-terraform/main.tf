provider "aws" {
  region = "us-east-2"
}

# DynamoDB Table: Stores scratch card data with primary keys `scratchCardNumber` and `createdDate`.
resource "aws_dynamodb_table" "scratch_card_table" {
  name           = "ScratchCardTable"
  billing_mode   = "PAY_PER_REQUEST"
  hash_key       = "scratchCardNumber"
  range_key      = "createdDate"

  attribute {
    name = "scratchCardNumber"
    type = "S"
  }

  attribute {
    name = "createdDate"
    type = "S"
  }

  tags = {
    Name = "ScratchCardTable"
  }
}

# ECS Cluster: Manages the ECS service and task deployments.
resource "aws_ecs_cluster" "fsc_cluster" {
  name = "fsc-cluster"
}

# IAM Role for ECS Task Execution: Grants permissions to ECS tasks for basic operations like pulling container images.
resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecs-task-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
        Effect = "Allow"
        Sid       = ""
      }
    ]
  })
}

# Attach the default ECS Task Execution policy to the task execution role.
resource "aws_iam_role_policy_attachment" "ecs_task_execution_policy" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  role       = aws_iam_role.ecs_task_execution_role.name
}

# IAM Role for DynamoDB Access: Grants ECS tasks permissions to interact with DynamoDB.
resource "aws_iam_role" "ecs_dynamodb_role" {
  name = "ecs-dynamodb-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
        Effect = "Allow"
      }
    ]
  })
}

# Attach the DynamoDB Full Access policy to the DynamoDB role.
resource "aws_iam_role_policy_attachment" "ecs_dynamodb_policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess"
  role       = aws_iam_role.ecs_dynamodb_role.name
}

# ECS Task Definition: Defines the container and resources used for the ECS task.
resource "aws_ecs_task_definition" "fsc_task" {
  family                   = "fsc-task"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_dynamodb_role.arn
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"

  container_definitions = jsonencode([{
    name      = "fsc-container"
    image     = "593793059395.dkr.ecr.us-east-2.amazonaws.com/fsc-container-repo:latest"
    essential = true
    portMappings = [
      {
        containerPort = 8080
        hostPort      = 8080
        protocol      = "tcp"
      }
    ]
  }])
}

# VPC: Creates a virtual private cloud for network isolation.
resource "aws_vpc" "fsc_vpc" {
  cidr_block = "172.31.0.0/16"
}

# Subnet 1: Public subnet in availability zone `us-east-2a`.
resource "aws_subnet" "fsc_subnet_1" {
  vpc_id                  = aws_vpc.fsc_vpc.id
  cidr_block              = "172.31.1.0/24"
  availability_zone       = "us-east-2a"
  map_public_ip_on_launch = true
}

# Subnet 2: Public subnet in availability zone `us-east-2b`.
resource "aws_subnet" "fsc_subnet_2" {
  vpc_id                  = aws_vpc.fsc_vpc.id
  cidr_block              = "172.31.2.0/24"
  availability_zone       = "us-east-2b"
  map_public_ip_on_launch = true
}

# Internet Gateway: Provides internet access for resources in the VPC.
resource "aws_internet_gateway" "fsc_igw" {
  vpc_id = aws_vpc.fsc_vpc.id
}

# Security Group: Controls inbound and outbound traffic to ECS tasks and ALB.
resource "aws_security_group" "fsc_sg" {
  name        = "fsc-sg"
  description = "Allow inbound traffic on port 8080"
  vpc_id      = aws_vpc.fsc_vpc.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Application Load Balancer (ALB): Distributes incoming traffic to ECS tasks.
resource "aws_lb" "fsc_alb" {
  name               = "fsc-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.fsc_sg.id]
  subnets            = [aws_subnet.fsc_subnet_1.id, aws_subnet.fsc_subnet_2.id]
}

# Target Group: Defines the group of ECS tasks ALB routes traffic to.
resource "aws_lb_target_group" "fsc_target_group" {
  name         = "fsc-target-group"
  port         = 8080
  protocol     = "HTTP"
  vpc_id       = aws_vpc.fsc_vpc.id
  target_type  = "ip"

  health_check {
    path                = "/health"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
}

# ALB Listener: Routes incoming HTTP requests to the target group.
resource "aws_lb_listener" "fsc_listener" {
  load_balancer_arn = aws_lb.fsc_alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.fsc_target_group.arn
  }
}

# ECS Service: Deploys and manages the ECS tasks behind the ALB.
resource "aws_ecs_service" "fsc_service" {
  name            = "fsc-service"
  cluster         = aws_ecs_cluster.fsc_cluster.id
  task_definition = aws_ecs_task_definition.fsc_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [aws_subnet.fsc_subnet_1.id, aws_subnet.fsc_subnet_2.id]
    security_groups  = [aws_security_group.fsc_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.fsc_target_group.arn
    container_name   = "fsc-container"
    container_port   = 8080
  }

  depends_on = [aws_lb_listener.fsc_listener]
}

# API Gateway: Manages API routes and integration with ALB.
resource "aws_api_gateway_rest_api" "fsc" {
  name        = "fsc"
  description = "API for Scratch Card Service"
}

# API Resource for Validate Endpoint
resource "aws_api_gateway_resource" "validate_resource" {
  rest_api_id = aws_api_gateway_rest_api.fsc.id
  parent_id   = aws_api_gateway_rest_api.fsc.root_resource_id
  path_part   = "validate"
}

# API Method for Validate Endpoint: Accepts POST requests.
resource "aws_api_gateway_method" "validate_method" {
  rest_api_id   = aws_api_gateway_rest_api.fsc.id
  resource_id   = aws_api_gateway_resource.validate_resource.id
  http_method   = "POST"
  authorization = "NONE"
}

# API Integration: Connects the API Gateway to the ALB.
resource "aws_api_gateway_integration" "validate_integration" {
  rest_api_id             = aws_api_gateway_rest_api.fsc.id
  resource_id             = aws_api_gateway_resource.validate_resource.id
  http_method             = aws_api_gateway_method.validate_method.http_method
  integration_http_method = "POST"
  type                    = "HTTP"
  uri                     = "http://${aws_lb.fsc_alb.dns_name}"
}


# API Deployment: Deploys the API Gateway configuration.
resource "aws_api_gateway_deployment" "fsc_deployment" {
  rest_api_id = aws_api_gateway_rest_api.fsc.id

  depends_on = [
    aws_api_gateway_integration.validate_integration
  ]
}

# API Stage: Exposes the API deployment for production use.
resource "aws_api_gateway_stage" "fsc_stage" {
  rest_api_id   = aws_api_gateway_rest_api.fsc.id
  deployment_id = aws_api_gateway_deployment.fsc_deployment.id
  stage_name    = "prod"
}
