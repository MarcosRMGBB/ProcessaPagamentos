import java.io.BufferedReader;

public class ReaderFilesName {
    BufferedReader reader;
    String[] files;

    public ReaderFilesName(BufferedReader reader, String[] files) {
        this.reader = reader;
        this.files = files;
    }

    public BufferedReader getReader() {
        return this.reader;
    }

    public String[] getFiles() {
        return this.files;
    }
}
