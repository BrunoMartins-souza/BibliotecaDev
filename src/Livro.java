import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Livro {
    private int idLivro;
    private String tituloLivro;
    private String autorLivro;
    private double precoLivro;
    private int quantidadeEstoque;

    // Lista para armazenar todos os livros cadastrados
    private static ArrayList<Livro> livros = new ArrayList<>();

    // ---- Construtor ----
    public Livro(String titulo, String autor, double preco, int quantidadeEstoque) {
        this.idLivro = livros.size() + 1; // Geração de ID único
        this.tituloLivro = titulo;
        this.autorLivro = autor;
        this.precoLivro = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        livros.add(this); // Adiciona o livro à lista de livros
    }

    // ---- Getters e Setters ----
    public int getIdLivro() {
        return idLivro;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }

    public String getAutorLivro() {
        return autorLivro;
    }

    public void setAutorLivro(String autorLivro) {
        this.autorLivro = autorLivro;
    }

    public double getPrecoLivro() {
        return precoLivro;
    }

    public void setPrecoLivro(double precoLivro) {
        this.precoLivro = precoLivro;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // ---- Métodos Estáticos ----

    // Cadastrar novo livro
    public static void cadastrarLivro() {
        Scanner input = new Scanner(System.in);

        System.out.println("\n--- Cadastro de Livro ---");
        System.out.print("Título: ");
        String titulo = input.nextLine();
        System.out.print("Autor: ");
        String autor = input.nextLine();
        System.out.print("Preço: ");
        double preco = input.nextDouble();
        System.out.print("Quantidade em Estoque: ");
        int quantidadeEstoque = input.nextInt();

        new Livro(titulo, autor, preco, quantidadeEstoque);
        System.out.println("Livro cadastrado com sucesso!");

        String sql = "INSERT INTO livro (tituloLivro, autorLivro, precoLivro, quantidadeEstoque) " +
                "VALUES ('" + titulo + "', '" + autor + "', '" + preco + "', '" +
                quantidadeEstoque + "');";

        boolean salvo = MyJDBC.salvar(sql);

        if (salvo) {
            System.out.println("\nLivro cadastrado com sucesso!\n");
        } else {
            System.out.println("\nProblemas ao adicionar o médico!\n");
        }
    }

    // Editar livro
    public static void editarLivro() {
        Scanner input = new Scanner(System.in);

        System.out.print("\nDigite o ID do livro que deseja editar: ");
        int id = input.nextInt();
        input.nextLine(); // Consumir quebra de linha

        // Buscar livro no banco de dados
        String queryBusca = "SELECT * FROM livro WHERE idLivro = " + id;
        try {
            ResultSet rs = MyJDBC.buscar(queryBusca);
            if (rs.next()) {
                System.out.println("Editando livro: " + rs.getString("tituloLivro"));

                System.out.print("Novo título (ou Enter para manter): ");
                String novoTitulo = input.nextLine();
                if (novoTitulo.isEmpty()) novoTitulo = rs.getString("tituloLivro");

                System.out.print("Novo autor (ou Enter para manter): ");
                String novoAutor = input.nextLine();
                if (novoAutor.isEmpty()) novoAutor = rs.getString("autorLivro");

                System.out.print("Novo preço (ou -1 para manter): ");
                double novoPreco = input.nextDouble();
                if (novoPreco == -1) novoPreco = rs.getDouble("precoLivro");

                System.out.print("Nova quantidade em estoque (ou -1 para manter): ");
                int novaQuantidade = input.nextInt();
                if (novaQuantidade == -1) novaQuantidade = rs.getInt("quantidadeEstoque");

                // Atualizar no banco de dados
                String queryUpdate = "UPDATE livro SET tituloLivro = '" + novoTitulo + "', autorLivro = '" + novoAutor +
                        "', precoLivro = " + novoPreco + ", quantidadeEstoque = " + novaQuantidade +
                        " WHERE idLivro = " + id;
                boolean atualizado = MyJDBC.salvar(queryUpdate);

                if (atualizado) {
                    System.out.println("Livro atualizado com sucesso!");
                } else {
                    System.out.println("Erro ao atualizar o livro.");
                }
            } else {
                System.out.println("Livro com ID " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar o livro: " + e.getMessage());
        }
    }

    // Remover livro
    public static void removerLivro() {
        Scanner input = new Scanner(System.in);

        System.out.print("\nDigite o ID do livro que deseja remover: ");
        int id = input.nextInt();

        String sql = "DELETE FROM livro WHERE idLivro = " + id;
        boolean dadoExcluido = MyJDBC.deletar(sql);
        if (dadoExcluido) {
            System.out.println("Livro removido com sucesso!\n");
        } else {
            System.out.println("Problemas ao excluir o livro!\n");
        }

    }

    // Exibir estoque de livros
    public static void exibirEstoque() {
        System.out.println("\n--- Estoque de Livros ---");
        String query = "SELECT * FROM livro";
        try {
            ResultSet rs = MyJDBC.buscar(query);
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idLivro") + " | Título: " + rs.getString("tituloLivro") +
                        " | Autor: " + rs.getString("autorLivro") + " | Preço: R$" + rs.getDouble("precoLivro") +
                        " | Estoque: " + rs.getInt("quantidadeEstoque"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao exibir o estoque: " + e.getMessage());
        }
    }

    // Buscar livro por ID
    public static Livro buscarLivroPorId(int id) {
        String query = "SELECT * FROM livro WHERE idLivro = " + id;
        try {
            ResultSet rs = MyJDBC.buscar(query);
            if (rs.next()) {
                Livro livro = new Livro(
                        rs.getString("tituloLivro"),
                        rs.getString("autorLivro"),
                        rs.getDouble("precoLivro"),
                        rs.getInt("quantidadeEstoque")
                );
                livro.idLivro = id; // Define o ID corretamente
                return livro;
            } else {
                System.out.println("Livro com ID " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar o livro: " + e.getMessage());
        }
        return null;
    }

    // Atualizar estoque após venda
    public static void atualizarEstoque(int id, int quantidadeVendida) {
        String queryBusca = "SELECT * FROM livro WHERE idLivro = " + id;
        try {
            ResultSet rs = MyJDBC.buscar(queryBusca);
            if (rs.next()) {
                int estoqueAtual = rs.getInt("quantidadeEstoque");
                if (estoqueAtual >= quantidadeVendida) {
                    int novoEstoque = estoqueAtual - quantidadeVendida;

                    String queryUpdate = "UPDATE livro SET quantidadeEstoque = " + novoEstoque + " WHERE idLivro = " + id;
                    boolean atualizado = MyJDBC.salvar(queryUpdate);

                    if (atualizado) {
                        System.out.println("Estoque atualizado. Restam " + novoEstoque + " unidades de \"" + rs.getString("tituloLivro") + "\".");
                    } else {
                        System.out.println("Erro ao atualizar o estoque.");
                    }
                } else {
                    System.out.println("Quantidade insuficiente no estoque!");
                }
            } else {
                System.out.println("Livro com ID " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar o livro: " + e.getMessage());
        }
    }
}
