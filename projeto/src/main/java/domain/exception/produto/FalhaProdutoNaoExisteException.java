package domain.exception.produto;

public class FalhaProdutoNaoExisteException extends RuntimeException {
    public FalhaProdutoNaoExisteException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaProdutoNaoExisteException(String message) {
        super(message);
    }

    public FalhaProdutoNaoExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
