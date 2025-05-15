CREATE TABLE IF NOT EXISTS book (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100),
    quantity INT,
    total INT
);

CREATE TABLE IF NOT EXISTS borrower (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(15),
    borrow_count INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS records (
    id SERIAL PRIMARY KEY,
    book_id INT REFERENCES book(id),
    borrower_id INT REFERENCES borrower(id),
    borrow_date DATE,
    return_date DATE,
    status INT
);

CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    display_name VARCHAR(50),
    title VARCHAR(50)
);