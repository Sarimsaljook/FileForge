import java.io.*;
import com.sun.net.httpserver.*;

public class DeleteFolder implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
            Headers headers = exchange.getRequestHeaders();
            String uid = headers.getFirst("uid"); // Assuming UID is passed in the header
            String folderName = headers.getFirst("folder-name"); // Assuming folder name is passed in the header
            String folderPath = headers.getFirst("folder-path");

            try {
                File directory = new File("C:/Users/sarim/Downloads/FileForgeDrive/" + uid + folderPath + folderName);
                if (directory.exists()) {
                    deleteDirectory(directory);
                    String successMessage = "Folder deleted successfully.";
                    exchange.sendResponseHeaders(200, successMessage.getBytes().length);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(successMessage.getBytes());
                    responseBody.close();
                } else {
                    String errorMessage = "Folder not found.";
                    exchange.sendResponseHeaders(404, errorMessage.getBytes().length);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(errorMessage.getBytes());
                    responseBody.close();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
                String errorMessage = "Forbidden: Unable to delete folder.";
                exchange.sendResponseHeaders(403, errorMessage.getBytes().length);
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(errorMessage.getBytes());
                responseBody.close();
            }
        } else {
            String errorMessage = "Method Not Allowed: Only DELETE requests are allowed.";
            exchange.sendResponseHeaders(405, errorMessage.getBytes().length);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(errorMessage.getBytes());
            responseBody.close();
        }
        exchange.close();
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
