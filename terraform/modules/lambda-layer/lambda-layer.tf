resource "aws_lambda_layer_version" "this" {
  layer_name          = var.name
  s3_bucket           = var.artifacts_bucket
  s3_key              = aws_s3_object.lambda_layer_archive.key
  source_code_hash    = aws_s3_object.lambda_layer_archive.metadata.hash
  compatible_runtimes = var.runtimes
  skip_destroy        = true
}

resource "aws_s3_object" "lambda_layer_archive" {
  bucket      = var.artifacts_bucket
  key         = basename(var.archive_filepath)
  source      = var.archive_filepath
  source_hash = filebase64sha256(var.archive_filepath)
  metadata = {
    hash = filebase64sha256(var.archive_filepath)
  }
}
