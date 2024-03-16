import java.io.*;
import com.sun.net.httpserver.*;

public class ReadFile implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
            Headers headers = exchange.getRequestHeaders();
            String uid = headers.getFirst("uid"); // Assuming UID is passed in the header
            String fileName = headers.getFirst("file-name"); // Assuming file name is passed in the header
            String filePath = headers.getFirst("file-path");

            try {
                File file = new File("C:/Users/sarim/Downloads/FileForgeDrive/" + uid + filePath + fileName);
                if (file.exists()) {
                    exchange.sendResponseHeaders(200, file.length());
                    OutputStream responseBody = exchange.getResponseBody();
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        responseBody.write(buffer, 0, bytesRead);
                    }
                    fileInputStream.close();
                    responseBody.close();
                    // Set appropriate content type based on file extension
                    String contentType = getContentType(fileName);
                    exchange.getResponseHeaders().set("Content-Type", contentType);
                } else {
                    String errorMessage = "File not found.";
                    exchange.sendResponseHeaders(404, errorMessage.getBytes().length);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(errorMessage.getBytes());
                    responseBody.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                String errorMessage = "Internal Server Error: Failed to read file.";
                exchange.sendResponseHeaders(500, errorMessage.getBytes().length);
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(errorMessage.getBytes());
                responseBody.close();
            }
        exchange.close();
    }

    // Method to determine content type based on file extension
    private String getContentType(String fileName) {
        if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return "application/msword";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            return "application/octet-stream"; // Default content type for unknown file types
        }
    }
}
