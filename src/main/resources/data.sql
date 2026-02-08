-- Person base row
INSERT INTO persons (id, name, gender, age, identification, address, phone)
VALUES (1, 'Daniela', 'F', 30, '123456', 'Quito', '099999');

-- Customer row (same id, links to persons)
INSERT INTO customers (id, customer_id, password, status)
VALUES (1, 'C001', '1234', true);

-- Account row
INSERT INTO accounts (id, account_number, account_type, initial_balance, status, customer_id)
VALUES (1, 'ACC-001', 'SAVINGS', 500.00, true, 1);
