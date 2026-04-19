package dev.thalysom.pessoas.service;

import dev.thalysom.pessoas.domain.Pessoa;
import dev.thalysom.pessoas.repository.PessoaRepository;

import java.util.List;

public class PessoaService {

    private PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Pessoa pessoa) {
        repository.salvar(pessoa);
    }

    public void remover(String documento) {
        repository.remover(documento);
    }

    public List<Pessoa> listar() {
        return repository.listar();
    }

}
