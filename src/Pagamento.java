import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.Scanner;

public class Pagamento {
    private Scanner scanner = new Scanner(System.in);

    public void realizarPagamento(double total) {
        System.out.println("\n--- Área de Pagamento ---");
        System.out.println("Escolha o método de pagamento:");
        System.out.println("1. Cartão de Crédito");
        System.out.println("2. Cartão de Débito");
        System.out.println("3. PIX");
        int opcaoPagamento = scanner.nextInt();
        scanner.nextLine();

        switch (opcaoPagamento) {
            case 1:
                processarCartao("crédito", total);
                break;
            case 2:
                processarCartao("débito", total);
                break;
            case 3:
                processarPix(total);
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }

        escolherFormatoLivro();
    }

    private void processarCartao(String tipoCartao, double total) {
        System.out.println("Pagamento com cartão de " + tipoCartao);

        String cpf = solicitarCpf();
        long numeroCartao = solicitarNumeroCartao();
        YearMonth validadeCartao = solicitarValidadeCartao();
        int cvv = solicitarCvv();

        System.out.println("Pagamento de R$" + total + " realizado com sucesso no cartão de " + tipoCartao + "!");
    }

    private void processarPix(double total) {
        System.out.println("Pagamento com PIX");

        String cpf = solicitarCpf();

        // Geração de um código PIX aleatório
        String codigoPix = gerarCodigoPix();
        System.out.println("Código PIX gerado para o pagamento: " + codigoPix);
        System.out.println("Copie o código para realizar o pagamento via PIX.");
    }

    private void escolherFormatoLivro() {
        System.out.println("\nEscolha o formato do livro:");
        System.out.println("1. Livro Digital");
        System.out.println("2. Livro Físico");
        int opcaoLivro = scanner.nextInt();
        scanner.nextLine();

        switch (opcaoLivro) {
            case 1:
                System.out.println("Você escolheu o formato digital. O PDF será enviado para seu e-mail.");
                break;
            case 2:
                solicitarEnderecoEntrega();
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    private void solicitarEnderecoEntrega() {
        System.out.println("Digite seu endereço para entrega do livro físico:");
        System.out.print("Rua: ");
        String rua = scanner.nextLine();
        System.out.print("Número: ");
        String numero = scanner.nextLine();
        System.out.print("Complemento (opcional): ");
        String complemento = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        System.out.print("Estado: ");
        String estado = scanner.nextLine();
        System.out.print("CEP: ");
        String cep = scanner.nextLine();

        System.out.println("Endereço cadastrado com sucesso. Seu livro físico será enviado para o endereço informado.");
    }

    private String gerarCodigoPix() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder("0002012633");
        for (int i = 0; i < 20; i++) {
            codigo.append(random.nextInt(10));
        }
        return codigo.toString();
    }

    private String solicitarCpf() {
        String cpf;
        while (true) {
            System.out.println("Digite seu CPF (somente números):");
            cpf = scanner.nextLine();
            if (isCpfValido(cpf)) {
                break;
            } else {
                System.out.println("CPF inválido. Deve conter exatamente 11 dígitos numéricos. Tente novamente.");
            }
        }
        return cpf;
    }

    private long solicitarNumeroCartao() {
        long numeroCartao;
        while (true) {
            System.out.println("Digite o número do cartão (16 dígitos):");
            try {
                numeroCartao = Long.parseLong(scanner.nextLine());
                if (String.valueOf(numeroCartao).length() == 16) {
                    break;
                } else {
                    System.out.println("Número do cartão inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Número do cartão inválido.");
            }
        }
        return numeroCartao;
    }

    private YearMonth solicitarValidadeCartao() {
        YearMonth validadeCartao;
        while (true) {
            System.out.println("Digite a data de validade do cartão (MM/AA):");
            try {
                String input = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
                validadeCartao = YearMonth.parse(input, formatter);
                if (validadeCartao.isAfter(YearMonth.now())) {
                    break;
                } else {
                    System.out.println("Data de validade inválida.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use o formato MM/AA.");
            }
        }
        return validadeCartao;
    }

    private int solicitarCvv() {
        int cvv;
        while (true) {
            System.out.println("Digite o CVV do cartão (3 dígitos):");
            try {
                cvv = Integer.parseInt(scanner.nextLine());
                if (String.valueOf(cvv).length() == 3) {
                    break;
                } else {
                    System.out.println("CVV inválido. Deve conter 3 dígitos.");
                }
            } catch (NumberFormatException e) {
                System.out.println("CVV inválido. Digite somente números.");
            }
        }
        return cvv;
    }

    private boolean isCpfValido(String cpf) {
        return cpf.matches("\\d{11}");
    }
}

