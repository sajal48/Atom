package com.sajal.atom;

import com.sajal.atom.annotations.atom.AtomApplicationAnnotation;
import com.sajal.atom.util.AtomAsciiLogo;
import com.sajal.atom.util.TimestampedPrintStream;
import com.sajal.atom.web.DispatcherServlet;
import com.sajal.atom.web.Server;
import com.sajal.atom.web.handler.HandlerRegistry;


import java.util.Map;

public class AtomApplication {
    private final AtomFactory atomFactory;
    private final Server server;
    private final HandlerRegistry handlerRegistry;

    public AtomApplication(AtomFactory atomFactory, Server server, HandlerRegistry handlerRegistry) {
        this.atomFactory = atomFactory;
        this.server = server;
        this.handlerRegistry = handlerRegistry;
    }

    public static void run(Class<?> mainClass) {
        AtomAsciiLogo.printLogo();
        System.setOut(new TimestampedPrintStream(System.out, false));
        System.setErr(new TimestampedPrintStream(System.err, true));
        System.out.println("Starting AtomApplication...");
        AtomFactory atomFactory = new AtomFactory();
        Server server = new Server();
        HandlerRegistry handlerRegistry = new HandlerRegistry();
        AtomApplication application = new AtomApplication(atomFactory, server, handlerRegistry);
        application.initialize(mainClass);
        application.startServer();
    }

    private void initialize(Class<?> mainClass) {
        System.out.println("Initializing application with main class: " + mainClass.getName());
        if (mainClass.isAnnotationPresent(AtomApplicationAnnotation.class)) {
            atomFactory.createAtoms(mainClass);
            atomFactory.injectDependencies();
            AtomContext.getInstance().initialize(atomFactory);
        } else {
            throw new RuntimeException("Main class must be annotated with @AtomApplicationAnnotation");
        }
    }

    private void startServer() {
        System.out.println("Starting server...");
        new Thread(() -> {
            try {
                Map<Class<?>, Object> atoms = atomFactory.getAtoms();
                System.out.println("Beans: " + atoms);
                handlerRegistry.registerHandlers(atoms);
                server.addServlet(new DispatcherServlet(handlerRegistry), "/*");
                server.start(8080);
            } catch (Exception e) {
                throw new RuntimeException("Failed to start server", e);
            }
        }).start();
    }
}