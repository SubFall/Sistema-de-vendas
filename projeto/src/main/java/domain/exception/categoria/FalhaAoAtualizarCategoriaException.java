package domain.exception.categoria;

public class FalhaAoAtualizarCategoriaException extends RuntimeException {
    public FalhaAoAtualizarCategoriaException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaAoAtualizarCategoriaException(String message) {
        super(message);
    }

    public FalhaAoAtualizarCategoriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
