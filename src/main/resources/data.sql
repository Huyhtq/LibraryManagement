-- Thêm dữ liệu vào bảng book
INSERT INTO book (id, title, author, quantity, total) VALUES
    (1, 'Java Programming', 'John Smith', 5, 10),
    (2, 'Python Made Easy', 'Jane Doe', 3, 8),
    (3, 'Data Structures', 'Michael Brown', 4, 6),
    (4, 'System Design', 'Emily Davis', 2, 5)
ON CONFLICT (id) DO NOTHING;

-- Thêm dữ liệu vào bảng borrower
INSERT INTO borrower (id, name, email, phone, borrow_count) VALUES
    (1, 'James Wilson', 'james.wilson@example.com', '1234567890', 2),
    (2, 'Sarah Johnson', 'sarah.johnson@example.com', '0987654321', 1),
    (3, 'David Lee', 'david.lee@example.com', '1122334455', 0)
ON CONFLICT (id) DO NOTHING;

-- Thêm dữ liệu vào bảng records
INSERT INTO records (id, book_id, borrower_id, borrow_date, return_date, status) VALUES
    (1, 1, 1, '2025-05-01', '2025-05-15', 0),
    (2, 2, 2, '2025-05-03', '2025-05-10', 1),
    (3, 3, 1, '2025-05-05', '2025-05-20', 0)
ON CONFLICT (id) DO NOTHING;

-- Thêm dữ liệu vào bảng account
INSERT INTO account (id, username, password, display_name, title) VALUES
    (1, 'admin1', 'password123', 'Administrator One', 'Admin'),
    (2, 'user1', 'pass456', 'User One', 'User'),
    (3, 'user2', 'pass789', 'User Two', 'User')
ON CONFLICT (id) DO NOTHING;