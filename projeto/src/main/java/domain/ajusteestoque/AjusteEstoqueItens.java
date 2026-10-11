package domain.ajusteestoque;

import domain.estoque.Estoque;
import domain.produto.Produto;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class AjusteEstoqueItens {
    private Long id;
    @EqualsAndHashCode.Include
    private Produto produto;
    private Estoque estoque;
    private BigDecimal contagem;

    @Builder
    private AjusteEstoqueItens(Long id, Produto produto, Estoque estoque, BigDecimal contagem) {
        this.id = id;
        this.produto = produto;
        this.estoque = estoque;
        this.contagem = contagem;
    }

    public BigDecimal getDiferenca() {
        return this.contagem.subtract(estoque.getQuantidade());
    }
}
