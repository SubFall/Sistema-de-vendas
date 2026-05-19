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
    CONSTRAINT `id_pessoa`
        FOREIGN KEY (`id_pessoa`)
            REFERENCES `eclipse_net`.`pessoa` (`id_pessoa`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

