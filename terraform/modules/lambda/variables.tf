variable "artifacts_bucket" {
  type = string
}

variable "name" {
  type = string
}

variable "code_filepath" {
  type = string
}

variable "envs" {
  type    = map(string)
  default = {}
}

variable "runtime" {
  type    = string
  default = "java21"
}

variable "handler" {
  type = string
}

variable "timeout" {
  type    = number
  default = 30
}

variable "memory" {
  type    = number
  default = 1024
}
variable "layers" {
  type    = list(string)
  default = []
}

variable "logs_retention_in_days" {
  type    = number
  default = 14
}

variable "subnet_ids" {
  type    = list(string)
  default = []
}

variable "security_group_ids" {
  type    = list(string)
  default = []
}
