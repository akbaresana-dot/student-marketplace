CREATE DATABASE student_market;
  
USE student_market;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nama VARCHAR(100),
  nim VARCHAR(20),
  no_hp VARCHAR(20),
  role ENUM('USER','ADMIN') DEFAULT 'USER',
  status ENUM('AKTIF','NONAKTIF') DEFAULT 'AKTIF',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE listing (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  judul VARCHAR(150) NOT NULL,
  deskripsi VARCHAR(150),
  harga DECIMAL(16,0) NOT NULL,
  kategori VARCHAR(50),
  status ENUM('TERSEDIA','TERJUAL') DEFAULT 'TERSEDIA',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  INDEX idx_listing_status_created (status, created_at),
  INDEX idx_listing_kategori (kategori)
);

CREATE TABLE listing_image (
  id INT AUTO_INCREMENT PRIMARY KEY,
  listing_id INT NOT NULL,
  url VARCHAR(255) NOT NULL,
  urutan INT DEFAULT 1,
  FOREIGN KEY (listing_id) REFERENCES listing(id) ON DELETE CASCADE
);


INSERT INTO users (email, password, nama, nim, no_hp, role, status) VALUES
('admin@student.ac.id', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Administrator', '', '', 'ADMIN', 'AKTIF');
