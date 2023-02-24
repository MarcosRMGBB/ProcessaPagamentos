import java.io.BufferedReader;
import java.io.FileWriter;

public class ProcessaArquivo implements Runnable {
    BufferedReader reader;
    FileWriter output;

    public ProcessaArquivo(BufferedReader reader, FileWriter output) {
        this.reader = reader;
        this.output = output;
    }

    public void atualizaRegistros() {  
        try {
            String line = this.reader.readLine();
            int lineCount = 0;
            // Faz a leitura das linhas não fazias
            while (line != null) {                               
                if (lineCount >= 1 & line != null) {
                // Transforma a leitura em uma lista para criar o objeto java
                String[] payInfo = line.split(",");
                Pagamentos pagamento = new Pagamentos(payInfo[0], Double.parseDouble(payInfo[2]), 
                                                    Integer.parseInt(payInfo[3]));
                pagamento.setDatavencimento(payInfo[1]);

                Pagamentos pagamentoAtualizado = atualizaDados(pagamento);
                    escreveArquivo(pagamentoAtualizado, this.output);
                }
            // Ler a próxima linha
            line = this.reader.readLine();
            lineCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Pagamentos atualizaDados(Pagamentos pagamento) {
        UpdatePagamentos update = new UpdatePagamentos(pagamento);
        update.periodoCalculado();
        int dias = update.getPeriodoCalculado();

        if (dias > 0) {
            update.atualizaValor();
            update.atualizaPontos();
            System.out.println("Aplicando juros e multa!");
        } else if (dias <= 0) {
            System.out.println("Aplicando desconto!");
            update.aplicaDesconto();
        }
        return pagamento;
    }

    public static void escreveArquivo(Pagamentos pagamento, FileWriter output) {
        try {
            String texto = pagamento.toString();
            System.out.println("Escrevendo registro: " + texto);
            output.write(texto);
            output.write(System.lineSeparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.atualizaRegistros();
    }
}
