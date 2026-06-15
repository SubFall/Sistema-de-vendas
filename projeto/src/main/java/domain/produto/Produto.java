package domain.produto;

import domain.categoria.Categoria;

import java.math.BigDecimal;

public class Produto {
    private int id;
    private String descricao;
    private BigDecimal precoVenda;
    private BigDecimal precoCusto;
    private boolean ativo;
    private Categoria categoria;

    private Produto(int id, String descricao, BigDecimal precoVenda, BigDecimal precoCusto, boolean ativo, Categoria categoria) {
        this.id = id;
        this.descricao = descricao;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
        this.ativo = ativo;
        this.categoria = categoria;
    }

    public static ProdutoBuilder builder() {
        return new ProdutoBuilder();
    }

    public static final class ProdutoBuilder {
        private int id;
        private String descricao;
        private BigDecimal precoVenda;
        private BigDecimal precoCusto = BigDecimal.ZERO;
        private boolean ativo = true;
        private Categoria categoria;

        public ProdutoBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ProdutoBuilder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public ProdutoBuilder precoVenda(BigDecimal precoVenda) {
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

        public ProdutoBuilder categoria(Categoria categoria) {
            this.categoria = categoria;
            return this;
        }

        public Produto build() {
            if (descricao == null || descricao.isBlank()) {
                throw new IllegalArgumentException("Descrição obrigatória.");
            }

            if (precoVenda == null) {
                throw new IllegalArgumentException("Preço de venda obrigatório.");
            }

            if (precoVenda.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Preço de venda deve ser maior que zero");
            }

            if (precoCusto.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Preço de custo inválido");
            }

            return new Produto(id, descricao, precoVenda, precoCusto, ativo, categoria);
        }
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {

        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição obrigatória.");
        }
        this.descricao = descricao;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {

        if (precoVenda == null) {
            throw new IllegalArgumentException("Preço de venda obrigatório.");
        }

        if (precoVenda.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço de venda deve ser maior que zero");
        }
        this.precoVenda = precoVenda;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {

        if (precoCusto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço de custo inválido");
        }
        this.precoCusto = precoCusto;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", precoVenda=" + precoVenda +
                ", precoCusto=" + precoCusto +
                ", ativo=" + ativo +
                ", categoria=" + categoria +
                '}';
    }
}
