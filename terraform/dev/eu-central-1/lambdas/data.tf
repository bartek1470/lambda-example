data "aws_region" "current" {}

data "terraform_remote_state" "shared" {
  backend = "s3"
  config = {
    bucket = "dev-example-terraform-state"
    key    = "lambda-example/shared/${var.region}/tf.tfstate"
    region = data.aws_region.current.name
  }
}
