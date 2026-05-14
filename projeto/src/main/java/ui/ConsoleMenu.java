//package ui;
//
////import domain.pessoa.Endereco;
////import domain.pessoa.Pessoa;
////import domain.pessoa.PessoaFisica;
////import domain.pessoa.PessoaJuridica;
//import service.PessoaService;
//import util.ConsoleUtils;
//
//import java.time.LocalDate;
//import java.util.Scanner;
//
//public class ConsoleMenu {
//    private Scanner scanner = new Scanner(System.in);
//    private PessoaService service;
//
//    public ConsoleMenu(PessoaService service) {
//        this.service = service;
//    }
//
////    public void iniciar() {
//////        service.teste();
////        int opcao;
////
////        do {
////            System.out.println("\n===== MENU =====");
////            System.out.println("1 - Cadastrar Pessoa");
////            System.out.println("2 - Remover Pessoa");
////            System.out.println("3 - Listar");
////            System.out.println("0 - Sair");
////
////            System.out.print("Escolha: ");
////            opcao = scanner.nextInt();
////            scanner.nextLine();
////
////            switch (opcao) {
////                case 1:
////                    do {
////                        System.out.println("1 - Cadastrar Pessoa Física");
////                        System.out.println("2 - Cadastrar Pessoa Jurídica");
////
////                        System.out.print("Escolha: ");
////                        opcao = scanner.nextInt();
////                        scanner.nextLine();
////                    }while (opcao != 1 && opcao != 2);
////
////                    if (opcao == 1) {
////                        cadastrarPessoaFisica();
////                    } else {
////                        cadastrarPessoaJuridica();
////                    }
////                    break;
////                case 2:
////                    removerPessoa();
////                    break;
////                case 3:
////                    service.listar().forEach(Pessoa::mostrarDados);
////                    break;
////                case 0:
////                    System.out.println("Saindo...");
////                    break;
////                default:
////                    System.out.println("Opção inválida");
////            }
////
////        } while (opcao != 0);
////    }
////
////    private void cadastrarPessoaFisica() {
////        PessoaFisica p = new PessoaFisica();
////
////        System.out.print("Nome: ");
////        p.setNome(scanner.nextLine());
////
////        boolean documentoValido = false;
////
////        while (!documentoValido) {
////            try {
////                System.out.print("CPF: ");
////                p.setDocumento(scanner.nextLine());
////
////                System.out.print("Digite a data de nascimento (DD/MM/YYYY): ");
////                LocalDate data = ConsoleUtils.lerData(scanner);
////                p.setAnoNascimento(data);
////
////                System.out.println("Deseja cadastrar o Endereço :");
////                System.out.println("1 - SIM");
////                System.out.println("2 -  NÃO");
////
////                int op = scanner.nextInt();
////                scanner.nextLine();
////
////                if (op == 1) {
////                    p.adicionarEndereco(cadastrarEndereco());
////                }
////
////                documentoValido = true;
////
////            } catch (IllegalArgumentException e) {
////                System.out.println("Erro: " + e.getMessage());
////            }
////        }
////
////        service.cadastrar(p);
////        System.out.println("Pessoa cadastrada com sucesso!");
////    }
////
////    private void cadastrarPessoaJuridica() {
////        PessoaJuridica p = new PessoaJuridica();
////
////        System.out.print("Nome: ");
////        p.setNome(scanner.nextLine());
////
////        boolean documentoValido = false;
////
////        while (!documentoValido) {
////            try {
////                System.out.print("CNPJ: ");
////                p.setDocumento(scanner.nextLine());
////
////                System.out.println("Deseja cadastrar o Endereço :");
////                System.out.println("1 - SIM");
////                System.out.println("2 -  NÃO");
////
////                int op = scanner.nextInt();
////                scanner.nextLine();
////
////                if (op == 1) {
////                    p.adicionarEndereco(cadastrarEndereco());
////                }
////
////                documentoValido = true;
////
////            } catch (IllegalArgumentException e) {
////                System.out.println("Erro: " + e.getMessage());
////            }
////        }
////
////        service.cadastrar(p);
////        System.out.println("Pessoa cadastrada com sucesso!");
////    }
////
////    private void removerPessoa() {
////        System.out.print("Digite o Documento: ");
////        String documento = scanner.nextLine();
////
////        try {
////            service.remover(documento);
////            System.out.println("Pessoa removida com sucesso!");
////        } catch (IllegalArgumentException e){
////            System.out.println("Erro: " + e.getMessage());
////        }
////    }
////
////    private Endereco cadastrarEndereco() {
////        Endereco endereco = new Endereco();
////
////        System.out.print("Logradouro: ");
////        endereco.setLogradouro(scanner.nextLine());
////
////        System.out.print("Nº: ");
////        endereco.setNumero(scanner.nextLine());
////
////        System.out.print("Cidade: ");
////        endereco.setCidade(scanner.nextLine());
////
////        System.out.print("UF: ");
////        endereco.setUf(scanner.nextLine());
////
////        System.out.print("CEP: ");
////        endereco.setCep(scanner.nextLine());
////
////        return endereco;
////    }
//}
//
