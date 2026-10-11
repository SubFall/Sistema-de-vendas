package domain.ajusteestoque;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class AjusteEstoque {
    @EqualsAndHashCode.Include
    private Long id;
    private String titulo;
    private LocalDateTime dateHora;
    private Status status;
    private StatusMovimentoCriado statusMovimentoCriado;
    private List<AjusteEstoqueItens> ajusteEstoqueItens;

    @Builder
    private AjusteEstoque(
            Long id,
            String titulo,
            LocalDateTime dateHora,
            Status status,
            StatusMovimentoCriado statusMovimentoCriado,
            List<AjusteEstoqueItens> ajusteEstoqueItens) {

        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Título obrigatório");

        if (status == null) throw new IllegalArgumentException("Status obrigatório");

        this.id = id;
        this.titulo = titulo;
        this.dateHora = dateHora != null
                ? dateHora
                : LocalDateTime.now();

        this.status = status;
        this.statusMovimentoCriado = statusMovimentoCriado != null
                ? statusMovimentoCriado
                : StatusMovimentoCriado.MOVIMENTO_NAO_CRIADO;

        this.ajusteEstoqueItens = ajusteEstoqueItens;
    }
}
