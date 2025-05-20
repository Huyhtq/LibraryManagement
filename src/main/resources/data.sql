-- Thêm dữ liệu vào bảng book 
INSERT INTO book (id, title, author, quantity, total) VALUES
    (1, 'Java Programming', 'John Smith', 10, 10),
    (2, 'Python Made Easy', 'Jane Doe', 10, 10),
    (3, 'Data Structures', 'Michael Brown', 10, 10),
    (4, 'System Design', 'Emily Davis', 10, 10),
    (5, 'Web Development', 'Robert Johnson', 10, 10),
    (6, 'Machine Learning', 'Sarah Williams', 10, 10),
    (7, 'Database Basics', 'Thomas Lee', 10, 10),
    (8, 'Algorithms', 'Laura Miller', 10, 10),
    (9, 'Cloud Computing', 'David Clark', 10, 10),
    (10, 'Software Engineering', 'Anna Taylor', 10, 10)
ON CONFLICT (id) DO NOTHING;

-- Thêm dữ liệu vào bảng borrower 
INSERT INTO borrower (id, name, email, phone, borrow_count) VALUES
    (1, 'James Wilson', 'james.wilson@example.com', '1234567890', 0),
    (2, 'Sarah Johnson', 'sarah.johnson@example.com', '0987654321', 00),
    (3, 'David Lee', 'david.lee@example.com', '1122334455', 0),
    (4, 'Emily Brown', 'emily.brown@example.com', '2233445566', 0),
    (5, 'Michael Davis', 'michael.davis@example.com', '3344556677', 0),
    (6, 'Lisa Anderson', 'lisa.anderson@example.com', '4455667788', 0),
    (7, 'Robert Taylor', 'robert.taylor@example.com', '5566778899', 0),
    (8, 'Jennifer White', 'jennifer.white@example.com', '6677889900', 0),
    (9, 'Thomas Clark', 'thomas.clark@example.com', '7788990011', 0),
    (10, 'Patricia Moore', 'patricia.moore@example.com', '8899001122', 0)
ON CONFLICT (id) DO NOTHING;

-- Thêm dữ liệu vào bảng account 
INSERT INTO account (id, username, password, display_name, title) VALUES
    (1, 'admin1', 'password123', 'Administrator One', 'Admin'),
    (2, 'user1', 'pass456', 'User One', 'User'),
    (3, 'user2', 'pass789', 'User Two', 'User'),
    (4, 'admin2', 'admin789', 'Administrator Two', 'Admin'),
    (5, 'user3', 'user123', 'User Three', 'User')
ON CONFLICT (id) DO NOTHING;