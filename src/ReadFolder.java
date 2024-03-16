import java.io.*;
import com.sun.net.httpserver.*;

public class ReadFolder implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            // Handle reading folder contents request
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
