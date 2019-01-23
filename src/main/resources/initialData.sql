DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
id LONG PRIMARY KEY AUTO_INCREMENT  NOT NULL,
accountID VARCHAR(128),
balance DECIMAL(10,2),
currency VARCHAR(3)
);

INSERT INTO accounts (accountID,balance,currency) VALUES ('03739274994857402953',25000.00,'USD');
INSERT INTO accounts (accountID,balance,currency) VALUES ('91664926255175179720',2000.00,'USD');
INSERT INTO accounts (accountID,balance,currency) VALUES ('49061563194923929330',5000.00,'EUR');

DROP TABLE IF EXISTS transfers;

CREATE TABLE transfers (
id LONG PRIMARY KEY AUTO_INCREMENT  NOT NULL,
idAccountFrom LONG,
idAccountTo LONG,
value DECIMAL(10,2),
description VARCHAR(128),
created DATETIME
);