# output "vpc_id" {
#   value = module.vpc.vpc_id
# }
#
# output "private_subnets" {
#   value = module.vpc.private_subnet_ids
# }

output "artifacts_bucket" {
  value = module.artifacts_bucket.artifacts_bucket
}

output "ktnative_repo_url" {
  value = module.ktnative_ecr.repo_url
}
