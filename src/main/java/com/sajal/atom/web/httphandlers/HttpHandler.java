package com.sajal.atom.web.httphandlers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HttpHandler {
    void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}