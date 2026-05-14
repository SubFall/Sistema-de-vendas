package domain.documento;

import java.util.Objects;

public final class CPF implements Documento {
    private final String valor;

    public CPF(String valor) {
        valor = valor.replaceAll("\\D", "");

        if (valor.length() != 11) {
            throw new IllegalArgumentException("CPF Inválido");
        }

        this.valor = valor;
    }

    @Override
    public String getValor() {
        return this.valor;
    }

    @Override
    public TipoPessoa getTipo() {
        return TipoPessoa.FISICA;
    }

    @Override
    public String toString() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CPF cpf = (CPF) o;
        return Objects.equals(valor, cpf.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(valor);
    }
}
