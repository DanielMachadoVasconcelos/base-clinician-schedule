package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.services.api.BookablePreferenceService;
import br.com.ead.home.services.delegates.beans.BookablePreferencesServiceBeanFactory;
import br.com.ead.home.services.delegates.namespace.NamespaceResolver;
import br.com.ead.home.services.delegates.register.ApplicationBeanRegister;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;
import lombok.extern.log4j.Log4j2;

import static br.com.ead.home.services.delegates.types.ServicePartitionType.SWEDEN;
import static br.com.ead.home.services.delegates.types.ServiceStageType.UNIT_TEST;

@Log4j2
public record BookablePreferencesServiceLookup(NamespaceResolver namespaceResolver) implements ServiceLookup<BookablePreferenceService> {

    private static final ApplicationBeanRegister register = ApplicationBeanRegister.getInstance();

    public BookablePreferencesServiceLookup {
        register.registerBean(namespaceResolver.resolve(UNIT_TEST, SWEDEN, BookablePreferenceService.class.getName()), BookablePreferencesServiceBeanFactory::creatUnitTest);
    }

    @Override
    public BookablePreferenceService getService(ServiceStageType stage, ServicePartitionType partition) {
        BookablePreferenceService bean = (BookablePreferenceService) register.getBean(stage, partition, BookablePreferenceService.class.getName());
        log.debug("Looking up for bookable preference service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return bean;
    }
}
