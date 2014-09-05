CREATE USER testDB IDENTIFIED BY testPass;

grant create session, grant any privilege to testDB;

GRANT UNLIMITED TABLESPACE TO testdb;

