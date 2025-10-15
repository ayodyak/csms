-- V1.1__supplier_schema.sql
-- Supplier-related tables

-- Supplier table
CREATE TABLE supplier (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(500),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Supplier payments table
CREATE TABLE supplier_payments (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    supplier_id BIGINT,
    amount DECIMAL(18,2) NOT NULL,
    due_date DATETIME NOT NULL,
    payment_date DATETIME NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE'
    payment_method VARCHAR(50) NOT NULL,
    invoice_number VARCHAR(100),
    notes VARCHAR(500),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

-- Supplier performance tracking table
CREATE TABLE supplier_performance (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    supplier_id BIGINT,
    delivery_date DATETIME,
    expected_delivery_date DATETIME,
    delivery_cost DECIMAL(10,2),
    quality_rating INT,
    on_time_delivery BIT,
    comments VARCHAR(500),
    evaluation_date DATETIME,
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);
