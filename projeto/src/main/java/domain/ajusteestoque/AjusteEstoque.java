package domain.ajusteestoque;

import java.time.LocalDateTime;
import java.util.List;

public class AjusteEstoque {
    private Long id;
    private String titulo;
    private LocalDateTime dateHora;
    private Status status;
    private StatusMovimentoCriado statusMovimentoCriado;
    private List<AjusteEstoqueItens> ajusteEstoqueItens;

    private AjusteEstoque(
            Long id,
            String titulo,
            LocalDateTime dateHora,
            Status status,
            StatusMovimentoCriado statusMovimentoCriado,
            List<AjusteEstoqueItens> ajusteEstoqueItens) {
        this.id = id;
        this.titulo = titulo;
        this.dateHora = dateHora;
        this.status = status;
        this.statusMovimentoCriado = statusMovimentoCriado;
        this.ajusteEstoqueItens = ajusteEstoqueItens;
    }

    public static AjusteEstoqueBuilder builder() {
        return new AjusteEstoqueBuilder();
    }

    public static class AjusteEstoqueBuilder {
        private Long id;
        private String titulo;
        private LocalDateTime dateHora;
        private Status status;
        private StatusMovimentoCriado statusMovimentoCriado;
        private List<AjusteEstoqueItens> ajusteEstoqueItens;

        public AjusteEstoqueBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AjusteEstoqueBuilder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public AjusteEstoqueBuilder dateHora(LocalDateTime dateHora) {
            this.dateHora = dateHora;
            return this;
        }

        public AjusteEstoqueBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public AjusteEstoqueBuilder statusMovimentoCriado(StatusMovimentoCriado statusMovimentoCriado) {
            this.statusMovimentoCriado = statusMovimentoCriado;
            return this;
        }

        public AjusteEstoqueBuilder ajusteEstoqueItens(List<AjusteEstoqueItens> ajusteEstoqueItens) {
            this.ajusteEstoqueItens = ajusteEstoqueItens;
            return this;
        }

        public AjusteEstoque build() {

            if (titulo == null || titulo.isEmpty()) {
                throw new IllegalArgumentException("Título obrigatório");
            }

            if (status == null) {
                throw new IllegalArgumentException("Status obrigatório");
            }

            if (dateHora == null) {
                dateHora = LocalDateTime.now();
            }

            if (statusMovimentoCriado == null) {
                statusMovimentoCriado = StatusMovimentoCriado.MOVIMENTO_NAO_CRIADO;
            }

            return new AjusteEstoque(id, titulo, dateHora, status, statusMovimentoCriado, ajusteEstoqueItens);
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getDateHora() {
        return dateHora;
    }

    public Status getStatus() {
        return status;
    }

    public StatusMovimentoCriado getStatusMovimentoCriado() {
        return statusMovimentoCriado;
    }

    public List<AjusteEstoqueItens> getAjusteEstoqueItens() {
        return ajusteEstoqueItens;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AjusteEstoque{" +
                "titulo='" + titulo + '\'' +
                ", dateTime=" + dateHora +
                ", status=" + status +
                ", ajusteEstoqueItens=" + ajusteEstoqueItens +
                '}';
    }
}
