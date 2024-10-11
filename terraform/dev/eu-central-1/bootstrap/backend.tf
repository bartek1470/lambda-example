terraform {
  # comment out when applying for the first time. Then uncomment and apply again
  backend "s3" {
    encrypt        = true
    bucket         = "dev-example-terraform-state"
    key            = "bootstrap/eu-central-1/tf.tfstate"
    region         = "eu-central-1"
    dynamodb_table = "dev-example-terraform-state-locks"
  }
}
