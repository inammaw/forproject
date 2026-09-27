-- =======================================================
-- StuRent / CampusMart Database Schema
-- Aiven MySQL Database: defaultdb
-- =======================================================

-- 1. Cart Table
CREATE TABLE IF NOT EXISTS cart (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DOUBLE NOT NULL
);

-- 2. Orders Table
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount DOUBLE NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    delivery_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Order Items Table
CREATE TABLE IF NOT EXISTS order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- 4. Messages Table
CREATE TABLE IF NOT EXISTS messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Reviews Table
CREATE TABLE IF NOT EXISTS reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    reviewer_id INT NOT NULL,
    seller_id INT NOT NULL,
    item_id INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Marketplace Items Table
CREATE TABLE IF NOT EXISTS items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    sale_price DOUBLE DEFAULT 0,
    rent_price DOUBLE DEFAULT 0,
    status VARCHAR(50) DEFAULT 'AVAILABLE',
    seller_id VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =======================================================
-- Sample Seed Data
-- =======================================================

-- Seed Items (matching screenshots)
INSERT IGNORE INTO items (item_id, title, type, sale_price, rent_price, status, seller_id, description) VALUES
(1, 'test5', 'SALE', 11111.0, 0, 'AVAILABLE', 'TEST123', 'test5 dumbbell set in mint condition'),
(2, 'test test', 'SALE', 11111.0, 0, 'AVAILABLE', 'CAMPUS_SELLER', 'Campus electronics and study kit'),
(3, 'useless human', 'SALE_AND_RENT', 10.0, 11.0, 'AVAILABLE', 'STUDENT_99', 'Funny novelty student desk bobblehead');

-- Seed Cart Item for User 1
INSERT IGNORE INTO cart (cart_item_id, user_id, item_id, quantity, unit_price) VALUES
(1, 1, 1, 1, 11111.0);
