package com.sajal.atom.web.httphandlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class GetHandler {
    private final Object controller;
    private final Method method;

    public GetHandler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            method.invoke(controller, req, resp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to handle GET request", e);
        }
    }
}