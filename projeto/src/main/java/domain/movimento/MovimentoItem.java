package domain.movimento;

import domain.produto.Produto;

import java.math.BigDecimal;

public class MovimentoItem {
    private int id;
    private Produto produto;
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;

    public MovimentoItem(int id, Produto produto, BigDecimal quantidade) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public static BuilderMovimentoItem builder() {
        return new BuilderMovimentoItem();
    }

    public static final class BuilderMovimentoItem {
        private int id;
        private Produto produto;
        private BigDecimal quantidade;

        public BuilderMovimentoItem id(int id) {
            this.id = id;
            return this;
        }

        public BuilderMovimentoItem produto(Produto produto) {
            this.produto = produto;
            return this;
        }

        public BuilderMovimentoItem quantidade(BigDecimal quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public MovimentoItem build() {
            if (produto == null) {
                throw new IllegalArgumentException("Produto obrigatório");
            }

            if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantidade inválida");
            }

            return new MovimentoItem(id, produto, quantidade);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
