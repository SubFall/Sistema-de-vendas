package domain.estoque;

import java.math.BigDecimal;

public class Estoque {
    private Long idProduto;
    private BigDecimal quantidade;

    private Estoque(Long idProduto, BigDecimal quantidade) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public static EstoqueBuilder builder() {
        return new EstoqueBuilder();
    }

    public static final class EstoqueBuilder {
        private Long idProduto;
        private BigDecimal quantidade;

        public EstoqueBuilder idProduto(Long idProduto) {
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

    public Long getIdProduto() {
        return idProduto;
    }

    public BigDecimal getQuantidade() {
        if (quantidade == null) return BigDecimal.ZERO;

        return quantidade;
    }
}
