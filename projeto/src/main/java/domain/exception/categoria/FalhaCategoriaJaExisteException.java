package domain.exception.categoria;

public class FalhaCategoriaJaExisteException extends RuntimeException {
    public FalhaCategoriaJaExisteException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaCategoriaJaExisteException(String message) {
        super(message);
    }

    public FalhaCategoriaJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
