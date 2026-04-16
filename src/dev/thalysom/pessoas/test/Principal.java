package dev.thalysom.pessoas.test;

import dev.thalysom.pessoas.repository.PessoaRepository;
import dev.thalysom.pessoas.service.PessoaService;
import dev.thalysom.pessoas.ui.ConsoleMenu;

public class Principal {
    public static void main(String[] args) {
        PessoaRepository repository = new PessoaRepository();
        PessoaService service = new PessoaService(repository);

        ConsoleMenu menu = new ConsoleMenu(service);
        menu.iniciar();
    }
}