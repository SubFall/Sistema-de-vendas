package test;

import domain.documento.CPF;
import domain.endereco.Endereco;
import domain.pessoa.Pessoa;
import domain.pessoa.PessoaPapel;
import service.EnderecoService;
import service.PessoaService;
import ui.pessoa.ConsoleMenu;

public class Principal {
    static void main(String[] args) {

        PessoaService service = new PessoaService();
        EnderecoService enderecoService = new EnderecoService();
        ConsoleMenu consoleMenu = new ConsoleMenu(service);
//
//        consoleMenu.iniciar();

        Endereco endereco = Endereco.builder()
                .logradouro("Rua teste")
                .cep("78123456")
                .bairro("teste")
                .numero("10")
                .cidade("vege")
                .uf("MT")
                .build();

        Pessoa pessoa = Pessoa.builder().nome("teste")
                .documento(new CPF("06032055111"))
                .endereco(endereco)
                .adicionarPapeis(PessoaPapel.FUNCIONARIO)
                .adicionarPapeis(PessoaPapel.CLIENTE)
                .build();

        service.inserirPessoa(pessoa);

    }
}