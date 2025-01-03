package com.sajal.atom.web;

import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;


import java.util.HashMap;
import java.util.Map;

public class Server {
    private final Map<String, HttpServlet> servlets = new HashMap<>();
    private Tomcat tomcat;

    public void start(int port) {
        tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        // Create and add context
        Context context = tomcat.addContext("", null);

        // Register servlets
        servlets.forEach((path, servlet) -> {
            String servletName = servlet.getClass().getName();
            tomcat.addServlet("", servletName, servlet);
            context.addServletMappingDecoded(path, servletName);
        });

        try {
            tomcat.start();
            System.out.println("Tomcat server started on port: " + port);
        } catch (LifecycleException e) {
            throw new RuntimeException("Failed to start Tomcat server", e);
        }
    }

    public void addServlet(HttpServlet servlet, String path) {
        servlets.put(path, servlet);
        System.out.println("Added servlet for path: " + path);
    }
}