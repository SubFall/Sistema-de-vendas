package domain.produto;

import java.math.BigDecimal;

public class Produto {
    private int id;
    private String descricao;
    private BigDecimal precoVenda;
    private BigDecimal precoCusto;
    private boolean ativo;

    private Produto(int id, String descricao, BigDecimal precoVenda, BigDecimal precoCusto, boolean ativo) {
        this.id = id;
        this.descricao = descricao;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
        this.ativo = ativo;
    }

    public static ProdutoBuilder builder() {
        return new ProdutoBuilder();
    }

    public static final class ProdutoBuilder {
        private int id;
        private String descricao;
        private BigDecimal precoVenda = BigDecimal.ZERO;
        private BigDecimal precoCusto = BigDecimal.ZERO;
        private boolean ativo = true;

        public ProdutoBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ProdutoBuilder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public ProdutoBuilder valorVenda(BigDecimal precoVenda) {
            this.precoVenda = precoVenda;
            return this;
        }

        public ProdutoBuilder precoCusto(BigDecimal custoVenda) {
            this.precoCusto = custoVenda;
            return this;
        }

        public ProdutoBuilder ativo(boolean ativo) {
            this.ativo = ativo;
            return this;
        }

        public Produto build() {
            return new Produto(id, descricao, precoVenda, precoCusto, ativo);
        }
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", precoVenda=" + precoVenda +
                ", precoCusto=" + precoCusto +
                ", ativo=" + ativo +
                '}';
    }
}
