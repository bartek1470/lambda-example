terraform {
  backend "s3" {
    encrypt        = true
    bucket         = "dev-example-terraform-state"
    key            = "lambda-example/shared/eu-central-1/tf.tfstate"
    region         = "eu-central-1"
    dynamodb_table = "dev-example-terraform-state-locks"
  }
}
