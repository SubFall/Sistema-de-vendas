package domain.exception.categoria;

public class FalhaCategoriaNaoExisteException extends RuntimeException {
    public FalhaCategoriaNaoExisteException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaCategoriaNaoExisteException(String message) {
        super(message);
    }

    public FalhaCategoriaNaoExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
