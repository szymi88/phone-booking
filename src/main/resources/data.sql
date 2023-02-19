CREATE TABLE PHONES (
   ID INT PRIMARY KEY,
   PHONE_BRAND VARCHAR(50) NOT NULL,
   PHONE_MODEL VARCHAR(50) NOT NULL,
   STOCK_COUNT INT NOT NULL
);

INSERT INTO PHONES VALUES (1, 'Samsung', 'Galaxy S9', 1);
INSERT INTO PHONES VALUES (2, 'Samsung', 'Galaxy S8', 2);
INSERT INTO PHONES VALUES (3, 'Motorola', 'Nexus 6', 1);
INSERT INTO PHONES VALUES (4, 'Oneplus', '9', 1);
INSERT INTO PHONES VALUES (5, 'Apple', 'iPhone 13', 1);
INSERT INTO PHONES VALUES (6, 'Apple', 'iPhone 12', 1);
INSERT INTO PHONES VALUES (7, 'Apple', 'iPhone 11', 1);
INSERT INTO PHONES VALUES (8, 'iPhone', 'X', 1);
INSERT INTO PHONES VALUES (9, 'Nokia', '3310', 1);