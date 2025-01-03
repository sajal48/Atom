package com.sajal.atom.web.handler;

import com.sajal.atom.web.httphandlers.HttpHandler;

import java.lang.reflect.Method;

public interface HandlerFactory {
    HttpHandler createHandler(Object controller, Method method);
}