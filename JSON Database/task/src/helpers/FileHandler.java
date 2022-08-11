package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
    String fileName;

    public FileHandler(String fileName) throws IOException {
        this.fileName = fileName;
        if(!fileExists()) {
            createFile();
        }
    }

    public String readJsonFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        return reader.readLine();
    }

    public boolean fileExists() {
        File file = new File( fileName);
        return file.exists();
    }
    public void createFile() {
        File file = new File(fileName);
        try {
            System.out.println("File created: " + file.createNewFile());
        }
    catch(Exception e){
            e.printStackTrace();
        }
    }
}
