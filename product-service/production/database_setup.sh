#!/bin/bash

db_pass='admin123'
MYSQL_PWD=${db_pass} mysql -h 0.0.0.0 -P 3306 -u root -e "CREATE DATABASE production;CREATE USER 'admin'@'%' IDENTIFIED BY 'admin';"
MYSQL_PWD=${db_pass} mysql -h 0.0.0.0 -P 3306 -u root -e "GRANT ALL PRIVILEGES ON production.* TO 'admin'@'%';FLUSH PRIVILEGES;"

# if using docker with internal network
#docker exec -i db-mylibrary bash <<'EOF'
#  db_pass='admin123'
#  MYSQL_PWD=${db_pass} mysql -u root -e "CREATE USER 'admin'@'%' IDENTIFIED BY 'admin';CREATE DATABASE production;"
#  MYSQL_PWD=${db_pass} mysql -u root -e "GRANT ALL PRIVILEGES ON production.* TO 'admin'@'%';FLUSH PRIVILEGES;"
# its optional for restore data to database
#  MYSQL_PWD=${db_pass} mysql -u root -D mylibrary < dbsql.sql
#  exit
#EOF