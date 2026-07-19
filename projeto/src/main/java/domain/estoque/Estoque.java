package domain.estoque;

import domain.produto.Produto;

import java.math.BigDecimal;

public class Estoque {
    private Produto produto;
    private BigDecimal quantidade;

    private Estoque(Produto produto, BigDecimal quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public static EstoqueBuilder builder() {
        return new EstoqueBuilder();
    }

    public static final class EstoqueBuilder {
        private Produto produto;
        private BigDecimal quantidade;

        public EstoqueBuilder produto(Produto produto) {
            this.produto = produto;
            return this;
        }

        public EstoqueBuilder quantidade(BigDecimal quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public Estoque build() {
            return new Estoque(produto, quantidade);
        }
    }

    public Produto getProduto() {
        return produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }
}
