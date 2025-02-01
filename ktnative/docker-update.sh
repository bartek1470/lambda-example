#!/usr/bin/env bash

if [ -z "${ECR_REPO}" ]; then
  echo "ECR_REPO is not defined"
  exit 1
fi

docker buildx build --platform linux/amd64 --provenance=false -f ../ktnative/Dockerfile -t "${ECR_REPO}:latest" ../

#aws ecr get-login-password \
#  --region us-east-1 \
#  | docker login \
#    --username AWS \
#    --password-stdin "${ECR_REPO}"
#
#docker push "${ECR_REPO}:latest"
