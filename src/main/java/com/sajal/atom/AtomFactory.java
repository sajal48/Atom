package com.sajal.atom;

import com.sajal.atom.annotations.atom.Atom;
import com.sajal.atom.annotations.web.Controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.reflections.Reflections;

import jakarta.annotation.PostConstruct;

public class AtomFactory {
    private final Map<Class<?>, Object> atoms = new TreeMap<>(Comparator.comparing(Class::getName));
    private final Map<Class<?>, List<Class<?>>> dependencyGraph = new HashMap<>();

    public void createAtoms(Class<?> mainClass) {
        System.out.println("Creating atom for class: " + mainClass.getName());
        try {
            Object mainInstance = createInstance(mainClass);
            atoms.put(mainClass, mainInstance);
            injectDependencies(mainInstance);
            invokePostConstruct(mainInstance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create atom: " + mainClass.getName(), e);
        }

        // Scan for classes annotated with @Atom and @Controller and create instances
        Reflections reflections = new Reflections(mainClass.getPackage().getName());
        Set<Class<?>> atomClasses = reflections.getTypesAnnotatedWith(Atom.class);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        atomClasses.addAll(controllerClasses);

        // Build dependency graph
        for (Class<?> atomClass : atomClasses) {
            dependencyGraph.putIfAbsent(atomClass, new ArrayList<>());
            for (Field field : atomClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Atom.class)) {
                    dependencyGraph.get(atomClass).add(field.getType());
                }
            }
        }

        // Topological sort to determine the order of creation
        List<Class<?>> sortedClasses = topologicalSort(atomClasses);

        // Create instances in sorted order
        for (Class<?> atomClass : sortedClasses) {
            try {
                Object instance = createInstance(atomClass);
                atoms.put(atomClass, instance);
                System.out.println("Created atom for: " + atomClass.getName());
                injectDependencies(instance);
                invokePostConstruct(instance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create atom for: " + atomClass.getName(), e);
            }
        }
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor.newInstance();
            } else {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    parameters[i] = atoms.get(parameterTypes[i]);
                }
                return constructor.newInstance(parameters);
            }
        }
        throw new RuntimeException("No suitable constructor found for: " + clazz.getName());
    }

    private void invokePostConstruct(Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(instance);
                    System.out.println("Invoked @PostConstruct method: " + method.getName() + " for " + instance.getClass().getName());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to invoke @PostConstruct method: " + method.getName(), e);
                }
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

    private List<Class<?>> topologicalSort(Set<Class<?>> classes) {
        List<Class<?>> sortedList = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();
        Set<Class<?>> visiting = new HashSet<>();

        for (Class<?> clazz : classes) {
            if (!visited.contains(clazz)) {
                topologicalSortUtil(clazz, visited, visiting, sortedList);
            }
        }

        return sortedList;
    }

    private void topologicalSortUtil(Class<?> clazz, Set<Class<?>> visited, Set<Class<?>> visiting, List<Class<?>> sortedList) {
        if (visiting.contains(clazz)) {
            throw new RuntimeException("Cyclic dependency detected: " + clazz.getName());
        }

        if (!visited.contains(clazz)) {
            visiting.add(clazz);
            for (Class<?> dependency : dependencyGraph.getOrDefault(clazz, Collections.emptyList())) {
                topologicalSortUtil(dependency, visited, visiting, sortedList);
            }
            visiting.remove(clazz);
            visited.add(clazz);
            sortedList.add(clazz);
        }
    }

    public Map<Class<?>, Object> getAtoms() {
        return atoms;
    }
}