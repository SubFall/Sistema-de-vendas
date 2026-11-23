package domain.exception;

public class FalhaProdutoEstaAtivoException extends RuntimeException {
    public FalhaProdutoEstaAtivoException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaProdutoEstaAtivoException(String message) {
        super(message);
    }

    public FalhaProdutoEstaAtivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
