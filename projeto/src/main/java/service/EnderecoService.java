package service;

import domain.endereco.Endereco;
import repository.EnderecoRepository;

import java.util.List;

public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public int atualizarEndereco(Endereco endereco) {
        return enderecoRepository.atualizarEndereco(endereco);
    }

    public boolean deletarEndereco(int id) {
        return enderecoRepository.deletarEndereco(id) > 0;
    }

    public List<Endereco> buscarTodos() {
        return enderecoRepository.buscarTodos();
    }

    public Endereco buscarPorId(int id) {
        return enderecoRepository.buscarPorId(id);
    }

    public List<Endereco> buscarPorCidade(String cidade) {
        return enderecoRepository.buscarPorCidade(cidade);
    }

    public List<Endereco> buscarPorCep(String cep) {
        return enderecoRepository.buscarPorCep(cep);
    }
}
