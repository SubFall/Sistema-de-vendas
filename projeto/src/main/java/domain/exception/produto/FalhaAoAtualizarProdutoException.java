package domain.exception.produto;

public class FalhaAoAtualizarProdutoException extends RuntimeException {
    public FalhaAoAtualizarProdutoException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaAoAtualizarProdutoException(String message) {
        super(message);
    }

    public FalhaAoAtualizarProdutoException(String message, Throwable cause) {
        super(message, cause);
    }
}
