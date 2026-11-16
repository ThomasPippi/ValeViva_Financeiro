CREATE TABLE notificacao (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             titulo VARCHAR(255) NOT NULL,
                             mensagem TEXT NOT NULL,
                             data_envio DATETIME NOT NULL,
                             lida BOOLEAN NOT NULL
);