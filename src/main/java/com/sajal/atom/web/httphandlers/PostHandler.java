package com.sajal.atom.web.httphandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class PostHandler implements HttpHandler {
    private final Object controller;
    private final Method method;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PostHandler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Object result = method.invoke(controller, req, resp);
            if (result != null) {
                resp.setContentType("application/json");
                String jsonResponse = objectMapper.writeValueAsString(result);
                resp.getWriter().write(jsonResponse);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to handle POST request", e);
        }
    }
}