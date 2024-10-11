provider "aws" {
  region = var.region

  default_tags {
    tags = {
      Environment = var.env
      App         = var.app_name
      Workspace   = terraform.workspace
    }
  }
}
