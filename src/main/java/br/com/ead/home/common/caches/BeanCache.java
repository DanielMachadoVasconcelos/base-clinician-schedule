package br.com.ead.home.common.caches;

import br.com.ead.home.common.injectors.Bean;

public interface BeanCache {

    <T extends Bean> T getBean(String name);

    <T extends Bean> void putBean(T service);
}
