package com.sajal.atom.web;

import com.sajal.atom.web.handler.HandlerRegistry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {
    private final HandlerRegistry handlerRegistry;

    public DispatcherServlet(HandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String method = req.getMethod();
        handlerRegistry.handleRequest(path, method, req, resp);
    }
}