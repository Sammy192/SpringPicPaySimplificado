--User COMOON que pode realizar transactions
INSERT INTO users(first_name, last_name, document, email, password, balance, user_type) VALUES ('Mateus', 'Silva', '1234999222', 'mateus@example.com', '123456', '100', 'COMMON');
INSERT INTO users(first_name, last_name, document, email, password, balance, user_type) VALUES ('Maria', 'Bezerra', '123456789', 'maria@example.com', '123456', '200', 'COMMON');

--User type MERCHANT - que nao pode fazer transactions
INSERT INTO users(first_name, last_name, document, email, password, balance, user_type) VALUES ('Jao', 'Maroto', '123488444', 'jao@example.com', '123456', '300', 'MERCHANT');

-- Inserção de Transações
INSERT INTO transactions(amount, sender_id, receiver_id, timestamp) VALUES (100.00, 1, 2, TIMESTAMP WITH TIME ZONE '2024-07-06T15:00:00Z');
INSERT INTO transactions(amount, sender_id, receiver_id, timestamp) VALUES (200.00, 1, 2, TIMESTAMP WITH TIME ZONE '2024-07-06T16:00:00Z');