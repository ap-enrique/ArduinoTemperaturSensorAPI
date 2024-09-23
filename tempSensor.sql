drop database if exists tempSensor;
CREATE DATABASE tempSensor;
USE tempSensor;

CREATE TABLE data (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    timepoint TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	temperatur FLOAT NOT NULL
);

SHOW TABLES;

SELECT * FROM data;

SELECT * FROM data ORDER BY timepoint DESC;