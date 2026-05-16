package service;

import domain.pessoa.Pessoa;
import repository.PessoaRepository;

import java.util.List;

public class PessoaService {
    PessoaRepository pessoaRepository = new PessoaRepository();

    public void inserirPessoa(Pessoa pessoa) {

        String documento = pessoa.getDocumento().getValor();
        if (pessoaRepository.existeDocumento(documento)) {
            throw new IllegalArgumentException("Documento " + pessoa.getDocumento().getValor() + " já cadastrado");
        }
        pessoaRepository.inserirPessoa(pessoa);
    }

    public boolean deletarPessoa(int id) {
        return pessoaRepository.deletarPessoa(id) > 0;
    }

    public boolean atualizarPessoa(Pessoa pessoa) {
        boolean existeDocumento = pessoaRepository.existeDocumentoPorOutroId(pessoa.getDocumento().getValor(), pessoa.getId());
        if (existeDocumento) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }
        return pessoaRepository.atualizarPessoa(pessoa) > 0;
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
