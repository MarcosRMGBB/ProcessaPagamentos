import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


public class ProcessaPagamentos {
    public static void main(String[] args) {
        Path path = Path.of("db/");

        if (Files.exists(path)) {
            System.out.println("A pasta existe!");
            // Verifica se existe arquivos
            if (path.toFile().listFiles().length != 0) {
                System.out.println("Há arquivos para processamento!"); 
                // Faz a leitura dos arquivos no diretório
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

                                    // Atualiza valores
                                    UpdatePagamentos update = new UpdatePagamentos(pagamento);
                                    update.periodoCalculado();
                                    int dias = update.getPeriodoCalculado();

                                    if (dias > 0) {
                                        update.atualizaValor();
                                        update.atualizaPontos();
                                    } else if (dias <= 0) {
                                        update.aplicaDesconto();
                                    }
                                    System.out.println("Dias: " + dias);
                                    System.out.println(pagamento);
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
                
            } else {
                System.out.println("Não há arquivos para processamento!");
            } 
        } else {
            System.out.println("A pasta não existe!");
        }
    }
}
