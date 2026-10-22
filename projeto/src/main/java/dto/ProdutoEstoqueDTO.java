package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoEstoqueDTO {
    private long idProduto;
    private String descricao;
    private BigDecimal quantidade;

    @Builder
    private ProdutoEstoqueDTO(long idProduto, String descricao, BigDecimal quantidade) {
        this.idProduto = idProduto;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }
}
