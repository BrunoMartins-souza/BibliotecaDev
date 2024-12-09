import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Usuario usuarioLogado = null; // Usuário atualmente logado
        Carrinho carrinho = null; // Carrinho do usuário logado

        int opcao;

        do {
            System.out.println("\n--- Sistema de Livraria ---");
            if (usuarioLogado == null) {
                // Menu para usuários não logados
                System.out.println("1. Cadastrar Usuário");
                System.out.println("2. Fazer Login");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = input.nextInt();

                switch (opcao) {
                    case 1: // Cadastrar Usuário
                        Usuario.cadastrarUsuario();
                        break;

                    case 2: // Fazer Login
                        usuarioLogado = Usuario.login();
                        if (usuarioLogado != null) {
                            carrinho = new Carrinho(usuarioLogado);
                            System.out.println("Login realizado com sucesso!");
                        }
                        break;

                    case 0: // Sair
                        System.out.println("Encerrando o sistema. Até mais!");
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } else {
                // Menu para usuários logados
                System.out.println("1. Cadastrar Livro");
                System.out.println("2. Editar Livro");
                System.out.println("3. Remover Livro");
                System.out.println("4. Remover Usuário");
                System.out.println("5. Exibir Estoque");
                System.out.println("6. Adicionar Livro ao Carrinho");
                System.out.println("7. Remover Livro do Carrinho");
                System.out.println("8. Exibir Carrinho");
                System.out.println("9. Finalizar Compra");
                System.out.println("10. Exibir Histórico de Compras");
                System.out.println("11. Fazer Logout");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = input.nextInt();

                switch (opcao) {
                    case 1: // Cadastrar Livro
                        Livro.cadastrarLivro();
                        break;

                    case 2: // Editar Livro
                        Livro.editarLivro();
                        break;

                    case 3: // Remover Livro
                        Livro.removerLivro();
                        break;

                    case 4: // Remover Usuário
                        Usuario.removerUsuario();
                        break;

                    case 5: // Exibir Estoque
                        Livro.exibirEstoque();
                        break;

                    case 6: // Adicionar Livro ao Carrinho
                        System.out.print("\nDigite o ID do livro: ");
                        int idLivro = input.nextInt();
                        System.out.print("Digite a quantidade: ");
                        int quantidade = input.nextInt();
                        Livro livro = Livro.buscarLivroPorId(idLivro);

                        if (livro != null) {
                            carrinho.adicionarLivro(livro, quantidade);
                        }
                        break;

                    case 7: // Remover Livro do Carrinho
                        System.out.print("\nDigite o ID do livro para remover: ");
                        int idRemover = input.nextInt();
                        carrinho.removerLivro(idRemover);
                        break;

                    case 8: // Exibir Carrinho
                        carrinho.exibirCarrinho();
                        break;

                    case 9: // Finalizar Compra
                        carrinho.concluirCompra();
                        break;

                    case 10: // Exibir Histórico de Compras
                        usuarioLogado.exibirHistoricoCompras();
                        break;

                    case 11: // Fazer Logout
                        usuarioLogado = null;
                        carrinho = null;
                        System.out.println("Logout realizado com sucesso.");
                        break;

                    case 0: // Sair
                        System.out.println("Encerrando o sistema. Até mais!");
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } while (opcao != 0);

        input.close();
    }
}
