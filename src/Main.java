import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/upload", new UploadHandler());
        server.createContext("/read", new ReadHandler());
        server.createContext("/delete", new DeleteHandler());
        server.createContext("/create-folder", new CreateFolderHandler());
        server.createContext("/delete-folder", new DeleteFolderHandler());
        server.createContext("/list-full-user-directory", new FolderListHandler());
        server.setExecutor(null); // Use the default executor
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class UploadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
                if (contentType != null && contentType.startsWith("multipart/form-data")) {
                    Headers headers = exchange.getRequestHeaders();
                    String uid = headers.getFirst("uid"); // Assuming UID is passed in the header
                    String fileName = headers.getFirst("file-name"); // Assuming file name is passed in the header
                    String filePath = headers.getFirst("file-path");
                    InputStream inputStream = exchange.getRequestBody();

                    try {
                        SaveFile.save(uid, inputStream, fileName, filePath);
                        String response = "File '" + fileName + "' uploaded successfully for UID: " + uid;
                        exchange.sendResponseHeaders(200, response.getBytes().length);
                        OutputStream responseBody = exchange.getResponseBody();
                        responseBody.write(response.getBytes());
                        responseBody.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        String errorMessage = "Internal Server Error: Failed to upload file.";
                        exchange.sendResponseHeaders(500, errorMessage.getBytes().length);
                        OutputStream responseBody = exchange.getResponseBody();
                        responseBody.write(errorMessage.getBytes());
                        responseBody.close();
                    }
                } else {
                    String errorMessage = "Bad Request: Content-Type header must be multipart/form-data.";
                    exchange.sendResponseHeaders(400, errorMessage.getBytes().length);
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
    static class ReadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                // Handle reading file request
                new ReadFile().handle(exchange);
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

    static class DeleteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
                // Handle deleting file request
                new DeleteFile().handle(exchange);
            } else {
                String errorMessage = "Method Not Allowed: Only DELETE requests are allowed.";
                exchange.sendResponseHeaders(405, errorMessage.getBytes().length);
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(errorMessage.getBytes());
                responseBody.close();
            }
            exchange.close();
        }
    }
    static class CreateFolderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Handle create folder request
            new CreateFolder().handle(exchange);
        }
    }
    static class DeleteFolderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Handle delete folder request
            new DeleteFolder().handle(exchange);
        }
    }
}
