
CREATE TABLE IF NOT EXISTS accounts (
account_number INT PRIMARY KEY AUTO_INCREMENT,
recipient_name VARCHAR(255) NOT NULL,
pin VARCHAR(4) NOT NULL,
balance DOUBLE CHECK (balance >= 0)
);

CREATE SEQUENCE "INCREMENT"
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 4
    NOCACHE
NOCYCLE;

CREATE TABLE IF NOT EXISTS transactions (
transaction_id INT PRIMARY KEY AUTO_INCREMENT,
account_number INT NOT NULL,
type VARCHAR(255) NOT NULL,
amount DOUBLE NOT NULL,
timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_transactions_accounts FOREIGN KEY (account_number) REFERENCES accounts (account_number)
);

CREATE SEQUENCE "INCREMENT1"
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 4
    NOCACHE
NOCYCLE;

INSERT INTO accounts (recipient_name, pin, balance)
VALUES ('John Doe', '1234', 1000.0),
       ('Jane Smith', '5678', 500.0),
       ('Alice Johnson', '9012', 1500.0);

INSERT INTO transactions (account_number, type, amount)
VALUES (1, 'DEPOSIT', 1000.0),
       (2, 'DEPOSIT', 500.0),
       (3, 'DEPOSIT', 1500.0);

