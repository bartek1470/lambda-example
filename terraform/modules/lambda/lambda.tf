resource "aws_lambda_function" "this" {
  function_name    = var.name
  runtime          = var.runtime
  handler          = var.handler
  timeout          = var.timeout
  s3_bucket        = aws_s3_object.lambda_code.bucket
  s3_key           = aws_s3_object.lambda_code.key
  source_code_hash = aws_s3_object.lambda_code.source_hash
  role             = aws_iam_role.lambda_exec_role.arn
  memory_size      = var.memory
  layers           = var.layers

  logging_config {
    log_format = "JSON"
    log_group  = aws_cloudwatch_log_group.simple_lambda_log_group.name
  }

  tracing_config {
    mode = "Active"
  }

  environment {
    variables = var.envs
  }

  vpc_config {
    security_group_ids = var.security_group_ids
    subnet_ids         = var.subnet_ids
  }
}

resource "aws_s3_object" "lambda_code" {
  bucket      = var.artifacts_bucket
  key         = basename(var.code_filepath)
  source      = var.code_filepath
  source_hash = filebase64sha256(var.code_filepath)
  metadata = {
    hash = filebase64sha256(var.code_filepath)
  }
}

resource "aws_cloudwatch_log_group" "simple_lambda_log_group" {
  name              = "/aws/lambda/${var.name}"
  retention_in_days = var.logs_retention_in_days
  skip_destroy      = false
}

