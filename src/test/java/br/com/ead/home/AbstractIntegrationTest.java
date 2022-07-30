package br.com.ead.home;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.loader.InMemoryBeanLoader;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.SystemEnvironmentVariables;
import br.com.ead.home.configurations.system.ClockProvider;
import br.com.ead.home.configurations.system.MockSystemClockProvider;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractIntegrationTest {

    protected static final ClockProvider clockProvider = new MockSystemClockProvider();

    protected static final InitialContext initialContext = InMemoryInitialContext.getInstance();

    protected static final Environment environment = new SystemEnvironmentVariables();
    protected static final NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);
    protected static final InMemoryBeanLoader loader = new InMemoryBeanLoader(environment, initialContext, resolver);

    @BeforeAll
    static void afterAll() {
        loader.init();
    }
}
