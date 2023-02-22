import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pagamentos {
    String clientNome;
    LocalDate dataVencimento;
    double valor;
    int classificacao;

    public Pagamentos(String clienteNome, double valor, int classificacao) {
        this.clientNome = clienteNome;
        this.valor = valor;
        this.classificacao = classificacao;
    }

    public void setDatavencimento(String dataVencimento) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dataVencimento = LocalDate.parse(dataVencimento, format);
    }

    @Override
    public String toString() {
        return "Cliente: " + this.clientNome + "\n" +
               "Vencimento: " + this.dataVencimento + "\n" +
               "Valor: " + this.valor + "\n" +
               "Classificacao: " + this.classificacao;
    }
}
