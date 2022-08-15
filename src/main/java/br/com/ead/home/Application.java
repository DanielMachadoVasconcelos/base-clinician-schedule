package br.com.ead.home;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.loader.InMemoryBeanLoader;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.SystemEnvironmentVariables;
import io.vertx.core.Launcher;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Application {

    static {
        InitialContext initialContext = InMemoryInitialContext.getInstance();
        Environment environment = new SystemEnvironmentVariables();
        NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);
        InMemoryBeanLoader loader = new InMemoryBeanLoader(environment, initialContext, resolver);
        
        loader.init();
    }

    public static void main(String[] args) {
        Launcher.main(new String[]{"run", "br.com.ead.home.MainVerticle"});
    }
}
