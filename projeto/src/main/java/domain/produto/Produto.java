package domain.produto;

import domain.categoria.Categoria;
import lombok.*;

import java.math.BigDecimal;

@Setter
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

        this.id = id;
        this.descricao = descricao;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
        this.ativo = ativo;
        this.categoria = categoria;
    }

    public void setDescricao(String descricao) {

        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição obrigatória.");
        }
        this.descricao = descricao;
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

    public void setPrecoCusto(BigDecimal precoCusto) {

        if (precoCusto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço de custo inválido");
        }
        this.precoCusto = precoCusto;
    }

}
