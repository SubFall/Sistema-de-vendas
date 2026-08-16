package domain.ajusteestoque;

import java.time.LocalDateTime;
import java.util.List;

public class AjusteEstoque {
    private Long id;
    private String titulo;
    private LocalDateTime dateHora;
    private Status status;
    private List<AjusteEstoqueItens> ajusteEstoqueItens;

    private AjusteEstoque(Long id, String titulo, Status status, List<AjusteEstoqueItens> ajusteEstoqueItens) {
        this.id = id;
        this.titulo = titulo;
        this.dateHora = LocalDateTime.now();
        this.status = status;
        this.ajusteEstoqueItens = ajusteEstoqueItens;
    }

    public static AjusteEstoqueBuilder builder() {
        return new AjusteEstoqueBuilder();
    }

    public static class AjusteEstoqueBuilder {
        private Long id;
        private String titulo;
        private Status status;
        private List<AjusteEstoqueItens> ajusteEstoqueItens;

        public AjusteEstoqueBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AjusteEstoqueBuilder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public AjusteEstoqueBuilder status(Status status) {
            this.status = status;
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

            return new AjusteEstoque(id, titulo, status, ajusteEstoqueItens);
        }
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
