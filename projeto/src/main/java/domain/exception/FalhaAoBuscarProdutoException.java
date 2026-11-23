package domain.exception;

public class FalhaAoBuscarProdutoException extends RuntimeException {
    public FalhaAoBuscarProdutoException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaAoBuscarProdutoException(String message) {
        super(message);
    }

    public FalhaAoBuscarProdutoException(String message, Throwable cause) {
        super(message, cause);
    }
}
