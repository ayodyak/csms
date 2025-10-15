-- Create supplier_payments table
CREATE TABLE supplier_payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    due_date TIMESTAMP NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    invoice_number VARCHAR(100),
    notes VARCHAR(255),
    CONSTRAINT fk_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

