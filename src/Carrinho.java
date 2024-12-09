import java.util.ArrayList;

public class Carrinho {
    private Usuario usuario; // Associado ao usuário que está usando o carrinho
    private ArrayList<Livro> itens; // Livros adicionados ao carrinho
    private ArrayList<Integer> quantidades; // Quantidade de cada livro no carrinho

    // ---- Construtor ----
    public Carrinho(Usuario usuario) {
        this.usuario = usuario;
        this.itens = new ArrayList<>();
        this.quantidades = new ArrayList<>();
    }

    // ---- Métodos ----

    // Adicionar livro ao carrinho
    public void adicionarLivro(Livro livro, int quantidade) {
        if (livro.getQuantidadeEstoque() >= quantidade) {
            int index = itens.indexOf(livro);

            if (index != -1) { // Livro já no carrinho
                quantidades.set(index, quantidades.get(index) + quantidade);
            } else {
                itens.add(livro);
                quantidades.add(quantidade);
            }

            System.out.println("Livro \"" + livro.getTituloLivro() + "\" adicionado ao carrinho!");
        } else {
            System.out.println("Estoque insuficiente para \"" + livro.getTituloLivro() + "\". Disponível: " + livro.getQuantidadeEstoque());
        }
    }

    // Remover livro do carrinho
    public void removerLivro(int id) {
        exibirCarrinho();

        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getIdLivro() == id) {
                Livro livro = itens.get(i);
                int quantidadeAtual = quantidades.get(i);
                System.out.println("Livro encontrado: \"" + livro.getTituloLivro() + "\" no carrinho com " + quantidadeAtual + " unidade(s).");

                // Solicitar ao usuário quantas unidades remover
                System.out.println("Quantas unidades deseja remover? (Digite 0 para cancelar ou " + quantidadeAtual + " para remover tudo)");
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                int quantidadeParaRemover = scanner.nextInt();

                if (quantidadeParaRemover <= 0) {
                    System.out.println("Operação cancelada.");
                    return;
                }

                if (quantidadeParaRemover >= quantidadeAtual) {
                    // Remove o livro completamente
                    itens.remove(i);
                    quantidades.remove(i);
                    System.out.println("Todas as unidades de \"" + livro.getTituloLivro() + "\" foram removidas do carrinho.");
                } else {
                    // Atualiza a quantidade
                    quantidades.set(i, quantidadeAtual - quantidadeParaRemover);
                    System.out.println("Removidas " + quantidadeParaRemover + " unidade(s) de \"" + livro.getTituloLivro() + "\". Restam " + (quantidadeAtual - quantidadeParaRemover) + " no carrinho.");
                }
                return;
            }
        }
        System.out.println("Livro com ID " + id + " não encontrado no carrinho.");
    }


    // Exibir itens do carrinho
    public void exibirCarrinho() {
        System.out.println("\n--- Carrinho de Compras ---");
        if (itens.isEmpty()) {
            System.out.println("Seu carrinho está vazio.");
        } else {
            double total = 0;
            for (int i = 0; i < itens.size(); i++) {
                Livro livro = itens.get(i);
                int quantidade = quantidades.get(i);
                double subtotal = livro.getPrecoLivro() * quantidade;
                total += subtotal;

                System.out.println("ID: " + livro.getIdLivro() + " | Título: " + livro.getTituloLivro() +
                        " | Quantidade: " + quantidade + " | Subtotal: R$" + subtotal);
            }
            System.out.println("Total: R$" + total);
        }
    }

    // Concluir compra
    public void concluirCompra() {
        Pagamento pgto = new Pagamento();
        if (itens.isEmpty()) {
            System.out.println("Seu carrinho está vazio. Adicione itens antes de finalizar a compra.");
            return;
        }

        double total = 0;
        StringBuilder detalhesCompra = new StringBuilder();

        for (int i = 0; i < itens.size(); i++) {
            Livro livroCarrinho = itens.get(i);
            int quantidade = quantidades.get(i);

            // Verifica o livro atualizado no banco antes de processar
            Livro livroBanco = Livro.buscarLivroPorId(livroCarrinho.getIdLivro());
            if (livroBanco == null) {
                System.out.println("Livro \"" + livroCarrinho.getTituloLivro() + "\" não encontrado no banco. Compra não realizada.");
                return;
            }

            // Verifica novamente o estoque antes de concluir
            if (livroBanco.getQuantidadeEstoque() < quantidade) {
                System.out.println("Estoque insuficiente para \"" + livroBanco.getTituloLivro() + "\". Compra não realizada.");
                return;
            }

            // Atualiza o estoque no banco de dados
            Livro.atualizarEstoque(livroBanco.getIdLivro(), quantidade);

            // Calcula total e registra detalhes
            double subtotal = livroBanco.getPrecoLivro() * quantidade;
            total += subtotal;
            detalhesCompra.append(quantidade).append("x ").append(livroBanco.getTituloLivro()).append(" (R$").append(subtotal).append(")\n");
        }

        pgto.realizarPagamento(total);
        System.out.println("Compra concluída com sucesso! Total: R$" + total);

        // Salva no histórico do usuário
        usuario.adicionarCompraAoHistorico(detalhesCompra.toString());

        // Limpa o carrinho
        itens.clear();
        quantidades.clear();
    }
}