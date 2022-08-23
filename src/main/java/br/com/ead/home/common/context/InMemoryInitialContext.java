package br.com.ead.home.common.context;

import br.com.ead.home.common.injectables.Bean;

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

    private static final Map<String, Bean> cache = new ConcurrentHashMap<>();

    public void bind(String key, Bean value) {
        cache.put(key, value);
    }

    public <T extends Bean> T lookup(String key) {
        return (T) Optional.ofNullable(cache.get(key))
                .orElseThrow(() -> new IllegalStateException("No bean register for jndi name %s".formatted(key)));
    }
}