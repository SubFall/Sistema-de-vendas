package domain.ajusteestoque;

import domain.estoque.Estoque;
import domain.produto.Produto;

import java.math.BigDecimal;

public class AjusteEstoque {
    private Produto produto;
    private Estoque estoque;
    private BigDecimal contagem;
    private BigDecimal diferenca;

    private AjusteEstoque(Produto produto, Estoque estoque, BigDecimal contagem, BigDecimal diferenca) {
        this.produto = produto;
        this.estoque = estoque;
        this.contagem = contagem;
        this.diferenca = diferenca;
    }

    public static AjusteEstoqueBuilder Builder() {
        return new AjusteEstoqueBuilder();
    }

    public static class AjusteEstoqueBuilder {
        private Produto produto;
        private Estoque estoque;
        private BigDecimal contagem;
        private BigDecimal diferenca;

        public AjusteEstoqueBuilder produto(Produto produto) {
            this.produto = produto;
            return this;
        }

        public AjusteEstoqueBuilder estoque(Estoque estoque) {
            this.estoque = estoque;
            return this;
        }

        public AjusteEstoqueBuilder contagem(BigDecimal contagem) {
            this.contagem = contagem;
            return this;
        }

        public AjusteEstoqueBuilder diferenca(BigDecimal diferenca) {
            this.diferenca = diferenca;
            return this;
        }

        public AjusteEstoque build() {
            return new AjusteEstoque(produto, estoque, contagem, diferenca);
        }
    }

    public Produto getProduto() {
        return produto;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public BigDecimal getContagem() {
        return contagem;
    }

    public BigDecimal getDiferenca() {
        return diferenca;
    }
}
