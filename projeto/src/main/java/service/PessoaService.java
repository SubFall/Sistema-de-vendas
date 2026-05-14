package service;

import domain.pessoa.Pessoa;
import repository.PessoaRepository;

import java.util.List;

public class PessoaService {
    PessoaRepository pessoaRepository = new PessoaRepository();

//    private PessoaRepository repository;
//
//    public PessoaService(PessoaRepository repository) {
//        this.repository = repository;
//    }
//
    public void inserirPessoa(Pessoa pessoa) {

        String documento = pessoa.getDocumento().getValor();
        if (pessoaRepository.existeDocumento(documento)) {
            throw new IllegalArgumentException("Documento " + pessoa.getDocumento().getValor() + " já cadastrado");
        }
        pessoaRepository.inserirPessoa(pessoa);
    }

    public Pessoa buscarPorDocumento(String documento) {
        Pessoa pessoa = pessoaRepository.buscarPorDocumento(documento);
        if (pessoa == null) {
            throw new IllegalArgumentException("Documento não existe");
        }
        return pessoa;
    }

    public List<Pessoa> buscarTodos() {
        return pessoaRepository.buscarTodos();
    }
//
//    public void remover(String documento) {
//        repository.remover(documento);
//    }
//
//    public List<Pessoa> listar() {
//        return repository.listar();
//    }
}
