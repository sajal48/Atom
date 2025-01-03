package com.sajal.atom.web.handler;

import com.sajal.atom.web.httphandlers.GetHandler;
import com.sajal.atom.web.httphandlers.HttpHandler;

import java.lang.reflect.Method;

public class GetHandlerFactory implements HandlerFactory {
    @Override
    public HttpHandler createHandler(Object controller, Method method) {
        return new GetHandler(controller, method);
    }
}