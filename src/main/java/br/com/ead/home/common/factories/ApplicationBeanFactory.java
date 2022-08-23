package br.com.ead.home.common.factories;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.injectables.Bean;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.SystemEnvironmentVariables;

public class ApplicationBeanFactory {

    private static InitialContext initialContext = InMemoryInitialContext.getInstance();
    private static Environment environment = new SystemEnvironmentVariables();
    private static NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);

    public static <T extends Bean> T getBean(Class<T> clazz) {
        String jndiName = resolver.namespace(environment.getStage(), environment.getPartition(), clazz.getName());
        return initialContext.lookup(jndiName);
    }
}
