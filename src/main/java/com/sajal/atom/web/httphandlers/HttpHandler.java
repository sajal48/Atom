package com.sajal.atom.web.httphandlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface HttpHandler {
    void handle(HttpExchange exchange) throws IOException;
}