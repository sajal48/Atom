package com.sajal.atom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtomContext {
    private static AtomContext instance;
    private final Map<String, Object> context = new ConcurrentHashMap<>();

    private AtomContext() {
        // Private constructor to prevent instantiation
    }

    public static synchronized AtomContext getInstance() {
        if (instance == null) {
            instance = new AtomContext();
        }
        return instance;
    }

    public void initialize(AtomFactory atomFactory) {
        Map<Class<?>, Object> atoms = atomFactory.getAtoms();
        for (Map.Entry<Class<?>, Object> entry : atoms.entrySet()) {
            context.put(entry.getKey().getName(), entry.getValue());
        }
    }

    public void put(String key, Object value) {
        context.put(key, value);
    }

    public Object get(String key) {
        return context.get(key);
    }

    public <T> T getAtom(Class<T> clazz) {
        return clazz.cast(context.get(clazz.getName()));
    }
}