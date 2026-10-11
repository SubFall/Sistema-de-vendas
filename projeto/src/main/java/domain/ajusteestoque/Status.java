package domain.ajusteestoque;

public enum Status {
    ABERTO(0),
    FECHADO(1);

    private final int codigo;

    Status(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static Status porCodigo(int codigo) {
        for (Status status : Status.values()) {
            if (status.codigo == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido " + codigo);
    }
}
