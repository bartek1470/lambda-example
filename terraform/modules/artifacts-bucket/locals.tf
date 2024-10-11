locals {
  archives_bucket_name = terraform.workspace == "default" ? "${var.env}-example-artifacts" : "${terraform.workspace}-${var.env}-example-artifacts"
}
