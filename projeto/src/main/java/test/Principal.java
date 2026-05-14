package test;


import domain.documento.CNPJ;
import domain.documento.CPF;
import domain.endereco.Endereco;
import domain.pessoa.Pessoa;
import repository.PessoaRepository;
//import repository.PessoaRepository;
import service.PessoaService;
//import ui.ConsoleMenu;

public class Principal {
    static void main(String[] args) {
//        PessoaRepository repository = new PessoaRepository();
        PessoaService service = new PessoaService();
//
//        ConsoleMenu menu = new ConsoleMenu(service);
//        menu.iniciar();
        Pessoa thalysom = Pessoa.builder().nome("thalysom").documento(new CPF("06032055184"))
                //.endereco(new Endereco("teste","teste","teste", "teste", "teste"))
                .build();
//        System.out.println(thalysom);

        Endereco build = Endereco.builder()
                .logradouro("Rua girassol")
                .cep("78670140")
                .uf("MT")
                .cidade("Várzea Grande")
                .numero("20")
                .build();

        thalysom.setEndereco(build);

        Pessoa hetosoft = Pessoa.builder().nome("hetosoft 2").documento(new CNPJ("01123456000180")).build();

//        service.inserirPessoa(hetosoft);
//        service.inserirPessoa(thalysom);

        Pessoa pessoa = service.buscarPorDocumento("01123456000180");
        System.out.println(pessoa);

        System.out.println(service.buscarTodos());

    }
}