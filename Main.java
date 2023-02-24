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
                        Runnable processaArquivo = new ProcessaArquivo(readerList.get(i), output);
                        Thread thread = new Thread(processaArquivo);
                        thread.start();
                        //atualizaRegistros(readerList.get(i), output);
                        // Fecha conexão
                        // readerList.get(0).close();
                        // readerList.get(1).close();
                        // output.close();
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
}
