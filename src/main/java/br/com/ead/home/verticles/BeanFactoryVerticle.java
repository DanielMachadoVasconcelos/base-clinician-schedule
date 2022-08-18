package br.com.ead.home.verticles;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.loader.InMemoryBeanLoader;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.SystemEnvironmentVariables;
import io.vertx.core.AbstractVerticle;

public class BeanFactoryVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        InitialContext initialContext = InMemoryInitialContext.getInstance();
        Environment environment = new SystemEnvironmentVariables();
        NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);
        InMemoryBeanLoader loader = new InMemoryBeanLoader(environment, initialContext, resolver);
        loader.init();
    }
}
