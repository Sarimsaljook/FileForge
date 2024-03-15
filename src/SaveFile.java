import java.io.*;

public class SaveFile {
    public static void save(String uid, InputStream fileInputStream, String fileName) throws IOException {
        String directoryPath = "C:/Users/sarim/Downloads/FileForgeDrive/" + uid;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File outputFile = new File(directory, fileName);
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
