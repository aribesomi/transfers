DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
id LONG PRIMARY KEY AUTO_INCREMENT  NOT NULL,
accountID VARCHAR(128) NOT NULL,
balance DECIMAL(10,2) NOT NULL,
currency VARCHAR(3) NOT NULL
);

INSERT INTO accounts (accountID,balance,currency) VALUES ('03739274994857402953',2500.00,'USD');
INSERT INTO accounts (accountID,balance,currency) VALUES ('91664926255175179720',2000.00,'USD');
INSERT INTO accounts (accountID,balance,currency) VALUES ('49061563194923929330',2000.00,'EUR');
INSERT INTO accounts (accountID,balance,currency) VALUES ('27356239234432893258',1000.00,'EUR');
INSERT INTO accounts (accountID,balance,currency) VALUES ('67236219213623971236',1500.00,'GBP');

DROP TABLE IF EXISTS transfers;

CREATE TABLE transfers (
id LONG PRIMARY KEY AUTO_INCREMENT  NOT NULL,
idAccountFrom LONG NOT NULL,
idAccountTo LONG NOT NULL,
value DECIMAL(10,2) NOT NULL,
description VARCHAR(128),
created DATETIME NOT NULL
);

INSERT INTO transfers (idAccountFrom,idAccountTo,value,description,created) VALUES (1,2,100.00,'Test Transfer 1',NOW());
INSERT INTO transfers (idAccountFrom,idAccountTo,value,description,created) VALUES (4,3,50.00,'Test Transfer 2',NOW());
