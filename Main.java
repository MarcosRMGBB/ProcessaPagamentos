import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;


public class Main {
    public static final String PATH_LEITURA = "db/processamento/";
    public static final String PATH_ESCRITA = "db/processado/";
    public static final String FILE_ATUALIZADOS = "pagamentosAtualizados_DATA.csv";

    public static void main(String[] args) {
        Path path = Path.of(PATH_LEITURA);

        if (Files.exists(path)) {
            System.out.println("A pasta existe!");
            // Verifica se existe arquivos
            if (path.toFile().listFiles().length != 0) {
                System.out.println("Há arquivos para processamento!"); 
                BufferedReader reader = lerArquivos(PATH_LEITURA);
                atualizaRegistros(reader);
            } else {
                System.out.println("Não há arquivos para processamento!");
            } 
        } else {
            System.out.println("A pasta não existe!");
        }
    }

    public static BufferedReader lerArquivos(String path) {
        BufferedReader reader = null;
        File directory = new File(path);
        String[] contents = directory.list();
        if (contents.length <= 1) {
            Path pathFile = Path.of(path, contents[0]); 
            try{
                reader = Files.newBufferedReader(pathFile);
                return reader;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Implementar multiplos arquivos!");
        }   
        return reader;
    }

    public static void atualizaRegistros(BufferedReader reader) {  
        try {
            String line = reader.readLine();
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
                try {
                    // Creates a FileWriter
                    FileWriter arquivo = new FileWriter(PATH_ESCRITA + FILE_ATUALIZADOS);
                    // Creates a BufferedWriter
                    BufferedWriter output = new BufferedWriter(arquivo);
                    escreveArquivo(pagamentoAtualizado, output);
                    output.newLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Ler a próxima linha
            line = reader.readLine();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
