import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientLog {
    private long count;
    private boolean addToCount;
    private StringBuilder saveLog = new StringBuilder();
    private String fileName;
    private File fileLog;
    public ClientLog(File file) {
        fileLog = file;
        count =countLine();
        addToCount = emptyLog();
    }
    public void log(int productNum, int amount) {
        if (!fileLog.exists()) {
            try {
                fileLog.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        count += 1;
        if (addToCount) {
            count += 1;
        }
        saveLog.append(count);
        saveLog.append(". ");
        saveLog.append(productNum + 1);
        saveLog.append(". ");
        saveLog.append(amount);
        saveLog.append('\n');
        addToCount = false;
    }

    public void exportAsCSV(File txtFile) {
        try (FileWriter writer = new FileWriter(txtFile, true)) {
            if (emptyLog()) {
                String strings = "1. productNum, amount";
                writer.write(strings);
                writer.write("\n");
            }
            writer.write(saveLog.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean emptyLog() {
        if (fileLog.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private long countLine() {
        if (!fileLog.exists()) {
            try {
                fileLog.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        long count = 0;
        if(!emptyLog()) {
            try {
                count = Files.lines(Paths.get(fileLog.getName())).count();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return count;
    }
}
