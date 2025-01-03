package com.sajal.atom.web;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final Map<String, HttpServlet> servlets = new HashMap<>();
    private Tomcat tomcat;

    public void start(int port) {
        tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        servlets.forEach((path, servlet) -> {
            tomcat.addServlet("", path, servlet.getClass().getName());
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