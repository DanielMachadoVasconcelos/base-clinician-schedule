package br.com.ead.home.common.factories;

import br.com.ead.home.common.injectables.Service;
import br.com.ead.home.configurations.Environment;

public abstract class AbstractServiceFactory {

    public abstract Service getService(Environment environment);
}
