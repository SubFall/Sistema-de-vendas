package domain.movimento;

public enum StatusMovimento {
    ABERTO(0, "Aberto"),
    FINALIZADO(1, "Finalizado"),
    CANCELADO(2, "Cancelado");

    private final int codigo;
    private final String descricao;

    StatusMovimento(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() { return  descricao; }

    public static StatusMovimento porCodigo(int codigo) {
        for (StatusMovimento status : StatusMovimento.values()) {
            if (status.codigo == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + codigo);
    }
}
