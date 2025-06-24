INSERT INTO merchants (merchant_id, name)
VALUES (1, 'Test');
INSERT INTO merchants (merchant_id, name)
VALUES (2, 'Test2');

INSERT INTO clients (client_id, first_name, last_name, merchant_id, job)
VALUES (1, 'TestFirstName', 'TestLastName', 1, 'TesterNum1');
INSERT INTO clients (client_id, first_name, last_name, merchant_id, job)
VALUES (2, 'TestName2', 'TestLsName2', 1, 'TesterNum2');