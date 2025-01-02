package com.sajal.atom.web;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Server {
    private Tomcat tomcat;

    public void start(int port) {
        System.out.println("Creating Tomcat server on port: " + port);
        tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        File webappDir = new File("src/main/webapp");
        if (!webappDir.exists()) {
            System.err.println("Web application directory does not exist: " + webappDir.getAbsolutePath());
            System.err.println("Please create the directory or specify the correct path.");
            return;
        }

        tomcat.addWebapp("", webappDir.getAbsolutePath()); // Set context path to an empty string
        try {
            tomcat.start();
            System.out.println("Tomcat server started on port: " + port);
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        System.out.println("Stopping Tomcat server...");
        if (tomcat != null) {
            try {
                tomcat.stop();
                System.out.println("Tomcat server stopped.");
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }
    }

    public void createContext(String path, Object handler) {
        // Implement context creation logic if needed
        System.out.println("Creating context for path: " + path);
    }
}