
//ZADANIE DOMOWE
//zalozyc tabele Invoice,ktora ma pole id INT , invoiceNumber vARCHAR(255)

CREATE TABLE INVOICE(ID INT PRIMARY KEY, NUMBER VARCHAR(255));

//C
INSERT INTO INVOICE values(1, 'Invoice1');
INSERT INTO INVOICE values(2, 'Invoice1');
INSERT INTO INVOICE values(3, 'Invoice1');
INSERT INTO INVOICE values(4, 'Invoice1');
INSERT INTO INVOICE values(5, 'Invoice1');


//R
select * from INVOICE where id=4

//U
update invoice set number =null
where id=4


//D
delete from invoice where id=3


//List
select * from invoice where number like '%23%' and (id=1 or id= 3 or id=4 );
select * from invoice where number like '%23%' and id in (1, 3 ,4)


