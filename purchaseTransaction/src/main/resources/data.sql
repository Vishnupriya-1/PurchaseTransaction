DROP TABLE IF EXISTS PURCHASETRANSACTION;
CREATE TABLE PURCHASETRANSACTION ( 
   id number(100),
   description VARCHAR2(50), 
   transaction_date DATE, 
   purchase_amount NUMBER(100,3)
);
