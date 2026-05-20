package test;

import domain.documento.CPF;
import domain.endereco.Endereco;
import domain.pessoa.Pessoa;
import service.EnderecoService;
import service.PessoaService;
//import ui.ConsoleMenu;

public class Principal {
    static void main(String[] args) {

        PessoaService service = new PessoaService();
        EnderecoService enderecoService = new EnderecoService();

        Pessoa thalysom = Pessoa.builder()
                .id(20)
                .nome("thalysom s2")
                .documento(new CPF("06032055184"))
                .endereco(null)
                .build();

        Endereco endereco = Endereco.builder()
                .id(12)
                .logradouro("Rua Girassol")
                .cidade("Várzea Grande")
                .uf("MT")
                .bairro("Mapim")
                .numero("20")
                .cep("78123456")
                .idPessoa(20)
                .build();

        thalysom.setEndereco(endereco);
//        service.inserirPessoa(thalysom, endereco);
//        service.deletarPessoa(15);
        service.atualizarPessoa(thalysom, thalysom.getEndereco());
    }
}