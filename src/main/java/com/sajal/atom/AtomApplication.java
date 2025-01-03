package com.sajal.atom;

import com.sajal.atom.annotations.atom.Atom;
import com.sajal.atom.annotations.atom.AtomApplicationAnnotation;
import com.sajal.atom.annotations.web.Controller;
import com.sajal.atom.annotations.web.GetMapping;
import com.sajal.atom.annotations.web.PostMapping;
import com.sajal.atom.util.AtomAsciiLogo;
import com.sajal.atom.util.TimestampedPrintStream;
import com.sajal.atom.web.httphandlers.GetHandler;
import com.sajal.atom.web.httphandlers.PostHandler;
import com.sajal.atom.web.Server;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AtomApplication {
    private Map<Class<?>, Object> beans = new HashMap<>();
    private Server server = new Server();

    public static void run(Class<?> mainClass) {
        AtomAsciiLogo.printLogo();
        System.setOut(new TimestampedPrintStream(System.out,false));
        System.setErr(new TimestampedPrintStream(System.err,true));
        System.out.println("Starting AtomApplication...");
        AtomApplication application = new AtomApplication();
        application.initialize(mainClass);
        application.startServer();
    }

    private void initialize(Class<?> mainClass) {
        System.out.println("Initializing application with main class: " + mainClass.getName());
        if (mainClass.isAnnotationPresent(AtomApplicationAnnotation.class)) {
            beans.put(mainClass, createAtoms(mainClass));
            injectDependencies();
        } else {
            throw new RuntimeException("Main class must be annotated with @AtomApplicationAnnotation");
        }
    }

    private Object createAtoms(Class<?> atomClass) {
        System.out.println("Creating bean for class: " + atomClass.getName());
        try {
            return atomClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean: " + atomClass.getName(), e);
        }
    }

    private void injectDependencies() {
        System.out.println("Injecting dependencies...");
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Atom.class)) {
                    field.setAccessible(true);
                    try {
                        field.set(bean, beans.get(field.getType()));
                        System.out.println("Injected dependency: " + field.getType().getName() + " into " + bean.getClass().getName());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to inject dependencies", e);
                    }
                }
            }
        }
    }

    private void startServer() {
        System.out.println("Starting server...");
        new Thread(() -> {
            server.start(8080);
            for (Object bean : beans.values()) {
                if (bean.getClass().isAnnotationPresent(Controller.class)) {
                    for (Method method : bean.getClass().getDeclaredMethods()) {
                        if (method.isAnnotationPresent(GetMapping.class)) {
                            GetMapping getMapping = method.getAnnotation(GetMapping.class);
                            server.createContext(getMapping.value(), new GetHandler(bean, method));
                            System.out.println("Registered GET handler for path: " + getMapping.value());
                        } else if (method.isAnnotationPresent(PostMapping.class)) {
                            PostMapping postMapping = method.getAnnotation(PostMapping.class);
                            server.createContext(postMapping.value(), new PostHandler(bean, method));
                            System.out.println("Registered POST handler for path: " + postMapping.value());
                        }
                    }
                }
            }
        }).start();
    }

    public <T> T getBean(Class<T> beanClass) {
        return beanClass.cast(beans.get(beanClass));
    }
}