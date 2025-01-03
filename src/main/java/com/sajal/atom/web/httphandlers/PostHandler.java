package com.sajal.atom.web.httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class PostHandler implements HttpHandler {
    private final Object bean;
    private final Method method;

    public PostHandler(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Handling POST request for path: " + exchange.getRequestURI().getPath());
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String response = (String) method.invoke(bean);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                System.out.println("Response sent for POST request");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}