variable "region" { type = string }
variable "vpc_cidr" { type = string default = "10.0.0.0/16" }
variable "azs" { type = list(string) }
variable "private_subnets" { type = list(string) }
variable "public_subnets" { type = list(string) }

variable "db_engine_version" { type = string default = "15.5" }
variable "db_instance_class" { type = string default = "db.t3.micro" }
variable "db_name" { type = string default = "bolsas" }
variable "db_username" { type = string default = "bolsas" }
