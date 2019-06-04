drop user dealer cascade;

/* Drop can remove the user "object" or "entity" from the system.
    We want to run this script on the dba user, so SYSTEM or your ADMIN user
        We don't want our schema getting built on our dba user.
*/
-- create a user in the database
create user dealer
identified by pass
default tablespace users
temporary tablespace temp
quota 10m on users;

-- we need to be able to connect to another user from bookapp
grant connect to dealer;
-- we want the ability to create types
grant resource to dealer;
-- we don't want the ability to alter and destroy types
-- grant dba to bookapp;
-- We do want the ability to create a session for transactions
grant create session to dealer;
-- self explanatory
grant create table to dealer;
grant create view to dealer;

conn dealer/pass

drop table dealerusers;

drop table offers;
drop table lot;
drop table payments;
drop table owned;

create table dealerusers(
    userid number not null,
    username VARCHAR2(20),
    pass varchar2(20),
    usertype varchar2(20)
);

insert into dealerusers (userid, username, pass, usertype) values (1234, 'admin', 'pass', 'employee');
insert into dealerusers (userid, username, pass, usertype) values (1234, 'djiang', 'pass', 'customer');
select * from dealerusers;

commit;

select * from dealerusers where username='admin' and pass='pass';

create table lot(
    carid number not null,
    caryear number not null,
    carmake varchar(20),
    carmodel varchar(20),
    mileage varchar(20),
    color varchar(20)
);

insert into lot (carid, caryear, carmake, carmodel, mileage, color) values (1234, 2007, 'Dodge', 'Charger', '70000', 'Black');
insert into lot (carid, caryear, carmake, carmodel, mileage, color) values (1235, 2019, 'Tesla', 'Model X', '10000', 'White');

create table offers(
    carid number,
    userid number,
    leninmonths number,
    totalprice number,
    rejectoraccept number
);

insert into offers(carid, userid, leninmonths, totalprice, rejectoraccept) values(1234, 8008, 12, 7000, 0);
insert into offers(carid, userid, leninmonths, totalprice, rejectoraccept) values(1234, 1010, 12, 9000, 0);
select * from offers;

create table payments(
    carid number not null,
    userid number not null,
    amountpaid number not null,
    leninmonths number not null,
    totalprice number not null,
    thispayment number not null
);

insert into payments (carid, userid, amountpaid, leninmonths, totalprice, thispayment) values(1235, 1235, 12000, 24, 24000, 0);

create table owned(
    carid number not null,
    userid number not null,
    totalprice number not null,
    paid number not null,
    debt number not null
);
