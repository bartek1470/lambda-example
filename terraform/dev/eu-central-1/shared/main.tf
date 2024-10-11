module "artifacts_bucket" {
  source = "../../../modules/artifacts-bucket"
  env    = var.env
}

module "ktnative_ecr" {
  source = "../../../modules/ecr"
  name   = "lambda-example-native"
}

# module "vpc" {
#   source = "../../../modules/vpc"
#   region = var.region
#   env    = var.env
# }
