package domain.exception;

public class FalhaValorObrigatorioException extends RuntimeException {
    public FalhaValorObrigatorioException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaValorObrigatorioException(String message) {
        super(message);
    }

    public FalhaValorObrigatorioException(String message, Throwable cause) {
        super(message, cause);
    }
}
