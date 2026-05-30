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

    public int getCodigo() {
        return this.codigo;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
