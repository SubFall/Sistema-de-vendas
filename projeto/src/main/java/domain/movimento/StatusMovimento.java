package domain.movimento;

public enum StatusMovimento {
    ABERTO(0),
    FINALIZADO(1),
    CANCELADO(2);

    private final int codigo;

    StatusMovimento(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusMovimento porCodigo(int codigo) {
        for (StatusMovimento status : StatusMovimento.values()) {
            if (status.codigo == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + codigo);
    }
}
