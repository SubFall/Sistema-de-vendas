package domain.ajusteestoque;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AjusteEstoqueTest {
    @Test
    void builder_DeveLancarExcecao_QuandoTituloForVazio() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> AjusteEstoque.builder().titulo("").status(Status.ABERTO).build()
        );
    }

    @Test
    void builder_DeveLancarExcecao_QuandoTituloForNull() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> AjusteEstoque.builder().status(Status.ABERTO).build()
        );
    }

    @Test
    void builder_DeveLancarExcecao_StatusForNull() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> AjusteEstoque.builder().titulo("teste").build()
        );
    }


}