package hr.naivci;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class IOManager {

    public static void writeText(String text, String filePath) {
        try {
            String folderPath = "generated/";
            BufferedWriter bw = new BufferedWriter(new FileWriter(folderPath + filePath));
            bw.write(text);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
