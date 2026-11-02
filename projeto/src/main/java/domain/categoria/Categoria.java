package domain.categoria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Categoria {
    private Long id;
    private String descricao;
    @Setter
    private boolean ativo;

    public static final Long ID_SEM_CATEGORIA = 1L;

    @Builder
    private Categoria(Long id, String descricao, boolean ativo) {
        this.id = id;
        setDescricao(descricao);
        this.ativo = ativo;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição obrigatória.");
        }
        this.descricao = descricao;
    }
}
