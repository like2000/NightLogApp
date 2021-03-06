package ch.li.k.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class DataProvider {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private final static String FILENAME = "lib/res/new.csv";
//    private static final String DIRECTORY = Environment
//            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            .getAbsolutePath();

    public static List<String[]> readFile(String filename) throws IOException {
        String row;
        List<String[]> data = new ArrayList<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filename));
        while ((row = csvReader.readLine()) != null) {
            String[] rowData = row.split(",");
            data.add(rowData);
        }
        csvReader.close();

        return data;
    }

    public static void initFile(String filename) throws IOException {

        File csvFile = new File(filename);
        if (csvFile.exists()) {
            LOGGER.info("File already exists: " + filename);
        } else {
            FileWriter csvWriter = new FileWriter(csvFile);
            // HEADER
            // ======
            csvWriter.append("Name");
            csvWriter.append(",");
            csvWriter.append("Role");
            csvWriter.append(",");
            csvWriter.append("Topic");
            csvWriter.append(",");
            csvWriter.append("Time");
            csvWriter.append("\n");

//        for (String[] rowData : data) {
//            csvWriter.append(String.join(",", rowData));
//            csvWriter.append("\n");
//        }

            csvWriter.flush();
            csvWriter.close();
        }
    }

    public static void writeFile(String filename, List<String[]> data) throws IOException {
        FileWriter csvWriter = new FileWriter(filename);

        // HEADER
        // ======
        csvWriter.append("Name");
        csvWriter.append(",");
        csvWriter.append("Role");
        csvWriter.append(",");
        csvWriter.append("Topic");
        csvWriter.append(",");
        csvWriter.append("Time");
        csvWriter.append("\n");

        for (String[] rowData : data) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();
    }

    public static void main(String[] args) throws IOException {
        List<String[]> rows = Arrays.asList(
                new String[]{"Jean", "author", "Java", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.mm.yyyy"))},
                new String[]{"David", "editor", "Python", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE dd. MMMM yyyy"))},
                new String[]{"Scott", "editor", "Node.js", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:MM:SS"))}
        );
        writeFile(FILENAME, rows);
        readFile(FILENAME);
    }
}
