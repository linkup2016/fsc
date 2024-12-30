provider "aws" {
  region = "us-east-1" # Change to your preferred region
}

# S3 Bucket to store the application
resource "aws_s3_bucket" "app_bucket" {
  bucket = "fsc-bucket"

  versioning {
    enabled = true
  }

  tags = {
    Name        = "JavaAppBucket"
    Environment = "Production"
  }
}

resource "aws_s3_bucket_object" "app_jar" {
  bucket = aws_s3_bucket.app_bucket.id
  key    = "app.jar" # Name of the JAR/WAR file
  source = "path/to/your/app.jar" # Local path to your JAR/WAR file
}

# Elastic Beanstalk Application
resource "aws_elastic_beanstalk_application" "java_app" {
  name        = "JavaApp"
  description = "Java application deployed using Elastic Beanstalk"
}

# Elastic Beanstalk Application Version
resource "aws_elastic_beanstalk_application_version" "java_app_version" {
  name        = "v1"
  application = aws_elastic_beanstalk_application.java_app.name
  bucket      = aws_s3_bucket.app_bucket.id
  key         = aws_s3_bucket_object.app_jar.key
}

# IAM Role for Elastic Beanstalk
resource "aws_iam_role" "beanstalk_role" {
  name = "ElasticBeanstalkRole"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action    = "sts:AssumeRole",
        Effect    = "Allow",
        Principal = { Service = "elasticbeanstalk.amazonaws.com" }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "beanstalk_policy" {
  role       = aws_iam_role.beanstalk_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkFullAccess"
}

# Elastic Beanstalk Environment
resource "aws_elastic_beanstalk_environment" "java_env" {
  name                = "JavaAppEnvironment"
  application         = aws_elastic_beanstalk_application.java_app.name
  solution_stack_name = "64bit Amazon Linux 2 v3.8.12 running Corretto 21" # Choose the appropriate platform

  setting {
    namespace = "aws:autoscaling:launch-configuration"
    name      = "IamInstanceProfile"
    value     = aws_iam_instance_profile.beanstalk_instance_profile.arn
  }

  version_label = aws_elastic_beanstalk_application_version.java_app_version.name
}
