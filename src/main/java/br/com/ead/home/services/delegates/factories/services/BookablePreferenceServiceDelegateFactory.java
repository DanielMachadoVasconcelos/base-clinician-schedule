package br.com.ead.home.services.delegates.factories.services;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.factories.AbstractServiceFactory;
import br.com.ead.home.common.injectables.Service;
import br.com.ead.home.common.lookups.InMemoryLookup;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.delegates.BookablePreferenceServiceDelegate;
import br.com.ead.home.services.delegates.lookups.BookablePreferencesServiceLookup;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BookablePreferenceServiceDelegateFactory extends AbstractServiceFactory {

    @Override
    public Service getService(Environment environment) {
        log.debug("Delegating to work schedule service. Using Stage %s and Partition %s bean".formatted(environment.getStage(), environment.getPartition()));
        InMemoryLookup<BookablePreferenceService> lookup =
                new BookablePreferencesServiceLookup(
                        environment,
                        InMemoryInitialContext.getInstance(),
                        new InMemoryNamespaceResolver(environment));
        return new BookablePreferenceServiceDelegate(lookup);
    }
}
