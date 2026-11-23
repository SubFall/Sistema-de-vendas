package domain.exception;

public class FalhaAoInserirProdutoException extends RuntimeException {
    public FalhaAoInserirProdutoException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaAoInserirProdutoException(String message) {
        super(message);
    }

    public FalhaAoInserirProdutoException(String message, Throwable cause) {
        super(message, cause);
    }
}
