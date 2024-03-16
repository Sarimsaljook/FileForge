import java.io.*;
import com.sun.net.httpserver.*;

public class DeleteFile implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
            Headers headers = exchange.getRequestHeaders();
            String uid = headers.getFirst("uid"); // Assuming UID is passed in the header
            String fileName = headers.getFirst("file-name"); // Assuming file name is passed in the header
            String filePath = headers.getFirst("file-path");

        try {
            File file = new File("C:/Users/sarim/Downloads/FileForgeDrive/" + uid + filePath + fileName);
                if (file.exists()) {
                    file.delete();
                    String successMessage = "File deleted successfully.";
                    exchange.sendResponseHeaders(200, successMessage.getBytes().length);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(successMessage.getBytes());
                    responseBody.close();
                } else {
                    String errorMessage = "File not found.";
                    exchange.sendResponseHeaders(404, errorMessage.getBytes().length);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(errorMessage.getBytes());
                    responseBody.close();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
                String errorMessage = "Forbidden: Unable to delete file.";
                exchange.sendResponseHeaders(403, errorMessage.getBytes().length);
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(errorMessage.getBytes());
                responseBody.close();
            }
        exchange.close();
    }
}
