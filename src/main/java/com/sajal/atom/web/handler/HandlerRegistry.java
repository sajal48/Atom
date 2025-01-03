package com.sajal.atom.web.handler;

import com.sajal.atom.annotations.web.Controller;
import com.sajal.atom.annotations.web.GetMapping;
import com.sajal.atom.annotations.web.PostMapping;
import com.sajal.atom.web.httphandlers.HttpHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {
    private final Map<String, HttpHandler> handlers = new HashMap<>();
    private final Map<Class<? extends Annotation>, HandlerFactory> factories = new HashMap<>();

    public HandlerRegistry() {
        factories.put(GetMapping.class, new GetHandlerFactory());
        factories.put(PostMapping.class, new PostHandlerFactory());
    }

    public void registerHandlers(Map<Class<?>, Object> atoms) {
        for (Object atom : atoms.values()) {
            if (atom.getClass().isAnnotationPresent(Controller.class)) {
                for (Method method : atom.getClass().getDeclaredMethods()) {
                    for (Map.Entry<Class<? extends Annotation>, HandlerFactory> entry : factories.entrySet()) {
                        if (method.isAnnotationPresent(entry.getKey())) {
                            Annotation annotation = method.getAnnotation(entry.getKey());
                            String path = null;
                            if (annotation instanceof GetMapping) {
                                path = ((GetMapping) annotation).value();
                            } else if (annotation instanceof PostMapping) {
                                path = ((PostMapping) annotation).value();
                            }
                            if (path != null) {
                                handlers.put(path, entry.getValue().createHandler(atom, method));
                                System.out.println("Registered handler for path: " + path);
                            }
                        }
                    }
                }
            }
        }
    }

    public void handleRequest(String path, String method, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpHandler handler = handlers.get(path);
        if (handler != null) {
            handler.handle(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}