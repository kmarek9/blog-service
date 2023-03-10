CREATE TABLE POST(ID INT PRIMARY KEY, TEXT VARCHAR(255));

//C
INSERT INTO POST VALUES(4, 'Hello3', 'PRIVATE');

//R
select text, id from post where id=4

//U
update POST set text='Nowy tekst' ,scope='PRIVATE' where id=4


//D
delete from post where id=3


//List
select * from POST where scope ='PRIVATE' and text like '%N%'
