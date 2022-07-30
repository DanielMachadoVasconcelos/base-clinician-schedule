package br.com.ead.home.common.caches;

import br.com.ead.home.common.injectors.Bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record InMemoryBeanCache() implements BeanCache {

    private static final Map<String, Bean> mapOfBeans = new ConcurrentHashMap<>();

    @Override
    public <T extends Bean> T getBean(String name) {
        return (T) mapOfBeans.get(name);
    }

    @Override
    public <T extends Bean> void putBean(T bean) {
        mapOfBeans.put(bean.getClass().getName(), bean);
    }
}
