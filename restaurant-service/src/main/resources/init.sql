CREATE TABLE dish (
                      dish_id INT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(100) NOT NULL,
                      description TEXT,
                      price DECIMAL(10, 2) NOT NULL,
                      quantity INT NOT NULL,
                      is_available BOOLEAN NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP()
);
CREATE TABLE orders (
                       order_id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT NOT NULL,
                       status VARCHAR(50) NOT NULL,
                       special_requests TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP()
);

CREATE TABLE order_dish (
                            order_dish_id INT AUTO_INCREMENT PRIMARY KEY,
                            order_id INT NOT NULL,
                            dish_id INT NOT NULL,
                            quantity INT NOT NULL,
                            price DECIMAL(10, 2) NOT NULL,
                            FOREIGN KEY (order_id) REFERENCES orders(order_id),
                            FOREIGN KEY (dish_id) REFERENCES dish(dish_id)
);

INSERT INTO dish (title, description, price, quantity, is_available)
VALUES ('Хачапури по аджарски', '', 450, 10, true),
       ('Лаваш армянский', '', 50, 50, true),
       ('Лаваш грузинский', '', 50, 30, true),
       ('Шаурма с курицей', '', 230, 30, true);
