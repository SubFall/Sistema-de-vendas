package test;

import service.EnderecoService;
import service.PessoaService;
import ui.pessoa.ConsoleMenu;

public class Principal {
    static void main(String[] args) {

        PessoaService service = new PessoaService();
        EnderecoService enderecoService = new EnderecoService();
        ConsoleMenu consoleMenu = new ConsoleMenu(service);

        consoleMenu.iniciar();

//        Pessoa thalysom = Pessoa.builder()
////                .id(20)
//                .nome("hetosoft")
//                .documento(new CNPJ("00123456000120"))
//                .endereco(null)
//                .build();
//
//        Endereco endereco = Endereco.builder()
//                .id(12)
//                .logradouro("Rua Filinto Muller")
//                .cidade("Várzea Grande")
//                .uf("MT")
//                .bairro("Centro")
//                .numero("100")
//                .cep("78321610")
////                .idPessoa(20)
//                .build();
//
//        thalysom.setEndereco(endereco);
//        service.inserirPessoa(thalysom, endereco);
//        service.deletarPessoa(15);
//        service.atualizarPessoa(thalysom, thalysom.getEndereco());
    }
}