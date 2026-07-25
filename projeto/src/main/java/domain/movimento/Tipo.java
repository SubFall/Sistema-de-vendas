package domain.movimento;

public enum Tipo {
    SAIDA(0),
    ENTRADA(1);

    private final int codigo;

    Tipo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static Tipo porCodigo(int codigo) {
        for (Tipo tipo : Tipo.values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido " + codigo);
    }
}
