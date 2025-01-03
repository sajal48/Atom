package com.sajal.atom.web.httphandlers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface HttpHandler {
    void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}