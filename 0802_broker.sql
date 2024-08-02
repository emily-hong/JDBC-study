DESC customer;
DESC stock;
DESC shares;

-- ALTER TABLE shares DROP PRIMARY KEY;


insert into stock ( symbol,price) values( 'SUNW', 68.75);
insert into stock ( symbol,price) values( 'CyAs', 22.675);
insert into stock ( symbol,price) values( 'DUKE', 6.25);
insert into stock ( symbol,price) values( 'ABStk', 18.5);
insert into stock ( symbol,price) values( 'JSVco', 9.125);
insert into stock ( symbol,price) values( 'TMAs', 82.375);
insert into stock ( symbol,price) values( 'BWInc', 11.375);
insert into stock ( symbol,price) values( 'GMEnt', 44.625);
insert into stock ( symbol,price) values( 'PMLtd', 203.375);
insert into stock ( symbol,price) values( 'JDK', 33.5);
insert into customer values( '111-111', 'Yufirst1', 'Seoul');
insert into customer values( '111-112', 'Yufirst2', 'Seoul');
insert into customer values( '111-113', 'Yufirst3', 'Seoul');
insert into customer values( '111-114', 'Yufirst4', 'Seoul');
insert into customer values( '111-115', 'yufirst5', 'JeonJu');
insert into customer values( '111-116', 'Yufirst6', 'Seoul');
insert into customer values( '111-117', 'Yufirst7', 'Seoul');
insert into customer values( '111-118', 'Yufirst8', 'Seoul');
insert into customer values( '111-119', 'Yufirst9', 'Seoul');

commit;

ALTER TABLE shares ADD CONSTRAINT fk_shares_ssn foreign key(ssn) references customer(ssn) ON DELETE CASCADE;
ALTER TABLE shares ADD CONSTRAINT fk_shares_symbol foreign key(symbol) references stock(symbol) ON DELETE CASCADE;

-- ------
select * from customer;
select * from stock;
select * from shares;
-- ------
INSERT INTO shares (ssn, symbol, quantity) VALUES('111-111', 'JDK', 75);
INSERT INTO shares (ssn, symbol, quantity) VALUES('111-111', 'DUKE', 10);

-- ------
SELECT symbol, ssn, quantity From shares WHERE ssn='111-119';

SELECT symbol, ssn, quantity From shares WHERE ssn='111-111';

SELECT ssn, cust_name, address From customer WHERE ssn='111-111';



















