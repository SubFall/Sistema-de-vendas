package domain.exception;

public class FalhaProdutoEstaInativoException extends RuntimeException {
    public FalhaProdutoEstaInativoException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaProdutoEstaInativoException(String message) {
        super(message);
    }

    public FalhaProdutoEstaInativoException(String message, Throwable cause) {
        super(message, cause);
    }
}
