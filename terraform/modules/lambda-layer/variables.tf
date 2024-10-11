variable "artifacts_bucket" {
  type = string
}

variable "name" {
  type = string
}

variable "archive_filepath" {
  type = string
}

variable "runtimes" {
  type = list(string)
}
