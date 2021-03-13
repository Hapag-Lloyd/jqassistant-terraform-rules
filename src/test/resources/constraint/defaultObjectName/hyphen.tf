provider "aws" {
  region = "eu-central-1"
}

resource "aws_db_instance" "valid_object_name" {
  instance_class = "t3.medium"
}

resource "aws_db_instance" "name-with-hyphen" {
  instance_class = "t3.medium"
}