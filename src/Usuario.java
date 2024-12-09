import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Usuario {
    private int idUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String senhaUsuario;
    private ArrayList<String> historicoCompras;

    // Lista estática para armazenar todos os usuários cadastrados
    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    // ---- Construtor ----
    public Usuario(String nome, String email, String senha) {
        this.nomeUsuario = nome;
        this.emailUsuario = email;
        this.senhaUsuario = senha;
        this.historicoCompras = new ArrayList<>();
        usuarios.add(this); // Adiciona o usuário à lista de usuários
    }

    // ---- Getters e Setters ----
    public String getNome() {
        return nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public ArrayList<String> getHistoricoCompras() {
        return historicoCompras;
    }

    // ---- Métodos Estáticos ----

    // Cadastro de Usuário
    public static void cadastrarUsuario() {
        Scanner input = new Scanner(System.in);

        System.out.println("\n--- Cadastro de Usuário ---");
        System.out.print("Nome: ");
        String nome = input.nextLine();
        System.out.print("Email: ");
        String email = input.nextLine();

        // Verificar no banco de dados se o email já está cadastrado
        String sqlVerificacao = "SELECT COUNT(*) AS total FROM usuario WHERE emailUsuario = '" + email + "'";

        try {
            ResultSet rs = MyJDBC.buscar(sqlVerificacao); // Consultar o banco
            if (rs.next() && rs.getInt("total") > 0) {
                System.out.println("Email já cadastrado. Tente outro.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar o email no banco de dados: " + e.getMessage());
            return;
        }

        // Se o email não existir, prosseguir com o cadastro
        System.out.print("Senha: ");
        String senha = input.nextLine();

        String sqlInsercao = "INSERT INTO usuario (nomeUsuario, emailUsuario, senhaUsuario) " +
                "VALUES ('" + nome + "', '" + email + "', '" + senha + "')";

        boolean salvo = MyJDBC.salvar(sqlInsercao);

        if (salvo) {
            new Usuario(nome, email, senha); // Adicionar à lista local
            System.out.println("Usuário cadastrado com sucesso!");
        } else {
            System.out.println("Problemas ao adicionar o usuário!");
        }
    }

    // Remover usuario
    public static void removerUsuario() {
        Scanner input = new Scanner(System.in);

        System.out.print("\nDigite o ID do usuario que deseja remover: ");
        int id = input.nextInt();

        String sql = "DELETE FROM usuario WHERE idUsuario = " + id;
        boolean dadoExcluido = MyJDBC.deletar(sql);
        if (dadoExcluido) {
            System.out.println("Usuario removido com sucesso!\n");
        } else {
            System.out.println("Problemas ao excluir o Usuario!\n");
        }

    }


    // Login de Usuário
    public static Usuario login() {
        Scanner input = new Scanner(System.in);

        System.out.println("\n--- Login de Usuário ---");
        System.out.print("Email: ");
        String email = input.nextLine();
        System.out.print("Senha: ");
        String senha = input.nextLine();

        String query = "SELECT * FROM usuario WHERE emailUsuario = '" + email + "' AND senhaUsuario = '" + senha + "'";

        try {
            // Executar a consulta no banco de dados
            ResultSet rs = MyJDBC.buscar(query);

            // Verificar se retornou algum resultado
            if (rs.next()) {
                int id = rs.getInt("idUsuario");
                String nome = rs.getString("nomeUsuario");
                String emailUsuario = rs.getString("emailUsuario");
                String senhaUsuario = rs.getString("senhaUsuario");

                // Criar uma instância de Usuario com os dados do banco de dados
                Usuario usuario = new Usuario(nome, emailUsuario, senhaUsuario);
                usuario.idUsuario = id; // Definir o ID do usuário a partir do banco
                System.out.println("Login bem-sucedido! Bem-vindo, " + nome + "!");
                return usuario;
            } else {
                System.out.println("Email ou senha inválidos.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao realizar login: " + e.getMessage());
        }

        return null;
    }


    // Adicionar compra ao histórico
    public void adicionarCompraAoHistorico(String detalhesCompra) {
        String sqlInsercao = "INSERT INTO historico_compras (idUsuario, detalhesCompra) " +
                "VALUES (" + idUsuario + ", '" + detalhesCompra + "')";
        boolean salvo = MyJDBC.salvar(sqlInsercao);

        if (salvo) {
            System.out.println("Compra registrada no histórico com sucesso!");
            historicoCompras.add(detalhesCompra); // Opcional: atualizar lista local
        } else {
            System.out.println("Erro ao registrar compra no histórico.");
        }
    }

    // Exibir histórico de compras
    public void exibirHistoricoCompras() {
        System.out.println("\n--- Histórico de Compras ---");

        String query = "SELECT detalhesCompra, dataCompra FROM historico_compras WHERE idUsuario = " + idUsuario;

        try {
            ResultSet rs = MyJDBC.buscar(query);

            if (!rs.isBeforeFirst()) {
                System.out.println("Nenhuma compra realizada até o momento.");
                return;
            }

            while (rs.next()) {
                String detalhesCompra = rs.getString("detalhesCompra");
                String dataCompra = rs.getString("dataCompra");
                System.out.println(dataCompra + " - " + detalhesCompra);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao exibir histórico de compras: " + e.getMessage());
        }
    }
}