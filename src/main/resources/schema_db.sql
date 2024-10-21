CREATE DATABASE IF NOT EXISTS rubrica_db;

USE rubrica_db;

CREATE TABLE persone (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50),
    cognome VARCHAR(50),
    indirizzo VARCHAR(100),
    telefono VARCHAR(20),
    eta INT
);

