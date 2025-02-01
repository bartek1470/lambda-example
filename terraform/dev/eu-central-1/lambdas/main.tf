module "spring" {
  source        = "../../../modules/lambda"
  name          = "spring-lambda"
  code_filepath = abspath("${path.root}/../../../../spring/build/libs/spring-0.0.1-SNAPSHOT-plain.jar")
  envs = {
    LOGBACK_APPENDER = "json",
  }
  handler          = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
  artifacts_bucket = data.terraform_remote_state.shared.outputs.artifacts_bucket
  layers = [
    module.spring_layer.arn
  ]
}

module "spring_layer" {
  source           = "../../../modules/lambda-layer"
  archive_filepath = abspath("${path.root}/../../../../spring/build/distributions/spring-layer.zip")
  artifacts_bucket = data.terraform_remote_state.shared.outputs.artifacts_bucket
  name             = "spring-layer"
  runtimes         = ["java21"]
}

module "simple_lambda" {
  source        = "../../../modules/lambda"
  name          = "simple-lambda"
  code_filepath = abspath("${path.root}/../../../../simple/build/libs/simple-1.0-SNAPSHOT.jar")
  envs = {
    LOGBACK_APPENDER = "json",
  }
  handler          = "pl.bartek.SimpleHandler"
  artifacts_bucket = data.terraform_remote_state.shared.outputs.artifacts_bucket
  layers = [
    module.simple_layer.arn
  ]
}

module "simple_layer" {
  source           = "../../../modules/lambda-layer"
  archive_filepath = abspath("${path.root}/../../../../simple/build/distributions/simple-layer.zip")
  artifacts_bucket = data.terraform_remote_state.shared.outputs.artifacts_bucket
  name             = "simple-layer"
  runtimes         = ["java21"]
}

module "native_lambda" {
  source           = "../../../modules/lambda"
  name             = "native-lambda"
  code_filepath    = abspath("${path.root}/../../../../ktnative/build/distributions/ktnative.zip")
  handler          = "myhandler"
  artifacts_bucket = data.terraform_remote_state.shared.outputs.artifacts_bucket
  runtime          = "provided.al2023"
}
