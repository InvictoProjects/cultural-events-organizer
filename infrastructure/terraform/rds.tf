resource "aws_db_subnet_group" "db_subnet_group" {
  name = "postgres-db-subnet-group"
  subnet_ids = aws_subnet.private_subnets.*.id

  tags = {
    name = "cultural-events-organizer-db-subnet"
  }
}

resource "aws_db_instance" "rds_instance" {
  identifier                  = var.rds_identifier
  allocated_storage           = var.rds_allocated_storage
  storage_type                = var.rds_storage_type
  multi_az                    = false
  engine                      = var.rds_engine
  engine_version              = var.rds_engine_version
  instance_class              = var.rds_instance_type
  db_name                     = var.rds_database_name
  username                    = var.rds_username
  password                    = var.rds_password
  port                        = var.postgres_db_port
  vpc_security_group_ids      = [aws_security_group.rds_sg.id, aws_security_group.ecs_sg.id]
  parameter_group_name        = "default.postgres12"
  db_subnet_group_name        = aws_db_subnet_group.db_subnet_group.name
  publicly_accessible         = false
  allow_major_version_upgrade = false
  auto_minor_version_upgrade  = false
  apply_immediately           = true
  storage_encrypted           = false
  skip_final_snapshot         = true
  final_snapshot_identifier   = var.rds_final_snapshot_identifier

  tags = {
    name = "cultural-events-organizer-postgres-rds"
  }
}
