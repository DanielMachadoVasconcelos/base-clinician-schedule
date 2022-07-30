package br.com.ead.home.common.context;

import br.com.ead.home.common.injectors.Bean;

public interface InitialContext {

    <T extends Bean> T lookup(Class<T> bean);
    <T extends Bean> void bind(T bean);
}
