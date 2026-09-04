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

        String documento = pessoa.getDocumento().getValor();

        if (pessoaRepository.existeDocumento(documento)) {
            throw new IllegalArgumentException("Documento " + pessoa.getDocumento().getValor() + " já cadastrado");
        }
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();

            conn.setAutoCommit(false);

            int idPessoa = pessoaRepository.inserirPessoa(conn, pessoa);

            for (PessoaPapel pessoaPapel : pessoa.getPessoaPapel()) {
                pessoaPapelRepository.inserirPessoaPapel(conn, idPessoa, pessoaPapel.getCodigo());
            }

            if (pessoa.getEndereco() != null) {
                enderecoRepository.inserirEndereco(conn, idPessoa, pessoa.getEndereco());
            }

            conn.commit();
        } catch (SQLException e) {

            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao realizar rollback", ex);
            }

            throw new RuntimeException("Erro ao inserir pessoa com endereço", e);
        } finally {
            try {

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexão", e);
            }
        }
    }

    public boolean deletarPessoa(int id) {

        int row = 0;
        Connection conn = null;

        if (id == Pessoa.ID_PESSOA_PADRAO) {
            throw new IllegalArgumentException("A Pessoa padrão não pode ser alterada/deletada.");
        }

        try {
            conn = connectionProvider.getConnection();

            conn.setAutoCommit(false);

            enderecoRepository.deletarEndereco(conn, id);

            pessoaPapelRepository.deletarPessoaPapel(conn, id);

            row = pessoaRepository.deletarPessoa(conn, id);

            conn.commit();
        } catch (SQLException e) {

            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao realizar rollback", ex);
            }
            throw new RuntimeException("Erro ao deletar Pessoa", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexão", e);
            }
        }
        return row > 0;
    }

    public boolean inativarPessoa(int idPessoa) {
        Pessoa pessoa = validarPessoaAtivo(pessoaRepository.buscarPorId(idPessoa));

        return pessoaRepository.inativarPessoa(!pessoa.isAtivo(), idPessoa);
    }

    public boolean atualizarPessoa(Pessoa pessoa) {

        boolean existeDocumento = pessoaRepository.existeDocumentoPorOutroId(pessoa.getDocumento().getValor(), pessoa.getId());

        if (existeDocumento) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }

        int row;
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();

            conn.setAutoCommit(false);

            row = pessoaRepository.atualizarPessoa(conn, pessoa);

            pessoaPapelRepository.deletarPessoaPapel(conn, pessoa.getId());

            for (PessoaPapel p : pessoa.getPessoaPapel()) {
                pessoaPapelRepository.inserirPessoaPapel(conn, pessoa.getId(), p.getCodigo());
            }

            if (pessoa.getEndereco() != null) {

                boolean existeEndereco = enderecoRepository.existePessoaEndereco(conn, pessoa.getId());

                if (existeEndereco) {
                    enderecoRepository.atualizarEndereco(conn, pessoa.getEndereco());
                } else {
                    enderecoRepository.inserirEndereco(conn, pessoa.getId(), pessoa.getEndereco());
                }

            } else {
                enderecoRepository.deletarEndereco(conn, pessoa.getId());
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao realizar o rollback", ex);
            }
            throw new RuntimeException("Erro ao atualizar pessoa", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar a conexão", e);
            }
        }
        return row > 0;
    }

    public Pessoa buscarPorDocumento(String documento) {
        Pessoa pessoa = pessoaRepository.buscarPorDocumento(documento);
        if (pessoa == null) {
            throw new IllegalArgumentException("Documento não existe");
        }
        return pessoa;
    }

    public Pessoa buscarPorId(int id) {
        Pessoa pessoa = validarPessoaExiste(pessoaRepository.buscarPorId(id));

        pessoa.setPapeis(pessoaPapelRepository.buscarPorIdPessoa(id));

        pessoa.setEndereco(enderecoRepository.buscarPoridPessoa(id));

        return pessoa;
    }

    public Pessoa buscarPorIdAtivo(int id) {
        Pessoa pessoa = validarPessoaAtivo(pessoaRepository.buscarPorId(id));

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
}
