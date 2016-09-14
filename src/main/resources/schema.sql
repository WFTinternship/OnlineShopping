CREATE TABLE addresses (
	address_id INTEGER  NOT NULL,
	shipping_address VARCHAR(100) NOT NULL,
	user_id INTEGER NOT NULL
);


CREATE TABLE baskets (
	basket_id INTEGER  NOT NULL,
	total_price DOUBLE NOT NULL,
	user_id INTEGER NOT NULL,
	basket_status VARCHAR(250)
);
CREATE TABLE categories (
	category_id INTEGER  NOT NULL,
	category_name VARCHAR(20) NOT NULL,
	parent_id INTEGER NOT NULL
);
CREATE TABLE creditcards (
	card_id INTEGER  NOT NULL,
	billing_address VARCHAR(45) NOT NULL,
	balance DOUBLE NOT NULL
);
CREATE TABLE medias (
	media_id INTEGER  NOT NULL,
	media_path VARCHAR(250) NOT NULL,
	product_id INTEGER NOT NULL
);
CREATE TABLE orderitems (
	orderitem_id INTEGER  NOT NULL,
	basket_id INTEGER NOT NULL,
	product_id INTEGER NOT NULL,
	quantity INTEGER NOT NULL,
	size_option VARCHAR(10) NOT NULL
);
CREATE TABLE product_size (
	id INTEGER  NOT NULL,
	product_id INTEGER NOT NULL,
	size_option VARCHAR(10) NOT NULL,
	quantity INTEGER NOT NULL
);
CREATE TABLE products (
	product_id INTEGER  NOT NULL,
	name VARCHAR(45) NOT NULL,
	price DOUBLE NOT NULL,
	description VARCHAR(65535) NOT NULL,
	shipping_price DOUBLE NOT NULL,
	category_id INTEGER NOT NULL
);

CREATE TABLE sales (
	sale_id INTEGER  NOT NULL,
	user_id INTEGER NOT NULL,
	date_of_purchuase TIMESTAMP NOT NULL,
	card_id INTEGER NOT NULL,
	address_id INTEGER NOT NULL,
	basket_id INTEGER NOT NULL
);
CREATE TABLE sizes (
	id INTEGER  NOT NULL,
	category_id INTEGER NOT NULL,
	size_option VARCHAR(10) NOT NULL
);
CREATE TABLE users (
	user_id INTEGER  NOT NULL,
	firstname VARCHAR(20) NOT NULL,
	lastname VARCHAR(20) NOT NULL,
	username VARCHAR(20) NOT NULL,
	password VARCHAR(65) NOT NULL,
	phone VARCHAR(15),
	address VARCHAR(45),
	email VARCHAR(40) NOT NULL,
	confirmation_status INTEGER NOT NULL,
	access_privilege VARCHAR(250) NOT NULL
);
CREATE TABLE wishlist (
	user_id INTEGER  NOT NULL,
	product_id INTEGER NOT NULL
);


INSERT INTO baskets(basket_id, total_price, user_id, basket_status) VALUES (22, 142, 53, 'current');
INSERT INTO categories(category_id, category_name, parent_id) VALUES (16, 'BABY', 0);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (2, 'BABY BOY', 16);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (1, 'BABY GIRL', 16);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (8, 'bottoms', 1);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (10, 'bottoms', 2);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (9, 'bottoms', 3);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (11, 'bottoms', 4);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (4, 'BOY', 17);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (6, 'dresses', 1);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (7, 'dresses', 3);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (15, 'dresses', 5);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (3, 'GIRL', 17);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (17, 'GIRL/BOY', 0);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (19, 'NEUTRAL', 0);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (5, 'NEUTRAL', 19);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (18, 'SHOES', 0);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (12, 'tops', 2);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (13, 'tops', 4);
INSERT INTO categories(category_id, category_name, parent_id) VALUES (14, 'tops', 5);
INSERT INTO creditcards(card_id, billing_address, balance) VALUES (5, 'some billing address', 1000);
INSERT INTO medias(media_id, media_path, product_id) VALUES (1, '/resources/image/girldress11.jpg', 1);
INSERT INTO medias(media_id, media_path, product_id) VALUES (2, '/resources/image/girldress12.jpg', 1);
INSERT INTO medias(media_id, media_path, product_id) VALUES (5, '/resources/image/31.jpg', 3);
INSERT INTO medias(media_id, media_path, product_id) VALUES (6, '/resources/image/32.jpg', 3);
INSERT INTO medias(media_id, media_path, product_id) VALUES (7, '/resources/image/41.jpg', 4);
INSERT INTO medias(media_id, media_path, product_id) VALUES (8, '/resources/image/42.jpg', 4);
INSERT INTO medias(media_id, media_path, product_id) VALUES (9, '/resources/image/61.jpg', 5);
INSERT INTO medias(media_id, media_path, product_id) VALUES (10, '/resources/image/62.jpg', 5);
INSERT INTO orderitems(orderitem_id, basket_id, product_id, quantity, size_option) VALUES (1, 22, 1, 5, '3M');
INSERT INTO orderitems(orderitem_id, basket_id, product_id, quantity, size_option) VALUES (2, 22, 1, 2, 'NB');
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (1, 1, 'NB', 5);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (2, 1, '3M', 7);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (3, 1, '6M', 5);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (4, 1, '9M', 8);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (5, 1, '12M', 5);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (6, 1, '24M', 5);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (34, 18, '3M', 10);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (35, 18, '9M', 1);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (36, 20, '18M', 10);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (37, 20, '12M', 1);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (38, 21, '3M', 12);
INSERT INTO product_size(id, product_id, size_option, quantity) VALUES (39, 21, '12M', 1);
INSERT INTO products(product_id, name, price, description, shipping_price, category_id) VALUES (1, '2-piece dress', 20, 'desc', 1, 6);
INSERT INTO products(product_id, name, price, description, shipping_price, category_id) VALUES (3, '2-piece dress', 20, 'desc', 1, 6);
INSERT INTO products(product_id, name, price, description, shipping_price, category_id) VALUES (4, '2-piece dress', 30, 'desc', 1, 6);
INSERT INTO products(product_id, name, price, description, shipping_price, category_id) VALUES (5, ' 2-piece dress', 40, ' pink', 1, 6);
INSERT INTO sizes(id, category_id, size_option) VALUES (5, 16, '12M');
INSERT INTO sizes(id, category_id, size_option) VALUES (24, 16, '18M');
INSERT INTO sizes(id, category_id, size_option) VALUES (6, 16, '24M');
INSERT INTO sizes(id, category_id, size_option) VALUES (2, 16, '3M');
INSERT INTO sizes(id, category_id, size_option) VALUES (3, 16, '6M');
INSERT INTO sizes(id, category_id, size_option) VALUES (4, 16, '9M');
INSERT INTO sizes(id, category_id, size_option) VALUES (1, 16, 'NB');
INSERT INTO sizes(id, category_id, size_option) VALUES (7, 17, '2Y');
INSERT INTO sizes(id, category_id, size_option) VALUES (8, 17, '3Y');
INSERT INTO sizes(id, category_id, size_option) VALUES (10, 17, '4Y');
INSERT INTO sizes(id, category_id, size_option) VALUES (11, 17, '5Y');
INSERT INTO sizes(id, category_id, size_option) VALUES (12, 17, '6Y');
INSERT INTO sizes(id, category_id, size_option) VALUES (13, 17, '7Y');
INSERT INTO sizes(id, category_id, size_option) VALUES (20, 18, '10');
INSERT INTO sizes(id, category_id, size_option) VALUES (14, 18, '4');
INSERT INTO sizes(id, category_id, size_option) VALUES (15, 18, '5');
INSERT INTO sizes(id, category_id, size_option) VALUES (16, 18, '6');
INSERT INTO sizes(id, category_id, size_option) VALUES (17, 18, '7');
INSERT INTO sizes(id, category_id, size_option) VALUES (18, 18, '8');
INSERT INTO sizes(id, category_id, size_option) VALUES (19, 18, '9');
INSERT INTO sizes(id, category_id, size_option) VALUES (22, 19, '3M');
INSERT INTO sizes(id, category_id, size_option) VALUES (23, 19, '6M');
INSERT INTO sizes(id, category_id, size_option) VALUES (21, 19, 'NB');
INSERT INTO users(user_id, firstname, lastname, username, password, phone, address, email, confirmation_status, access_privilege) VALUES (51, 'Gago', 'Gagoyan', 'gago', 'df1f5dbcf7096dd16ed8273bfe302ad2e4df2708', null, null, 'aaa@aaa.com', 1, 'user');
INSERT INTO users(user_id, firstname, lastname, username, password, phone, address, email, confirmation_status, access_privilege) VALUES (53, 'Anna', 'Asmangulyan', 'annaasm', 'c8db409786bd78a234cbc9178b3f1aec6a3ef8b1', null, null, 'asmangulyananna@gmail.com', 1, 'user');
INSERT INTO users(user_id, firstname, lastname, username, password, phone, address, email, confirmation_status, access_privilege) VALUES (54, 'Sona', 'Mikayelyan', 'sonamika', '7c5563af58eef274a792bd8de4a0a4ba84d2edde', null, null, 'sonamikayelyan@workfront.com', 1, 'user');
INSERT INTO users(user_id, firstname, lastname, username, password, phone, address, email, confirmation_status, access_privilege) VALUES (90, 'Anahit', 'galstyan', 'anigal', '73d6ada7a6d5a2e1e51ac55d269aea7f29943485', null, null, 'galstyan@gmail.com', 1, 'user');
