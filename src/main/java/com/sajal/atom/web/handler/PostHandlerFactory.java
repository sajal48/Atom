package com.sajal.atom.web.handler;

import com.sajal.atom.web.httphandlers.HttpHandler;
import com.sajal.atom.web.httphandlers.PostHandler;

import java.lang.reflect.Method;

public class PostHandlerFactory implements HandlerFactory {
    @Override
    public HttpHandler createHandler(Object controller, Method method) {
        return new PostHandler(controller, method);
    }
}