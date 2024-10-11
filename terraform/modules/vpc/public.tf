resource "aws_subnet" "public_subnet_1" {
  vpc_id            = aws_vpc.this.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "${var.region}a"

  tags = {
    Tier = "public"
  }
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.this.id

  tags = {
    Name = "${var.env}-example-igw"
  }
}

resource "aws_route_table" "internet_route" {
  vpc_id = aws_vpc.this.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "route to internet"
  }
}

resource "aws_route_table_association" "public_subnet_1_association" {
  subnet_id      = aws_subnet.public_subnet_1.id
  route_table_id = aws_route_table.internet_route.id
}

resource "aws_eip" "this" {
  depends_on = [aws_internet_gateway.igw]
  tags = {
    Name = "${var.env}-example-eip"
  }
}

resource "aws_nat_gateway" "this" {
  allocation_id = aws_eip.this.id
  subnet_id     = aws_subnet.public_subnet_1.id

  tags = {
    Name = "${var.env}-example-nat-gateway"
  }
}
