package service;

import conn.ConnectionProvider;
import domain.pessoa.Pessoa;
import domain.pessoa.PessoaPapel;
import repository.EnderecoRepository;
import repository.PessoaPapelRepository;
import repository.PessoaRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PessoaService {
    private final ConnectionProvider connectionProvider;
    private final PessoaRepository pessoaRepository;
    private final PessoaPapelRepository pessoaPapelRepository;
    private final EnderecoRepository enderecoRepository;

    public PessoaService(
            ConnectionProvider connectionProvider,
            PessoaRepository pessoaRepository,
            PessoaPapelRepository pessoaPapelRepository,
            EnderecoRepository enderecoRepository
    ) {
        this.connectionProvider = connectionProvider;
        this.pessoaRepository = pessoaRepository;
        this.pessoaPapelRepository = pessoaPapelRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public void inserirPessoa(Pessoa pessoa) {

        var documento = pessoa.getDocumento().getValor();

        if (pessoaRepository.existeDocumento(documento)) {
            throw new IllegalArgumentException("Documento " + pessoa.getDocumento().getValor() + " já cadastrado");
        }
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();

            conn.setAutoCommit(false);

            var idPessoa = pessoaRepository.inserirPessoa(conn, pessoa);

            for (PessoaPapel pessoaPapel : pessoa.getPapeis()) {
                pessoaPapelRepository.inserirPessoaPapel(conn, idPessoa, pessoaPapel.getCodigo());
            }

            if (pessoa.getEndereco() != null) {
                enderecoRepository.inserirEndereco(conn, idPessoa, pessoa.getEndereco());
            }

            conn.commit();
        } catch (SQLException | RuntimeException e) {
            rollback(conn);

            throw new RuntimeException("Erro ao inserir Pessoa", e);
        } finally {
            fechar(conn);
        }
    }

    public boolean deletarPessoa(int idPessoa) {

        int row = 0;
        Connection conn = null;

        verificarPessoaPadrao(idPessoa);

        try {
            conn = connectionProvider.getConnection();

            conn.setAutoCommit(false);

            enderecoRepository.deletarEndereco(conn, idPessoa);

            pessoaPapelRepository.deletarPessoaPapel(conn, idPessoa);

            row = pessoaRepository.deletarPessoa(conn, idPessoa);

            conn.commit();
        } catch (SQLException | RuntimeException e) {
            rollback(conn);

            throw new RuntimeException("Erro ao deletar Pessoa", e);
        } finally {
            fechar(conn);
        }
        return row > 0;
    }

    public boolean inativarAtivarPessoa(int idPessoa) {

        verificarPessoaPadrao(idPessoa);

        var pessoa = validarPessoaAtivo(pessoaRepository.buscarPorId(idPessoa));

        return pessoaRepository.inativarPessoa(!pessoa.isAtivo(), idPessoa);
    }

    public boolean atualizarPessoa(Pessoa pessoa) {

        verificarDocumentoJaCadastrado(pessoa);

        int row;
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();

            conn.setAutoCommit(false);

            row = pessoaRepository.atualizarPessoa(conn, pessoa);

            pessoaPapelRepository.deletarPessoaPapel(conn, pessoa.getId());

            for (PessoaPapel p : pessoa.getPapeis()) {
                pessoaPapelRepository.inserirPessoaPapel(conn, pessoa.getId(), p.getCodigo());
            }

            if (pessoa.getEndereco() != null) {

                var isExisteEndereco = enderecoRepository.existePessoaEndereco(conn, pessoa.getId());

                if (isExisteEndereco) {
                    enderecoRepository.atualizarEndereco(conn, pessoa.getEndereco());
                } else {
                    enderecoRepository.inserirEndereco(conn, pessoa.getId(), pessoa.getEndereco());
                }

            } else {
                enderecoRepository.deletarEndereco(conn, pessoa.getId());
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            throw new RuntimeException("Erro ao atualizar pessoa", e);
        } finally {
            fechar(conn);
        }
        return row > 0;
    }

    public Pessoa buscarPorDocumento(String documento) {
        var pessoa = pessoaRepository.buscarPorDocumento(documento);
        if (pessoa == null) {
            throw new IllegalArgumentException("Documento não existe");
        }
        return pessoa;
    }

    public Pessoa buscarPorId(int id) {
        var pessoa = validarPessoaExiste(pessoaRepository.buscarPorId(id));

        pessoa.setPapeis(pessoaPapelRepository.buscarPorIdPessoa(id));

        pessoa.setEndereco(enderecoRepository.buscarPoridPessoa(id));

        return pessoa;
    }

    public Pessoa buscarPorIdAtivo(int id) {
        var pessoa = validarPessoaAtivo(pessoaRepository.buscarPorId(id));

        pessoa.setPapeis(pessoaPapelRepository.buscarPorIdPessoa(id));

        pessoa.setEndereco(enderecoRepository.buscarPoridPessoa(id));

        return pessoa;
    }

    public List<Pessoa> buscarPorNome(String nome) {
        return pessoaRepository.buscarPorNome(nome);
    }

    public List<Pessoa> buscarTodos() {
        return pessoaRepository.buscarTodos();
    }

    public List<Pessoa> buscarTodosAtivo() {
        return pessoaRepository.buscarTodosAtivo();
    }

    public List<Pessoa> buscarPessoaPorPapelAtivo(PessoaPapel pessoaPapel) {
        return pessoaRepository.buscarPessoaPorPapelAtivo(pessoaPapel);
    }

    private void verificarDocumentoJaCadastrado(Pessoa pessoa) {
        var isExisteDocumento = pessoaRepository.existeDocumentoPorOutroId(pessoa.getDocumento().getValor(), pessoa.getId());

        if (isExisteDocumento) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }
    }

    private void verificarPessoaPadrao(int idPessoa) {
        if (idPessoa == Pessoa.ID_PESSOA_PADRAO) {
            throw new IllegalArgumentException("A Pessoa padrão não pode ser alterada/deletada.");
        }
    }

    private Pessoa validarPessoaExiste(Pessoa pessoa) {
        if (pessoa == null) {
            throw new IllegalArgumentException("Pessoa Não existe");
        }
        return pessoa;
    }

    private Pessoa validarPessoaAtivo(Pessoa pessoa) {
        validarPessoaExiste(pessoa);

        if (!pessoa.isAtivo()) {
            throw new IllegalArgumentException("Pessoa inativa!");
        }
        return pessoa;
    }

    private void fechar(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar a conexão", e);
        }
    }

    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao realizar rollback", ex);
        }
    }
}
