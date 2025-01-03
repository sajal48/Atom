package com.sajal.atom.web;

import com.sajal.atom.annotations.web.Controller;
import com.sajal.atom.annotations.web.GetMapping;
import com.sajal.atom.annotations.web.PostMapping;
import com.sajal.atom.web.httphandlers.GetHandler;
import com.sajal.atom.web.httphandlers.PostHandler;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;

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
        System.out.println("Creating context for path: " + path);
    }

    public void registerHandlers(Collection<Object> beans) {
        for (Object bean : beans) {
            if (bean.getClass().isAnnotationPresent(Controller.class)) {
                for (Method method : bean.getClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        createContext(getMapping.value(), new GetHandler(bean, method));
                        System.out.println("Registered GET handler for path: " + getMapping.value());
                    } else if (method.isAnnotationPresent(PostMapping.class)) {
                        PostMapping postMapping = method.getAnnotation(PostMapping.class);
                        createContext(postMapping.value(), new PostHandler(bean, method));
                        System.out.println("Registered POST handler for path: " + postMapping.value());
                    }
                }
            }
        }
    }
}