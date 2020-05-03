-- create a database
create database dbInfoX;
-- choose the db to be using
use dbInfoX;
-- create a user table
create table tbUsers(
-- type int, primary key is first data received
userID int primary key,
-- varchar looks like String, not null force to fill the field
userName varchar(50) not null,
userPhone varchar(14),
-- unique prevents same logins datas
userLogin varchar(15) not null unique,
userPassword varchar(15) not null,
userProfile varchar(20) not null
);

insert into tbUsers(userID,userName,userPhone,userLogin,userPassword,userProfile)
values(1,'Richard Hendricks','9999998-9979','admin','admin','admin');

create table tbCustomers(
customerID int primary key auto_increment,
customerName varchar(50) not null,
customerAddress varchar(100),
customerPhone varchar(50) not null,
customerMail varchar(50)
);

create table tbSO(
so int primary key auto_increment,
timeSO timestamp default current_timestamp,
serviceType varchar(15) not null,
statusSO varchar(25) not null,
product varchar(150) not null,
productProblem varchar(150) not null,
service varchar(150),
technician varchar(50),
price decimal(10,2),
customerID int not null,
-- foreign key brings the other table reference
-- in this case, brings the client data by ID
foreign key(customerID) references tbcustomers(customerID)
);

-- Bring information about two tables
select 
-- variable 'o' receive a choosed columns of table OS
o.so,product,service,price,
-- variable 'c' receive a choosed columns of table OS
c.customerName,customerPhone
-- import the values of table OS from 'o'
from tbSO as o
-- makes a junction of two tables at 'c'
inner join tbCustomers as c
-- assign 'idClient' of o as the same 'idClient' of c
on (o.customerID = c.customerID);

-- search by a key word the content of table
-- select * from tbClients where clientName like 'l%';

select * from tbUsers;
select * from tbCustomers;
select * from tbSO;
