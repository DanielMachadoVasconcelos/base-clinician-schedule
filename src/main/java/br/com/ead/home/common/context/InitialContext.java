package br.com.ead.home.common.context;

import br.com.ead.home.common.injectables.Bean;

public interface InitialContext {

    <T extends Bean> void bind(String key, T value);

    <T extends Bean> T lookup(String key);

}
