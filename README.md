MySQL Setup/Install
1) Install MySQL
   1) Run "mysql_secure_installation" to set root pw
   2) Run "brew services start mysql" to run mysql and have it run on startup
   3) Run "mysql -u root -p <password>"
2) Setup MySQL
   1) Run "create database stocksimulator;" in mysql
   2) Run "use stocksimulator"
3) Install Docker Desktop
   1) Get MySQL latest image
   2) Run "docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=<ROOT_PW> -d mysql"