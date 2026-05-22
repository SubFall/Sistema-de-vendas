package service;

import conn.ConnectionFactory;
import domain.endereco.Endereco;
import domain.pessoa.Pessoa;
import repository.EnderecoRepository;
import repository.PessoaRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PessoaService {
    PessoaRepository pessoaRepository = new PessoaRepository();
    EnderecoRepository enderecoRepository = new EnderecoRepository();

//    public void inserirPessoa(Pessoa pessoa) {
//
//        String documento = pessoa.getDocumento().getValor();
//        if (pessoaRepository.existeDocumento(documento)) {
//            throw new IllegalArgumentException("Documento " + pessoa.getDocumento().getValor() + " já cadastrado");
//        }
//        pessoaRepository.inserirPessoa(pessoa);
//    }

    public void inserirPessoa(Pessoa pessoa) {

        String documento = pessoa.getDocumento().getValor();

        if (pessoaRepository.existeDocumento(documento)) {
            throw new IllegalArgumentException("Documento " + pessoa.getDocumento().getValor() + " já cadastrado");
        }

        if (pessoa.getEndereco() == null) {
            pessoaRepository.inserirPessoa(pessoa);
            return;
        }

        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();

            conn.setAutoCommit(false);

            int idPessoa = pessoaRepository.inserirPessoa(conn, pessoa);

            enderecoRepository.inserirEndereco(conn, idPessoa, pessoa.getEndereco());

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

        try {
            conn = ConnectionFactory.getConnection();

            conn.setAutoCommit(false);

            enderecoRepository.deletarEndereco(conn, id);

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

    public boolean atualizarPessoa(Pessoa pessoa) {
        boolean existeDocumento = pessoaRepository.existeDocumentoPorOutroId(pessoa.getDocumento().getValor(), pessoa.getId());
        if (existeDocumento) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }
        return pessoaRepository.atualizarPessoa(pessoa) > 0;
    }

    public boolean atualizarPessoa(Pessoa pessoa, Endereco endereco) {

        boolean existeDocumento = pessoaRepository.existeDocumentoPorOutroId(pessoa.getDocumento().getValor(), pessoa.getId());

        if (existeDocumento) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }

        if (endereco == null) {
            return pessoaRepository.atualizarPessoa(pessoa) > 0;
        }

        int row;
        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();

            conn.setAutoCommit(false);

            row = pessoaRepository.atualizarPessoa(conn, pessoa);
            enderecoRepository.atualizarEndereco(conn, endereco);

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

    public List<Pessoa> buscarPorNome(String nome) {
        return pessoaRepository.buscarPorNome(nome);
    }

    public List<Pessoa> buscarTodos() {
        return pessoaRepository.buscarTodos();
    }

}
