import java.io.*;
import com.sun.net.httpserver.*;

public class CreateFolder implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            Headers headers = exchange.getRequestHeaders();
            String uid = headers.getFirst("uid"); // Assuming UID is passed in the header
            String folderName = headers.getFirst("folder-name"); // Assuming folder name is passed in the header
            String folderPath = headers.getFirst("folder-path");

            try {
                File directory = new File("C:/Users/sarim/Downloads/FileForgeDrive/" + uid + folderPath + folderName);
                if (!directory.exists()) {
                    directory.mkdirs();
                    String successMessage = "Folder created successfully.";
                    exchange.sendResponseHeaders(200, successMessage.getBytes().length);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(successMessage.getBytes());
                    responseBody.close();
                } else {
                    String errorMessage = "Folder already exists.";
                    exchange.sendResponseHeaders(409, errorMessage.getBytes().length);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(errorMessage.getBytes());
                    responseBody.close();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
                String errorMessage = "Forbidden: Unable to create folder.";
                exchange.sendResponseHeaders(403, errorMessage.getBytes().length);
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(errorMessage.getBytes());
                responseBody.close();
            }
        } else {
            String errorMessage = "Method Not Allowed: Only POST requests are allowed.";
            exchange.sendResponseHeaders(405, errorMessage.getBytes().length);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(errorMessage.getBytes());
            responseBody.close();
        }
        exchange.close();
    }
}
