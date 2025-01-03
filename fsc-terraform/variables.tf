variable "aws_region" {
  description = "The AWS region to deploy resources in."
  type        = string
  default     = "us-east-2" # You can set the default region here or override it when running Terraform
}
