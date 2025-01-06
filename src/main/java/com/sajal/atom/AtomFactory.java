package com.sajal.atom;

import com.sajal.atom.annotations.atom.Atom;
import com.sajal.atom.annotations.web.Controller;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

public class AtomFactory {
    private final Map<Class<?>, Object> atoms = new HashMap<>();

    public void createAtoms(Class<?> mainClass) {
        System.out.println("Creating atom for class: " + mainClass.getName());
        try {
            Object mainInstance = mainClass.getDeclaredConstructor().newInstance();
            atoms.put(mainClass, mainInstance);
            injectDependencies(mainInstance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create atom: " + mainClass.getName(), e);
        }

        // Scan for classes annotated with @Atom and create instances
        Reflections reflections = new Reflections(mainClass.getPackage().getName());
        Set<Class<?>> atomClasses = reflections.getTypesAnnotatedWith(Atom.class);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        atomClasses.addAll(controllerClasses);
        for (Class<?> atomClass : atomClasses) {
            try {
                atoms.put(atomClass, atomClass.getDeclaredConstructor().newInstance());
                System.out.println("Created atom for: " + atomClass.getName());
            } catch (Exception e) {
                throw new RuntimeException("Failed to create atom for: " + atomClass.getName(), e);
            }
        }
    }

    public void injectDependencies() {
        System.out.println("Injecting dependencies...");
        for (Object atom : atoms.values()) {
            injectDependencies(atom);
        }
    }

    private void injectDependencies(Object atom) {
        for (Field field : atom.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Atom.class)) {
                field.setAccessible(true);
                try {
                    field.set(atom, atoms.get(field.getType()));
                    System.out.println("Injected dependency: " + field.getType().getName() + " into " + atom.getClass().getName());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject dependencies", e);
                }
            }
        }
    }

    public Map<Class<?>, Object> getAtoms() {
        return atoms;
    }
}