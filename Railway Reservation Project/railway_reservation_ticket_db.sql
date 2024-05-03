show databases;
use railway_booking_db;

CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    train VARCHAR(50),
    from_city VARCHAR(50),
    to_city VARCHAR(50),
    date DATE,
    berth VARCHAR(10),
    seats INT,
    fare INT,
    total_fare INT,
    passenger_name VARCHAR(100),
    passenger_age INT,
    cnic_number VARCHAR(20)
);

select * from tickets;