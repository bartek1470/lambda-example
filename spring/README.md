# spring-lambda

## Publishing

```shell
aws-vault exec <profile> -- ./gradlew :spring:publishLambdaPublicationToS3Repository -Prelease

# or without release if working in a terraform workspace
aws-vault exec <profile> -- ./gradlew :spring:publishLambdaPublicationToS3Repository
```
