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
                .nome("thalysom gostoso")
                .documento(new CPF("06032055183"))
                .endereco(null)
                .build();

        Endereco endereco = Endereco.builder()
                .logradouro("Rua Girassol")
                .cidade("Várzea Grande")
                .uf("MT")
                .bairro("Mapim")
                .numero("20")
                .cep("78123456")
                .build();

        service.inserirPessoa(thalysom, endereco);
    }
}