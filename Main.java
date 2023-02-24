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

                    String fileName1 = FILE_ATUALIZADOS + filesNameList[0]; 
                    String fileName2 = FILE_ATUALIZADOS + filesNameList[1]; 

                    FileWriter output1 = new FileWriter(PATH_ESCRITA + fileName1);
                    FileWriter output2 = new FileWriter(PATH_ESCRITA + fileName2);

                    Runnable processaArquivo1 = new ProcessaArquivo(readerList.get(0), output1);
                    Runnable processaArquivo2 = new ProcessaArquivo(readerList.get(1), output2);

                    Thread thread1 = new Thread(processaArquivo1);
                    Thread thread2 = new Thread(processaArquivo2);

                    thread1.start();
                    thread2.start();

                    try {
                        thread1.join();
                        thread2.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    output1.close();
                    output2.close();
                    readerList.get(0).close();
                    readerList.get(1).close();
                    
                    // for (int i = 0; i < filesNameList.length; i++) {
                    //     String fileName = FILE_ATUALIZADOS + filesNameList[i]; 
                    //     FileWriter output = new FileWriter(PATH_ESCRITA + fileName);
                    //     Runnable processaArquivo = new ProcessaArquivo(readerList.get(i), output);
                    //     Thread thread = new Thread(processaArquivo);
                    //     thread.start();
                        // Fecha conexão
                        // readerList.get(0).close();
                        // readerList.get(1).close();
                        // output.close();
                    // }
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
