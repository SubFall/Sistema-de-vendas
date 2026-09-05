package domain.ajusteestoque;

import domain.estoque.Estoque;
import domain.produto.Produto;

import java.math.BigDecimal;
import java.util.Objects;

public class AjusteEstoqueItens {
    private Long id;
    private Produto produto;
    private Estoque estoque;
    private BigDecimal contagem;

    private AjusteEstoqueItens(Long id, Produto produto, Estoque estoque, BigDecimal contagem) {
        this.id = id;
        this.produto = produto;
        this.estoque = estoque;
        this.contagem = contagem;
    }

    public static AjusteEstoqueItensBuilder builder() {
        return new AjusteEstoqueItensBuilder();
    }

    public static class AjusteEstoqueItensBuilder {
        private Long id;
        private Produto produto;
        private Estoque estoque;
        private BigDecimal contagem;

        public AjusteEstoqueItensBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AjusteEstoqueItensBuilder produto(Produto produto) {
            this.produto = produto;
            return this;
        }

        public AjusteEstoqueItensBuilder estoque(Estoque estoque) {
            this.estoque = estoque;
            return this;
        }

        public AjusteEstoqueItensBuilder contagem(BigDecimal contagem) {
            this.contagem = contagem;
            return this;
        }

        public AjusteEstoqueItens build() {
            return new AjusteEstoqueItens(id, produto, estoque, contagem);
        }
    }

    public Long getId() {
        return id;
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
        return this.contagem.subtract(estoque.getQuantidade());
    }

    @Override
    public String toString() {
        return "AjusteEstoqueItens{" +
                "produto=" + produto.getId() +
                ", estoque=" + estoque.getQuantidade() +
                ", contagem=" + contagem +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AjusteEstoqueItens that = (AjusteEstoqueItens) o;
        return Objects.equals(id, that.id) && Objects.equals(produto, that.produto) && Objects.equals(estoque, that.estoque) && Objects.equals(contagem, that.contagem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, produto, estoque, contagem);
    }
}
