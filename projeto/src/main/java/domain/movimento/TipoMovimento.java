package domain.movimento;

public enum TipoMovimento {
    SAIDA(0),
    ENTRADA(1),
    AJUSTE(2);

    private final int codigo;

    TipoMovimento(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoMovimento porCodigo(int codigo) {
        for (TipoMovimento tipoMovimento : TipoMovimento.values()) {
            if (tipoMovimento.codigo == codigo) {
                return tipoMovimento;
            }
        }
        throw new IllegalArgumentException("Código inválido " + codigo);
    }
}
