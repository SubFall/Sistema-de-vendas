package dto;

import java.math.BigDecimal;

public class ProdutoEstoqueDTO {
    private long idProduto;
    private String descricao;
    private BigDecimal quantidade;

    private ProdutoEstoqueDTO(long idProduto, String descricao, BigDecimal quantidade) {
        this.idProduto = idProduto;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public static ProdutoEstoqueBuilder builder() {
        return new ProdutoEstoqueBuilder();
    }

    public static class ProdutoEstoqueBuilder {
        private long idProduto;
        private String descricao;
        private BigDecimal quantidade;

        public ProdutoEstoqueBuilder idProduto(Long idProduto) {
            this.idProduto = idProduto;
            return this;
        }

        public ProdutoEstoqueBuilder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public ProdutoEstoqueBuilder quantidade(BigDecimal quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public ProdutoEstoqueDTO build() {
            return new ProdutoEstoqueDTO(idProduto, descricao, quantidade);
        }
    }

    public long getIdProduto() {
        return idProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }
}
