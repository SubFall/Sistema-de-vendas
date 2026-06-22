package domain.movimento;

import domain.produto.Produto;

import java.math.BigDecimal;

public class MovimentoItem {
    private Produto produto;
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;

    public MovimentoItem(Produto produto, BigDecimal quantidade, BigDecimal valorUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public static BuilderMovimentoItem builder() {
        return new BuilderMovimentoItem();
    }

    public static final class BuilderMovimentoItem {
        private Produto produto;
        private BigDecimal quantidade;
        private BigDecimal valorUnitario;

        public BuilderMovimentoItem produto(Produto produto) {
            this.produto = produto;
            return this;
        }

        public BuilderMovimentoItem quantidade(BigDecimal quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public BuilderMovimentoItem valorUnitario(BigDecimal valorUnitario) {
            this.valorUnitario = valorUnitario;
            return this;
        }

        public MovimentoItem build() {
            if (produto == null) {
                throw new IllegalArgumentException("Produto obrigatório");
            }

            if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantidade inválida");
            }

            if (valorUnitario == null || valorUnitario.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor unitário inválido");
            }

            return new MovimentoItem(produto, quantidade, valorUnitario);
        }
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return this.quantidade.multiply(this.valorUnitario);
    }

    @Override
    public String toString() {
        return "MovimentoItem{" +
                "produto=" + produto.getDescricao() +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", valorTotal=" + getValorTotal() +
                '}';
    }
}
