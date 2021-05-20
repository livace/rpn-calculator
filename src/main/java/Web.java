import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringJoiner;

class ApiHandler implements HttpHandler {
    static class Data {
        String expression;
        String mode = "evaluate";
        String notation = "infix";

        static Data fromJSON(JSONObject json) {
            Data result = new Data();
            if (json.isNull("expression")) {
                throw new IllegalArgumentException("Missing required field 'expression'");
            }
            result.expression = json.getString("expression");
            if (!json.isNull("mode")) {
                result.mode = json.getString("mode");
            }
            if (!json.isNull("notation")) {
                result.notation = json.getString("notation");
            }
            return result;
        }
    }

    static void setResponse(HttpExchange exchange, String body, int code) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    Calculator calc = new Calculator(null);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            JSONObject json = new JSONObject(new String(httpExchange.getRequestBody().readAllBytes()));
            Data data = Data.fromJSON(json);
            if (data.mode.equals("evaluate")) {
                Notation notation;
                if (data.notation.equals("infix")) {
                    notation = Notation.Infix;
                } else if (data.notation.equals("postfix")) {
                    notation = Notation.Postfix;
                } else {
                    throw new IllegalArgumentException("Unknown notation " + data.notation);
                }
                setResponse(httpExchange, new JSONObject(new HashMap<String, String>() {{
                    put("result", calc.Evaluate(data.expression, notation).toString());
                }}).toString(), 200);
            } else if (data.mode.equals("transform")) {
                Transformer transformer;
                if (data.notation.equals("infix")) {
                    transformer = Transformer.InfixToPostfix;
                } else if (data.notation.equals("postfix")) {
                    transformer = Transformer.PostfixToInfix;
                } else {
                    throw new IllegalArgumentException("Unknown notation " + data.notation);
                }
                Token[] transformed = transformer.Transform(calc.Tokenize(data.expression));
                StringJoiner builder = new StringJoiner(" ");
                for (var token : transformed) {
                    builder.add(token.printableString());
                }
                httpExchange.getResponseHeaders().put("Content-Type", Collections.singletonList("application/json"));
                setResponse(httpExchange, new JSONObject(new HashMap<String, String>() {{
                    put("result", builder.toString());
                }}).toString(), 200);
            }
        } catch (Exception e) {
            setResponse(httpExchange, new JSONObject(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }}).toString(), 400);
        }
    }
}

public class Web {
    short port;

    Web(short port) {
        this.port = port;
    }

    int start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/api/eval", new ApiHandler());
            server.createContext("/api/stop", httpExchange -> server.stop(0));
            server.setExecutor(null);
            server.start();
            synchronized (server) {
                server.wait();
            }
            return 0;
        } catch (Exception e) {
            System.err.printf("Got exception %s of type %s\n", e.getMessage(), e.getClass().getName());
            return 1;
        }
    }
}
