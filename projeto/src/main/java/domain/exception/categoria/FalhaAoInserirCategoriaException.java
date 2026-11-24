package domain.exception.categoria;

public class FalhaAoInserirCategoriaException extends RuntimeException {
    public FalhaAoInserirCategoriaException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaAoInserirCategoriaException(String message) {
        super(message);
    }

    public FalhaAoInserirCategoriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
