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
userProfile varchar(10) not null
);

insert into tbUsers(userID,userName,userPhone,userLogin,userPassword,userProfile)
values(1,'Richard Hendricks','9999998-9979','admin','admin','admin');

insert into tbUsers(userID,userName,userPhone,userLogin,userPassword,userProfile)
values(2,'Lucas Lira','31999909-7799','lucas','123456','user');

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

-- append a new field on tbusers table
-- alter table tbusers add column userProfile varchar(20) not null;

-- delete the comlumn clientCPF on tbClients table
-- alter table tbclients drop clientCPF;

-- search by a key word the content of table
-- select * from tbClients where clientName like 'l%';

-- select * from tbUsers where idUser = 1 or customer = 'admin';

alter table tbSO add serviceType varchar(15) not null after timeSO;
alter table tbSO add statusSO varchar(25) not null after serviceType;

insert into tbCustomers(customerName,customerAddress,customerPhone,customerMail)
values('Teste da Silva','teste','99999-9999','teste@teste.com');

insert into tbSO(serviceType,statusSO,product,productProblem,service,technician,price,customerID)
values('Teste 01','Withdrawal ready','teste','problem','fix it','Lucas',99.90,1);

delete from tbCustomers where customerID = 1;

select * from tbUsers;

describe tbso;
describe tbusers;
select * from tbSO;

select technician from tbSO;