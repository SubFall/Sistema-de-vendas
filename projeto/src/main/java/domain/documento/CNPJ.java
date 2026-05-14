package domain.documento;

import java.util.Objects;

public final class CNPJ implements Documento {
    private final String valor;

    public CNPJ(String valor) {
        valor = valor.replaceAll("\\D", "");

        if (valor.length() != 14) {
            throw new IllegalArgumentException("CNPJ Inválido");
        }

        this.valor = valor;
    }

    @Override
    public String getValor() {
        return this.valor;
    }

    @Override
    public TipoPessoa getTipo() {
        return TipoPessoa.JURIDICA;
    }

    @Override
    public String toString() {
        return this.valor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CNPJ cnpj = (CNPJ) o;
        return Objects.equals(valor, cnpj.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(valor);
    }
}
