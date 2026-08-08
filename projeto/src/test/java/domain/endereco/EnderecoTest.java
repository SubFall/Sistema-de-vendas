package domain.endereco;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnderecoTest {
    @Test
    void builder_DeveLancarExcecao_QuandoCepForInvalido() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endereco.builder().cep("7800000").uf("MT").build()
        );
    }
    @Test
    void builder_DeveLancarExcecao_QuandoCepForVazio() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endereco.builder().uf("MT").build()
        );
    }
    @Test
    void builder_DeveLancarExcecao_QuandoUfForInvalido() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endereco.builder().cep("78000000").uf("MD").build()
        );
    }
    @Test
    void builder_DeveLancarExcecao_QuandoUfForVazio() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endereco.builder().cep("78000000").build()
        );
    }

}