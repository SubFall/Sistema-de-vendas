package domain.exception;

public class FalhaValorJaRegistradoException extends RuntimeException {
    public FalhaValorJaRegistradoException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaValorJaRegistradoException(String message) {
        super(message);
    }

    public FalhaValorJaRegistradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
