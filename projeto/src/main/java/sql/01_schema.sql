CREATE TABLE `eclipse_net`.`pessoa`
(
    `id_pessoa` INT         NOT NULL AUTO_INCREMENT,
    `descricao` VARCHAR(60) NOT NULL,
    `documento` VARCHAR(14) NOT NULL,
    `tipo`      TINYINT(1)  NOT NULL,
    PRIMARY KEY (`id_pessoa`),
    UNIQUE INDEX `id_pessoa_UNIQUE` (`id_pessoa` ASC) VISIBLE,
    UNIQUE INDEX `documento_UNIQUE` (`documento` ASC) VISIBLE
);

CREATE TABLE `eclipse_net`.`endereco`
(
    `id_endereco` INT          NOT NULL AUTO_INCREMENT,
    `logradouro`  VARCHAR(100) NOT NULL,
    `cidade`      VARCHAR(45)  NOT NULL,
    `uf`          VARCHAR(2)   NOT NULL,
    `bairro`      VARCHAR(45)  NOT NULL,
    `numero`      VARCHAR(10)  NOT NULL,
    `cep`         VARCHAR(20)  NOT NULL,
    `id_pessoa`   INT          NOT NULL,
    PRIMARY KEY (`id_endereco`),
    UNIQUE INDEX `id_endereco_UNIQUE` (`id_endereco` ASC) VISIBLE,
    INDEX         `id_pessoa_idx` (`id_pessoa` ASC) VISIBLE,
    CONSTRAINT `fk_pessoa_endereco`
        FOREIGN KEY (`id_pessoa`)
            REFERENCES `eclipse_net`.`pessoa` (`id_pessoa`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE `eclipse_net`.`papel`
(
    `id_papel` INT NOT NULL,
    `descricao` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`id_papel`)
);

CREATE TABLE `eclipse_net`.`pessoa_papel` (
    `id_pessoa` INT NOT NULL,
    `id_papel` INT NOT NULL,
    PRIMARY KEY (`id_pessoa`, `id_papel`),
    INDEX `papel_idx` (`id_papel` ASC) VISIBLE,
    CONSTRAINT `fk_pessoa_papel_pessoa`
        FOREIGN KEY (`id_pessoa`)
            REFERENCES `eclipse_net`.`pessoa` (`id_pessoa`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_pessoa_papel_papel`
        FOREIGN KEY (`id_papel`)
            REFERENCES `eclipse_net`.`papel` (`id_papel`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

INSERT INTO papel VALUES (1, 'CLIENTE');
INSERT INTO papel VALUES (2, 'FUNCIONARIO');

