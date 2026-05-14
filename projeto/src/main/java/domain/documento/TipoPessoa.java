package domain.documento;

public enum TipoPessoa {
    FISICA(0),
    JURIDICA(1);

    private final int codigo;

    TipoPessoa(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return this.codigo;
    }
}
