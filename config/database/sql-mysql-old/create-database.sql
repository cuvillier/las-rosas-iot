CREATE DATABASE lasrosasiot;

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'tibo001';
GRANT ALL PRIVILEGES ON lasrosasiot.* TO 'lasrosasiot'@'localhost' IDENTIFIED BY 'lasrosasiot';
GRANT ALL PRIVILEGES ON lasrosasiot.* TO 'lasrosasiot'@'%' IDENTIFIED BY 'lasrosasiot';
