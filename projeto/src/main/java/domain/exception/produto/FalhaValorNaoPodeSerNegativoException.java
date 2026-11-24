package domain.exception.produto;

public class FalhaValorNaoPodeSerNegativoException extends RuntimeException {
    public FalhaValorNaoPodeSerNegativoException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaValorNaoPodeSerNegativoException(String message) {
        super(message);
    }

    public FalhaValorNaoPodeSerNegativoException(String message, Throwable cause) {
        super(message, cause);
    }
}
