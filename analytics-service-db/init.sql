CREATE TABLE processed_event (
	topic VARCHAR(100) NOT NULL,
	event_id INTEGER NOT NULL,
	processed_at TIMESTAMP NOT NULL DEFAULT NOW(),
	PRIMARY KEY(topic, event_id)
);

-----------

CREATE TABLE order_client (
	id INTEGER PRIMARY KEY,
	name VARCHAR(255)
);

CREATE TABLE order_executor (
	id INTEGER PRIMARY KEY,
	name VARCHAR(255)
);

CREATE TABLE order_offering (
	id INTEGER PRIMARY KEY,
	name VARCHAR(255),
	cost DECIMAL(10, 2),
	is_deprecated BOOLEAN
);

CREATE TABLE "order" (
	id INTEGER PRIMARY KEY,
	status VARCHAR(25),
	id_order_client INTEGER,
	id_order_offering INTEGER,
	cost DECIMAL(10, 2),
	id_order_executor INTEGER,
	created_at TIMESTAMP,
	deadline_on DATE
);

-----------

CREATE TABLE execution_client (
	id INTEGER PRIMARY KEY,
	name VARCHAR(255)
);

CREATE TABLE execution_executor (
	id INTEGER PRIMARY KEY,
	name VARCHAR(255),
	daily_points INTEGER,
	final_date DATE
);

CREATE TABLE execution_offering (
	id INTEGER PRIMARY KEY,
	name VARCHAR(255),
	cost DECIMAL(10, 2),
	points INTEGER,
	deprecated_at TIMESTAMP
);

CREATE TABLE execution (
	id INTEGER PRIMARY KEY,
	status VARCHAR(25) NOT NULL,
	id_execution_client INTEGER,
	order_id INTEGER,
	id_execution_offering INTEGER,
	cost DECIMAL(10, 2),
	id_execution_executor INTEGER,
	created_at TIMESTAMP,
	deadline_on DATE
);

-----------

CREATE TABLE payment_client (
	id INTEGER PRIMARY KEY,
	name VARCHAR(255)
);

CREATE TABLE order_payment (
	id INTEGER PRIMARY KEY,
	id_payment_client INTEGER,
	order_id INTEGER,
	cost DECIMAL(10, 2),
	payment_id VARCHAR(100),
	status VARCHAR(25),
	processed_at TIMESTAMP
);