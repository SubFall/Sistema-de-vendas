package domain.ajusteestoque;

import java.time.LocalDateTime;

public class AjusteEstoque {
    private String titulo;
    private LocalDateTime dateTime;
    private Status status;
    private AjusteEstoqueItens ajusteEstoqueItens;

    private AjusteEstoque(String titulo, Status status, AjusteEstoqueItens ajusteEstoqueItens) {
        this.titulo = titulo;
        this.dateTime = LocalDateTime.now();
        this.status = status;
        this.ajusteEstoqueItens = ajusteEstoqueItens;
    }

    public static AjusteEstoqueItens.AjusteEstoqueBuilder Builder() {
        return new AjusteEstoqueItens.AjusteEstoqueBuilder();
    }

    public static class AjusteEstoqueBuilder {
        private String titulo;
        private Status status;
        private AjusteEstoqueItens ajusteEstoqueItens;

        public AjusteEstoqueBuilder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public AjusteEstoqueBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public AjusteEstoqueBuilder ajusteEstoqueItens(AjusteEstoqueItens ajusteEstoqueItens ) {
            this.ajusteEstoqueItens = ajusteEstoqueItens;
            return this;
        }
        
        public AjusteEstoque build() {
            return new AjusteEstoque(titulo, status, ajusteEstoqueItens);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Status getStatus() {
        return status;
    }
}
