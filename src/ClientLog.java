import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ClientLog {
    private File txtFile;


    public void log(int productNum, int amount) throws IOException {
        txtFile = new File("log.txt");
        try (FileWriter out = new FileWriter(txtFile, true)) {
            out.write((productNum + 1) + ", " + amount + "\n");
        }
    }

    public void exportAsCSV(File file) throws IOException {
        try {
            FileWriter writer;
            Scanner scan = new Scanner(txtFile);
            File csv = new File("log.csv");
            txtFile.createNewFile();
            writer = new FileWriter(csv, true);
            writer.append("productNum, amount" + "\n");
            while (scan.hasNext()) {
                String csvStr = scan.nextLine().replace("|", ",");
                writer.append(csvStr);
                writer.append("\n");
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
