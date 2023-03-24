insert into post (version, author, created_date_time, last_modified_date_time, publication_date, scope, status, text)
    values (0, 'Marek Koszałka', '2023-03-23T12:48:19','2023-03-24T12:48:19', null, 'PUBLIC', 'ACTIVE', 'POST z data.sql');

insert into post (version, author, created_date_time, last_modified_date_time, publication_date, scope, status, text)
    values (0, 'Marek Koszałka2', '2023-03-23T12:48:19','2023-03-24T12:48:19', null, 'PUBLIC', 'ACTIVE', 'POST2 z data.sql');

insert into invoice (BUYER,CREATED_DATE,LAST_MODIFIED_DATE,PAYMENT_DATE,SELLER,STATUS,VERSION)
    values ('Buyer1', '2023-03-23T12:48:19', '2023-03-23T12:48:19', '2023-03-30', 'Seller1', 'ACTIVE', 0 );

insert into invoice (BUYER,CREATED_DATE,LAST_MODIFIED_DATE,PAYMENT_DATE,SELLER,STATUS,VERSION)
    values ('Buyer2', '2023-03-23T12:48:19', '2023-03-23T12:48:19', '2023-03-29', 'Seller2', 'ACTIVE', 0 );

insert into invoice (BUYER,CREATED_DATE,LAST_MODIFIED_DATE,PAYMENT_DATE,SELLER,STATUS,VERSION)
    values ('Buyer3', '2023-03-23T12:48:19', '2023-03-23T12:48:19', '2023-03-28', 'Seller3', 'ACTIVE', 0 );

insert into invoice (BUYER,CREATED_DATE,LAST_MODIFIED_DATE,PAYMENT_DATE,SELLER,STATUS,VERSION)
    values ('Buyer4', '2023-03-23T12:48:19', '2023-03-23T12:48:19', '2023-03-28', 'Seller4', 'ACTIVE', 0 );