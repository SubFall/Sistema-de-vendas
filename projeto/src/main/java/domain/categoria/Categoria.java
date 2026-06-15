package domain.categoria;

public class Categoria {
    private int id;
    private String descricao;
    private boolean ativo;

    public static final int ID_SEM_CATEGORIA = 1;

    private Categoria(int id, String descricao, boolean ativo) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public static CategoriaBuilder builder() {
        return new CategoriaBuilder();
    }

    public static final class CategoriaBuilder {
        private int id;
        private String descricao;
        private boolean ativo = true;

        public CategoriaBuilder id(int id) {
            this.id = id;
            return this;
        }

        public CategoriaBuilder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public CategoriaBuilder ativo(boolean ativo) {
            this.ativo = ativo;
            return this;
        }

        public Categoria build() {
            if (descricao == null || descricao.isBlank()) {
                throw new IllegalArgumentException("Descrição obrigatória.");
            }

            return new Categoria(id, descricao, ativo);
        }
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição obrigatória.");
        }
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
