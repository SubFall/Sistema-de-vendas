package service;

import conn.ConnectionProvider;
import domain.documento.CPF;
import domain.endereco.Endereco;
import domain.pessoa.Pessoa;
import domain.pessoa.PessoaPapel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.EnderecoRepository;
import repository.PessoaPapelRepository;
import repository.PessoaRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class PessoaServiceTest {
    @Mock
    private ConnectionProvider connectionProvider;
    @Mock
    private Connection connection;
    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    private PessoaPapelRepository pessoaPapelRepository;
    @Mock
    private EnderecoRepository enderecoRepository;

    private Pessoa pessoa;

    @InjectMocks
    private PessoaService service;

    @BeforeEach
    void init() {
        var cpf = new CPF("12345678910");
        var pessoaPapelList = List.of(PessoaPapel.CLIENTE, PessoaPapel.FUNCIONARIO);
        var endereco = Endereco.builder().id(1).logradouro("Rua Brasil").bairro("Centro").numero("10")
                .cep("78000000").cidade("Várzea Grande").uf("MT").build();
        pessoa = Pessoa.builder().id(1).nome("Thalysom").documento(cpf).papeis(pessoaPapelList).endereco(endereco).ativo(true).build();
    }

    @Test
    @Order(1)
    void inserirAjusteEstoque_QuandoForBemSucedido() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(pessoaRepository.existeDocumento(pessoa.getDocumento().getValor())).thenReturn(false);

        BDDMockito.when(pessoaRepository.inserirPessoa(BDDMockito.eq(connection), BDDMockito.eq(pessoa))).thenReturn(1);

        service.inserirPessoa(pessoa);

        BDDMockito.verify(pessoaPapelRepository, BDDMockito.times(2))
                .inserirPessoaPapel(
                        BDDMockito.eq(connection),
                        BDDMockito.anyInt(),
                        BDDMockito.anyInt()
                );

        BDDMockito.verify(enderecoRepository).inserirEndereco(
                BDDMockito.eq(connection),
                BDDMockito.anyInt(),
                BDDMockito.eq(pessoa.getEndereco())
        );

        BDDMockito.verify(connection).commit();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(2)
    void inserirAjusteEstoque_inserirPessoa_DeveLancarExcecao() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(pessoaRepository.existeDocumento(pessoa.getDocumento().getValor())).thenReturn(false);

        BDDMockito.when(pessoaRepository.inserirPessoa(BDDMockito.eq(connection), BDDMockito.eq(pessoa))).thenThrow(SQLException.class);

        Assertions.assertThatException()
                .isThrownBy(() -> service.inserirPessoa(pessoa))
                .isInstanceOf(RuntimeException.class)
                .withMessage("Erro ao inserir pessoa com endereço");

        BDDMockito.verify(pessoaPapelRepository, BDDMockito.never()).inserirPessoaPapel(
                BDDMockito.eq(connection),
                BDDMockito.anyInt(),
                BDDMockito.anyInt()
        );

        BDDMockito.verify(enderecoRepository, BDDMockito.never()).inserirEndereco(
                BDDMockito.eq(connection),
                BDDMockito.anyInt(),
                BDDMockito.eq(pessoa.getEndereco())
        );

        BDDMockito.verify(connection, BDDMockito.never()).commit();
        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();

    }

//    @Test
//    @Order(3)
//    void inserirAjusteEstoque_inserirPessoaPapel_DeveLancarExcecao() throws SQLException {
//        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
//        BDDMockito.when(pessoaRepository.existeDocumento(pessoa.getDocumento().getValor())).thenReturn(false);
//
//        BDDMockito.when(pessoaRepository.inserirPessoa(BDDMockito.eq(connection), BDDMockito.eq(pessoa))).thenReturn(1);
//
//        Assertions.assertThatException()
//                .isThrownBy(() -> service.inserirPessoa(pessoa))
//                .isInstanceOf(RuntimeException.class)
//                .withMessage("Erro ao inserir pessoa com endereço");
//
//        BDDMockito.verify(pessoaPapelRepository, BDDMockito.never()).inserirPessoaPapel(
//                BDDMockito.eq(connection),
//                BDDMockito.anyInt(),
//                BDDMockito.anyInt()
//        );
//
//        BDDMockito.verify(enderecoRepository, BDDMockito.never()).inserirEndereco(
//                BDDMockito.eq(connection),
//                BDDMockito.anyInt(),
//                BDDMockito.eq(pessoa.getEndereco())
//        );
//
//        BDDMockito.verify(connection, BDDMockito.never()).commit();
//        BDDMockito.verify(connection).rollback();
//        BDDMockito.verify(connection).close();
//
//    }
}