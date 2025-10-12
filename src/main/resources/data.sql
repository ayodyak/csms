-- Insert sample products
INSERT INTO products (products_id, name, description, price, stock_quantity) VALUES
(1, 'Intel Core i7 Processor', 'High-performance CPU', 299.99, 50),
(2, 'NVIDIA RTX 3080', 'Graphics Card', 699.99, 30),
(3, 'Samsung 1TB SSD', 'Solid State Drive', 129.99, 100);

-- Insert sample suppliers
INSERT INTO suppliers (supplier_id, name, contact_person, email, phone, address) VALUES
(1, 'TechPro Supplies', 'John Smith', 'john@techpro.com', '+1234567890', '123 Tech Street'),
(2, 'Computer Parts Inc', 'Jane Doe', 'jane@cpi.com', '+0987654321', '456 Hardware Ave'),
(3, 'Digital Solutions', 'Mike Johnson', 'mike@digitalsol.com', '+1122334455', '789 Digital Road');

-- Insert sample purchase requests
INSERT INTO purchase_requests (request_id, products_id, supplier_id, quantity, status, request_date) VALUES
(1, 1, 1, 20, 'Pending', CURRENT_DATE()),
(2, 2, 2, 15, 'Accepted', CURRENT_DATE()),
(3, 3, 1, 50, 'Pending', CURRENT_DATE()),
(4, 2, 3, 10, 'Received', CURRENT_DATE()),
(5, 1, 2, 25, 'Pending', CURRENT_DATE());
