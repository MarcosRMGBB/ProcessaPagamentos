import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;


public class Main {
    public static final String PATH_LEITURA = "db/processamento/";
    public static final String PATH_ESCRITA = "db/processado/";
    public static final String FILE_ATUALIZADOS = "pagamentosAtualizados_";

    public static void main(String[] args) {
        Path path = Path.of(PATH_LEITURA);

        if (Files.exists(path)) {
            System.out.println("A pasta existe!");
            // Verifica se existe arquivos
            if (path.toFile().listFiles().length != 0) {
                System.out.println("Há arquivos para processamento!"); 
                try {
                    // Leitura
                    ReaderFilesName readerFilesName = lerArquivos(PATH_LEITURA);
                    ArrayList<BufferedReader> readerList = readerFilesName.getReader();
                    String[] filesNameList = readerFilesName.getFiles();
                    // Escrita
                    for (int i = 0; i < filesNameList.length; i++) {
                        String fileName = FILE_ATUALIZADOS + filesNameList[i]; 
                        FileWriter output = new FileWriter(PATH_ESCRITA + fileName);
                        atualizaRegistros(readerList.get(i), output);
                        // Fecha conexão
                        readerList.get(i).close();
                        output.close();
                    }
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

    public static ReaderFilesName lerArquivos(String path) {
        File directory = new File(path);
        String[] contents = directory.list();
        ArrayList<BufferedReader> readersList = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            Path pathFile = Path.of(path, contents[i]); 
            try{
                BufferedReader reader = Files.newBufferedReader(pathFile);
                readersList.add(reader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ReaderFilesName readerFilesName = new ReaderFilesName(readersList, contents);
        return readerFilesName;
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

    public static void atualizaRegistros(BufferedReader reader, FileWriter output) {  
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
                    escreveArquivo(pagamentoAtualizado, output);
                }
            // Ler a próxima linha
            line = reader.readLine();
            lineCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
