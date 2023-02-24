import java.io.BufferedReader;
import java.util.ArrayList;

public class ReaderFilesName {
    ArrayList<BufferedReader> reader;
    String[] files;

    public ReaderFilesName(ArrayList<BufferedReader> reader, String[] files) {
        this.reader = reader;
        this.files = files;
    }

    public ArrayList<BufferedReader> getReader() {
        return this.reader;
    }

    public String[] getFiles() {
        return this.files;
    }
}
