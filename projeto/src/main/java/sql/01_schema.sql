CREATE TABLE `eclipse_net`.`pessoas`
(
    `id_pessoa` INT         NOT NULL AUTO_INCREMENT,
    `descricao` VARCHAR(60) NOT NULL,
    `documento` VARCHAR(14) NOT NULL,
    `tipo`      TINYINT(1)  NOT NULL,
    `ativo`     TINYINT(1)  NOT NULL DEFAULT 1,
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
            REFERENCES `eclipse_net`.`pessoas` (`id_pessoa`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE `eclipse_net`.`papel`
(
    `id_papel`  INT         NOT NULL,
    `descricao` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`id_papel`)
);

CREATE TABLE `eclipse_net`.`pessoa_papel`
(
    `id_pessoa` INT NOT NULL,
    `id_papel`  INT NOT NULL,
    PRIMARY KEY (`id_pessoa`, `id_papel`),
    INDEX       `papel_idx` (`id_papel` ASC) VISIBLE,
    CONSTRAINT `fk_pessoa_papel_pessoa`
        FOREIGN KEY (`id_pessoa`)
            REFERENCES `eclipse_net`.`pessoas` (`id_pessoa`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_pessoa_papel_papel`
        FOREIGN KEY (`id_papel`)
            REFERENCES `eclipse_net`.`papel` (`id_papel`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

INSERT INTO papel
VALUES (1, 'CLIENTE');
INSERT INTO papel
VALUES (2, 'FUNCIONARIO');

CREATE TABLE `eclipse_net`.`categoria`
(
    `id_categoria` INT         NOT NULL AUTO_INCREMENT,
    `descricao`    VARCHAR(45) NOT NULL,
    `ativo`        TINYINT(1)  NOT NULL DEFAULT 1,
    PRIMARY KEY (`id_categoria`),
    UNIQUE INDEX `descricao_UNIQUE` (`descricao` ASC) VISIBLE
);

INSERT INTO `eclipse_net`.`categoria` (`id_categoria`, `descricao`, `ativo`)
VALUES ('1', 'Sem Categoria', '1');

CREATE TABLE `eclipse_net`.`produtos`
(
    `id_produto`   INT            NOT NULL AUTO_INCREMENT,
    `descricao`    VARCHAR(60)    NOT NULL,
    `preco_venda`  DECIMAL(15, 2) NOT NULL,
    `preco_custo`  DECIMAL(15, 2) NOT NULL,
    `ativo`        TINYINT(1) NOT NULL DEFAULT 1,
    `id_categoria` INT            NOT NULL,
    PRIMARY KEY (`id_produto`),
    INDEX          `id_categoria_idx` (`id_categoria` ASC) VISIBLE,
    CONSTRAINT `fk_produto_categoria`
        FOREIGN KEY (`id_categoria`)
            REFERENCES `eclipse_net`.`categoria` (`id_categoria`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);