package domain.exception;

public class FalhaAoBuscarCategoriaException extends RuntimeException {
    public FalhaAoBuscarCategoriaException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaAoBuscarCategoriaException(String message) {
        super(message);
    }

    public FalhaAoBuscarCategoriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
