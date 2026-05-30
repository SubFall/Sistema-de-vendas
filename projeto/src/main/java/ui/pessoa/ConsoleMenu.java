package ui.pessoa;

import domain.documento.CNPJ;
import domain.documento.CPF;
import domain.endereco.Endereco;
import domain.pessoa.Pessoa;
import domain.pessoa.PessoaPapel;
import service.PessoaService;
import util.ConsoleUtils;

import java.util.ArrayList;
import java.util.List;
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
            System.out.println("|2 - Remover                  |");
            System.out.println("|3 - Atualizar                |");
            System.out.println("|4 - Detalhes de Pessoa       |");
            System.out.println("|5 - Listar                   |");
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
                    } while (opcao != 1 && opcao != 2);

                    if (opcao == 1) {
                        cadastrarPessoaFisica();
                    } else {
                        cadastrarPessoaJuridica();
                        System.out.println();
                    }
                    break;
                case 2:
                    removerPessoa();
                    break;
                case 3:
//                    service.listar().forEach(Pessoa::mostrarDados);
                    break;
                case 4:
                    System.out.println("Digite o ID da pessoa: ");
                    try {
                        detalhesPessoa(ConsoleUtils.lerInteiro(scanner, "ID"));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    listarPesso();
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
        List<PessoaPapel> pessoaPapels;
        Endereco endereco = null;

        System.out.print("Nome: ");
        nome = scanner.nextLine();

        System.out.print("CPF: ");
        documento = scanner.nextLine();

        pessoaPapels = cadastrarPapel();

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
                .papeis(pessoaPapels)
                .endereco(endereco)
                .build();

        service.inserirPessoa(pessoa);

        System.out.println("Pessoa cadastrada com sucesso!");
    }

    private void cadastrarPessoaJuridica() {
        String nome;
        String documento;
        String opcao;
        List<PessoaPapel> pessoaPapels;
        Endereco endereco = null;

        System.out.print("Nome: ");
        nome = scanner.nextLine();

        System.out.print("CNPJ: ");
        documento = scanner.nextLine();

        pessoaPapels = cadastrarPapel();

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
                .papeis(pessoaPapels)
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

    public void removerPessoa() {
        int id;
        int opcao;
        listarPesso();

        System.out.print("Digite o ID da pessoa para remover:");
        id = ConsoleUtils.lerInteiro(scanner, "ID");

        try {
            Pessoa pessoa = service.buscarPorId(id);
            System.out.println("Tem certeza que deseja Excluir " + pessoa.getNome() + " ?");

            do {
                System.out.println("1 - SIM");
                System.out.println("2 - NÃO");

                opcao = ConsoleUtils.lerInteiro(scanner, "Opção");
            } while (opcao != 1 && opcao != 2);

            if (opcao != 1) {
                return;
            }
            boolean deletado = service.deletarPessoa(id);

            if (deletado) {
                System.out.println("Pessoa deletada com sucesso!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void detalhesPessoa(int id) {
        Pessoa pessoa = service.buscarPorId(id);
        System.out.println("ID: " + pessoa.getId());
        System.out.println("Nome: " + pessoa.getNome());
        System.out.println("Documento: " + pessoa.getDocumento());
        System.out.println("Tipo: " + pessoa.getDocumento().getTipo());

        System.out.println();

        System.out.println("Papéis:");
        for (PessoaPapel pessoaPapel : pessoa.getPessoaPapel()) {
            System.out.println("- " + pessoaPapel.getDescricao());
        }

        System.out.println();

        if (pessoa.getEndereco() != null) {
            System.out.println("Endereço:");
            System.out.println("Rua: " + pessoa.getEndereco().getLogradouro());
            System.out.println("Número: " + pessoa.getEndereco().getNumero());
            System.out.println(pessoa.getEndereco().getBairro());
            System.out.println(pessoa.getEndereco().getCidade() + " - " + pessoa.getEndereco().getUf());
            System.out.println("CEP " + pessoa.getEndereco().getCep());
        } else {
            System.out.println("Endereço não cadastrado.");
        }

    }

    public void listarPesso() {
        List<Pessoa> pessoas = service.buscarTodos();
        String id;
        String nome;
        String doc;
        String tipo;

        System.out.printf(
                "%-5s | %-20s | %-14s | %-10s%n",
                "ID", "NOME", "DOCUMENTO", "TIPO"
        );
        for (Pessoa p : pessoas) {
            id = ConsoleUtils.formatarColuna(String.valueOf(p.getId()), 5);
            nome = ConsoleUtils.formatarColuna(p.getNome(), 20);
            doc = ConsoleUtils.formatarColuna(p.getDocumento().getValor(), 14);
            tipo = p.getDocumento().getTipo().getCodigo() == 0 ? "Física" : "Jurídica";

            System.out.println(id + " | " + nome + " | " + doc + " | " + tipo);

        }
    }

    private List<PessoaPapel> cadastrarPapel() {
        List<PessoaPapel> pessoaPapels = new ArrayList<>();

        for (PessoaPapel papel : PessoaPapel.values()) {
            System.out.println("A pessoa é " + papel.getDescricao() + " ?");
            System.out.println("1 - Sim");
            System.out.println("2 - Não");
            if (ConsoleUtils.lerInteiro(scanner, "valor") == 1) {
                pessoaPapels.add(papel);
            }
        }
        return pessoaPapels;
    }
}

