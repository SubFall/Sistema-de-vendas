package domain.pessoa;

public enum PessoaPapel {
    CLIENTE(1),
    FUNCIONARIO(2);

    private int codigo;

    PessoaPapel(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return this.codigo;
    }
}
