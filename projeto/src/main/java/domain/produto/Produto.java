package domain.produto;

import domain.categoria.Categoria;
import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Produto {
    @EqualsAndHashCode.Include
    private Long id;
    private String descricao;
    private BigDecimal precoVenda;
    private BigDecimal precoCusto;
    private boolean ativo;
    private Categoria categoria;

    @Builder
    private Produto(Long id, String descricao, BigDecimal precoVenda, BigDecimal precoCusto, boolean ativo, Categoria categoria) {
        this.id = id;
        setDescricao(descricao.trim());
        setPrecoVenda(precoVenda);
        setPrecoCusto(precoCusto);
        this.ativo = ativo;
        setCategoria(categoria);
    }

    public void setDescricao(String descricao) {

        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descricao obrigatoria.");
        }

        this.descricao = descricao;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {

        if (precoVenda == null) {
            throw new IllegalArgumentException("Preco de venda obrigatorio.");
        }

        if (precoVenda.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preco de venda deve ser maior que zero");
        }

        this.precoVenda = precoVenda;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {

        if (precoCusto == null) {
            precoCusto = BigDecimal.ZERO;
        }

        if (precoCusto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preco de custo nao deve ser menor que zero");
        }

        this.precoCusto = precoCusto;
    }

    public void setCategoria(Categoria categoria) {

        if (categoria == null) {
            this.categoria = Categoria.builder().id(Categoria.ID_SEM_CATEGORIA).descricao("Sem Categoria").ativo(true).build();
        } else {
            this.categoria = categoria;
        }
    }

    public void ativar() {
        this.ativo = true;
    }

    public void inativar() {
        this.ativo = false;
    }

}
