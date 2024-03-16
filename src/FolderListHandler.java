import java.io.*;
import java.util.List;

import com.sun.net.httpserver.*;

public class FolderListHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            Headers headers = exchange.getRequestHeaders();
            String uid = headers.getFirst("uid"); // Assuming UID is passed in the header

            try {
                List<String> fileList = FolderListing.listFiles(uid);
                String response = String.join("\n", fileList);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(response.getBytes());
                responseBody.close();
            } catch (IOException e) {
                e.printStackTrace();
                String errorMessage = "Internal Server Error: Failed to list folder contents.";
                exchange.sendResponseHeaders(500, errorMessage.getBytes().length);
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(errorMessage.getBytes());
                responseBody.close();
            }
        } else {
            String errorMessage = "Method Not Allowed: Only GET requests are allowed.";
            exchange.sendResponseHeaders(405, errorMessage.getBytes().length);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(errorMessage.getBytes());
            responseBody.close();
        }
        exchange.close();
    }
}
