package domain.pessoa;

public enum PessoaPapel {
    CLIENTE(1, "Cliente"),
    FUNCIONARIO(2, "Funcionário");

    private int codigo;
    private String descricao;

    PessoaPapel(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static PessoaPapel fromCodigo(int id) {
        for (PessoaPapel pessoaPapel : values()) {
            if (pessoaPapel.getCodigo() == id) {
                return pessoaPapel;
            }
        }
        throw new IllegalArgumentException("Papel inválido " + id);
    }

    public int getCodigo() {
        return this.codigo;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
