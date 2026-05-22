package ui.pessoa;

import domain.documento.CNPJ;
import domain.documento.CPF;
import domain.endereco.Endereco;
import domain.pessoa.Pessoa;
import service.PessoaService;

import java.util.Scanner;

public class ConsoleMenu {
    private Scanner scanner = new Scanner(System.in);
    private PessoaService service;

    public ConsoleMenu(PessoaService service) {
        this.service = service;
    }

    public void iniciar() {
        int opcao;

        do {
            System.out.println("\n|********** Pessoas **********|");
            System.out.println("|1 - Cadastrar          - [] X|");
            System.out.println("|2 - Atualizar                |");
            System.out.println("|3 - Remover                  |");
            System.out.println("|4 - Listar                   |");
            System.out.println("|0 - Sair                     |");
            System.out.println("|*****************************|");

            System.out.print("Opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    do {
                        System.out.println("1 - Cadastrar Pessoa Física");
                        System.out.println("2 - Cadastrar Pessoa Jurídica");

                        System.out.print("Opção: ");
                        opcao = scanner.nextInt();
                        scanner.nextLine();
                    }while (opcao != 1 && opcao != 2);

                    if (opcao == 1) {
                        cadastrarPessoaFisica();
                    } else {
                        cadastrarPessoaJuridica();
                        System.out.println();
                    }
                    break;
                case 2:
//                    removerPessoa();
                    break;
                case 3:
//                    service.listar().forEach(Pessoa::mostrarDados);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } while (opcao != 0);
    }

    private void cadastrarPessoaFisica() {
        String nome;
        String documento;
        String opcao;
        Endereco endereco = null;

        System.out.print("Nome: ");
        nome = scanner.nextLine();

        System.out.print("CPF: ");
        documento = scanner.nextLine();


        System.out.println("Deseja cadastrar o Endereço :");
        System.out.println("1 - SIM");
        System.out.println("2 - NÃO");

        opcao = scanner.nextLine();

        if (opcao.equals("1")) {
            endereco = cadastrarEndereco();
        }

        Pessoa pessoa = Pessoa.builder()
                .nome(nome)
                .documento(new CPF(documento))
                .endereco(endereco)
                .build();

        service.inserirPessoa(pessoa);

        System.out.println("Pessoa cadastrada com sucesso!");
    }

    private void cadastrarPessoaJuridica() {
        String nome;
        String documento;
        String opcao;
        Endereco endereco = null;

        System.out.print("Nome: ");
        nome = scanner.nextLine();

        System.out.print("CNPJ: ");
        documento = scanner.nextLine();


        System.out.println("Deseja cadastrar o Endereço :");
        System.out.println("1 - SIM");
        System.out.println("2 - NÃO");

        opcao = scanner.nextLine();

        if (opcao.equals("1")) {
            endereco = cadastrarEndereco();
        }

        Pessoa pessoa = Pessoa.builder()
                .nome(nome)
                .documento(new CNPJ(documento))
                .endereco(endereco)
                .build();

        service.inserirPessoa(pessoa);

        System.out.println("Pessoa cadastrada com sucesso!");
    }

    private Endereco cadastrarEndereco() {
        String cep;
        String logradouro;
        String numero;
        String bairro;
        String cidade;
        String uf;

        System.out.print("Digite o CEP:");
        cep = scanner.nextLine();

        System.out.print("Digite o logradouro:");
        logradouro = scanner.nextLine();

        System.out.print("Digite o número:");
        numero = scanner.nextLine();

        System.out.print("Digite o bairro:");
        bairro = scanner.nextLine();

        System.out.print("Digite a cidade:");
        cidade = scanner.nextLine();

        System.out.print("Digite a UF:");
        uf = scanner.nextLine();

        return Endereco.builder()
                .cep(cep)
                .logradouro(logradouro)
                .numero(numero)
                .bairro(bairro)
                .cidade(cidade)
                .uf(uf)
                .build();
    }
}

