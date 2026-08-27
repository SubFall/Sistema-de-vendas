package domain.ajusteestoque;

public enum StatusMovimentoCriado {
    MOVIMENTO_NAO_CRIADO(0),
    FINALIZADO_CRIADO(1);

    private final int codigo;

    StatusMovimentoCriado(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusMovimentoCriado porCodigo(int codigo) {
        for (StatusMovimentoCriado status : StatusMovimentoCriado.values()) {
            if (status.codigo == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido " + codigo);
    }
}
