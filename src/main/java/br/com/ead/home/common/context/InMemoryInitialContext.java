package br.com.ead.home.common.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record InMemoryInitialContext() implements InitialContext {

    private static final Map<String, Object> cache = new ConcurrentHashMap<>();

    public void bind(String key, Object value) {
        cache.put(key, value);
    }

    public Object lookup(String key) {
        return cache.get(key);
    }
}