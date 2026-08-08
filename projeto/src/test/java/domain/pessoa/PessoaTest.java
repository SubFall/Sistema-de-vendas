package domain.pessoa;

import domain.documento.CNPJ;
import domain.documento.CPF;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PessoaTest {
    @Test
    void builder_DeveLancarExcecao_QuandoNomeForVazio() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Pessoa.builder().documento(new CPF("12345678910")).build()
        );
    }
    @Test
    void builder_DeveLancarExcecao_CpfInvalido() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Pessoa.builder().nome("teste").documento(new CPF("1234567891")).build(), "CPF Inválido"
        );
    }
    @Test
    void builder_DeveLancarExcecao_CnpjInvalido() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Pessoa.builder().nome("teste").documento(new CNPJ("1234567891012")).build(),
                "CNPJ Inválido"
        );
    }
    @Test
    void builder_DeveLancarExcecao_QuandoDocumentoForVazio() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Pessoa.builder().nome("teste").build(),
                "CNPJ Inválido"
        );
    }
}