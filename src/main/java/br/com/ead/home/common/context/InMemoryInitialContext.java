package br.com.ead.home.common.context;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryInitialContext implements InitialContext {

    private static final InitialContext INSTANCE = new InMemoryInitialContext();

    private InMemoryInitialContext() {
    }

    public static InitialContext getInstance() {
        return INSTANCE;
    }

    private static final Map<String, Object> cache = new ConcurrentHashMap<>();

    public void bind(String key, Object value) {
        cache.put(key, value);
    }

    public Object lookup(String key) {
        return Optional.ofNullable(cache.get(key))
                .orElseThrow(() -> new IllegalStateException("No bean register for jndi name %s".formatted(key)));
    }
}