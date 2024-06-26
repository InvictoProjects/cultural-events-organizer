resource "aws_security_group" "public-sg" {
  name = "public-group-default"
  description = "access to public instances"
  vpc_id = aws_vpc.vpc.id
}

resource "aws_security_group" "alb_sg" {
  name = "alb-group"
  description = "control access to the application load balancer"
  vpc_id = aws_vpc.vpc.id

  ingress {
    from_port = 80
    to_port = 80
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ecs_sg" {
  name = "ecs-from-alb-group"
  description = "control access to the ecs cluster"
  vpc_id = aws_vpc.vpc.id

  ingress {
    from_port = var.ems_service_port
    to_port = var.ems_service_port
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    protocol = "-1"
    from_port = 0
    to_port = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "rds_sg" {
  name = "postgres-public-group"
  description = "access to public rds instances"
  vpc_id = aws_vpc.vpc.id

  ingress {
    protocol = "tcp"
    from_port = var.postgres_db_port
    to_port = var.postgres_db_port
    cidr_blocks = ["0.0.0.0/0"]
    security_groups = [
      aws_security_group.alb_sg.id,
      aws_security_group.lambda_sg.id
    ]
  }

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "lambda_sg" {
  name        = "lambda-group"
  description = "access for the Lambda function to RDS instances"
  vpc_id      = aws_vpc.vpc.id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
