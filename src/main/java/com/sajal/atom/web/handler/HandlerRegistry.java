package com.sajal.atom.web.handler;

import com.sajal.atom.annotations.web.Controller;
import com.sajal.atom.annotations.web.GetMapping;
import com.sajal.atom.annotations.web.PostMapping;
import com.sajal.atom.web.httphandlers.GetHandler;
import com.sajal.atom.web.httphandlers.PostHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {
    private final Map<String, GetHandler> getHandlers = new HashMap<>();
    private final Map<String, PostHandler> postHandlers = new HashMap<>();

    public void registerHandlers(Map<Class<?>, Object> atoms) {
        for (Object atom : atoms.values()) {
            if (atom.getClass().isAnnotationPresent(Controller.class)) {
                for (Method method : atom.getClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        getHandlers.put(getMapping.value(), new GetHandler(atom, method));
                        System.out.println("Registered GET handler for path: " + getMapping.value());
                    } else if (method.isAnnotationPresent(PostMapping.class)) {
                        PostMapping postMapping = method.getAnnotation(PostMapping.class);
                        postHandlers.put(postMapping.value(), new PostHandler(atom, method));
                        System.out.println("Registered POST handler for path: " + postMapping.value());
                    }
                }
            }
        }
    }

    public void handleRequest(String path, String method, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if ("GET".equalsIgnoreCase(method)) {
            GetHandler handler = getHandlers.get(path);
            if (handler != null) {
                handler.handle(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if ("POST".equalsIgnoreCase(method)) {
            PostHandler handler = postHandlers.get(path);
            if (handler != null) {
                handler.handle(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}