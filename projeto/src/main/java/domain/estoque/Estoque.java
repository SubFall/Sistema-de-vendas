package domain.estoque;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Estoque {
    private Long idProduto;
    private BigDecimal quantidade;

    @Builder
    private Estoque(Long idProduto, BigDecimal quantidade) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public BigDecimal getQuantidade() {
        if (quantidade == null) return BigDecimal.ZERO;

        return quantidade;
    }
}
