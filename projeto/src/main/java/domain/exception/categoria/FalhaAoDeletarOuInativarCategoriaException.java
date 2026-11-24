package domain.exception.categoria;

public class FalhaAoDeletarOuInativarCategoriaException extends RuntimeException {
    public FalhaAoDeletarOuInativarCategoriaException() {
        System.out.println("Falha ao Inserir Produto");
    }

    public FalhaAoDeletarOuInativarCategoriaException(String message) {
        super(message);
    }

    public FalhaAoDeletarOuInativarCategoriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
