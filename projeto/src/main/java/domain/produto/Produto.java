package domain.produto;

import domain.categoria.Categoria;
import domain.exception.produto.FalhaValorNaoPodeSerNegativoException;
import domain.exception.produto.FalhaValorObrigatorioException;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedOn;
    private Categoria categoria;

    @Builder
    private Produto(Long id, String descricao, BigDecimal precoVenda, BigDecimal precoCusto,
                    boolean ativo, LocalDateTime createdAt, LocalDateTime modifiedOn, Categoria categoria) {
        this.id = id;
        setDescricao(descricao.trim());
        setPrecoVenda(precoVenda);
        setPrecoCusto(precoCusto);
        this.ativo = ativo;
        this.createdAt = createdAt;
        this.modifiedOn = modifiedOn;
        setCategoria(categoria);
    }

    public void setDescricao(String descricao) {

        if (descricao == null || descricao.isBlank()) {
            throw new FalhaValorObrigatorioException("Descricao obrigatoria.");
        }

        this.descricao = descricao;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {

        if (precoVenda == null) {
            throw new FalhaValorObrigatorioException("Preco de venda obrigatorio.");
        }

        if (precoVenda.compareTo(BigDecimal.ZERO) <= 0) {
            throw new FalhaValorNaoPodeSerNegativoException("Preco de venda deve ser maior que zero");
        }

        this.precoVenda = precoVenda;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {

        if (precoCusto == null) {
            precoCusto = BigDecimal.ZERO;
        }

        if (precoCusto.compareTo(BigDecimal.ZERO) < 0) {
            throw new FalhaValorNaoPodeSerNegativoException("Preco de custo nao deve ser menor que zero");
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

    public void setModifiedOn() { this.modifiedOn = LocalDateTime.now(); }

}
