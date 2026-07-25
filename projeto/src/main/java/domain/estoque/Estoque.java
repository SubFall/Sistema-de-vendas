package domain.estoque;

import java.math.BigDecimal;

public class Estoque {
    private int idProduto;
    private BigDecimal quantidade;

    private Estoque(int idProduto, BigDecimal quantidade) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public static EstoqueBuilder builder() {
        return new EstoqueBuilder();
    }

    public static final class EstoqueBuilder {
        private int idProduto;
        private BigDecimal quantidade;

        public EstoqueBuilder idProduto(int idProduto) {
            this.idProduto = idProduto;
            return this;
        }

        public EstoqueBuilder quantidade(BigDecimal quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public Estoque build() {
            return new Estoque(idProduto, quantidade);
        }
    }

    public int getIdProduto() {
        return idProduto;
    }

    public BigDecimal getQuantidade() {
        if (quantidade == null) return BigDecimal.ZERO;

        return quantidade;
    }
}
