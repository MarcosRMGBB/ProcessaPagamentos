import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


public class Main {
    public static final String PATH_LEITURA = "db/";
    public static final String PATH_ESCRITA = "db/processado/";
    public static final String FILE_ATUALIZADOS = "pagamentosAtualizados_DATA.csv";

    public static void main(String[] args) {
        Path path = Path.of(PATH_LEITURA);

        if (Files.exists(path)) {
            System.out.println("A pasta existe!");
            // Verifica se existe arquivos
            if (path.toFile().listFiles().length != 0) {
                System.out.println("Há arquivos para processamento!"); 
                atualizaArquivo(path);
            } else {
                System.out.println("Não há arquivos para processamento!");
            } 
        } else {
            System.out.println("A pasta não existe!");
        }
    }

    public static Pagamentos atualizaDados(Pagamentos pagamento) {
        UpdatePagamentos update = new UpdatePagamentos(pagamento);
        update.periodoCalculado();
        int dias = update.getPeriodoCalculado();

        if (dias > 0) {
            update.atualizaValor();
            update.atualizaPontos();
        } else if (dias <= 0) {
            update.aplicaDesconto();
        }
        return pagamento;
    }

    public static void atualizaArquivo(Path path) {
        try {
            Files.list(path).forEach((file) -> {
                // Captura o nome do arquivo
                Path fileName = file.getFileName();
                Path relativePath = Path.of(path.toString(), fileName.toString());
                try {
                    // Faz a leitura do arquivo
                    BufferedReader reader = Files.newBufferedReader(relativePath);
                    String line = reader.readLine();
                    int lineCount = 0;
                    // Faz a leitura das linhas não fazias
                    while (line != null) {                               
                        if (lineCount >= 1 & line != null) {
                            // Transforma a leitura em uma lista para criar o objeto java
                            String[] payInfo = line.split(",");
                            Pagamentos pagamento = new Pagamentos(payInfo[0], 
                                                                  Double.parseDouble(payInfo[2]), 
                                                                  Integer.parseInt(payInfo[3]));
                            pagamento.setDatavencimento(payInfo[1]);

                            Pagamentos pagamentoAtualizado = atualizaDados(pagamento);
                            System.out.println(pagamentoAtualizado);
                            escreveArquivo(pagamentoAtualizado, PATH_ESCRITA, FILE_ATUALIZADOS);
                        }
                        // Ler a próxima linha
                        line = reader.readLine();
                        lineCount++;
                    }
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void escreveArquivo(Pagamentos pagamento, String path, String file) {
        Path processado = Path.of(path, file);
        try {
            // Creates a FileWriter
            FileWriter arquivo = new FileWriter("output.txt");
            // Creates a BufferedWriter
            BufferedWriter output = new BufferedWriter(arquivo);
            
            String texto = pagamento.toString();
            output.write(texto);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
