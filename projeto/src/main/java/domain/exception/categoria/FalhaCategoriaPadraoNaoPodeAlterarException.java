package domain.exception.categoria;

public class FalhaCategoriaPadraoNaoPodeAlterarException extends RuntimeException {
    public FalhaCategoriaPadraoNaoPodeAlterarException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaCategoriaPadraoNaoPodeAlterarException(String message) {
        super(message);
    }

    public FalhaCategoriaPadraoNaoPodeAlterarException(String message, Throwable cause) {
        super(message, cause);
    }
}
