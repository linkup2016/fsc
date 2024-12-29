variable "region" {
  default = "us-east-1"
}

variable "app_name" {
  default = "JavaApp"
}

variable "app_version" {
  default = "v1"
}

variable "app_file" {
  description = "Path to the application JAR/WAR file"
  default     = "path/to/your/app.jar"
}
