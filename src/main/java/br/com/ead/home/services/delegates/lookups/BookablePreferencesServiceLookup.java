package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.common.lookups.ServiceLookup;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.register.ApplicationBeanRegister;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.delegates.factories.BookablePreferencesServiceBeanFactory;
import br.com.ead.home.types.ServicePartitionType;
import br.com.ead.home.types.ServiceStageType;
import lombok.extern.log4j.Log4j2;

@Log4j2
public record BookablePreferencesServiceLookup(NamespaceResolver namespaceResolver) implements ServiceLookup<BookablePreferenceService> {

    private static final ApplicationBeanRegister register = ApplicationBeanRegister.getInstance();

    public BookablePreferencesServiceLookup {
        for (ServiceStageType stage: ServiceStageType.values()){
            for (ServicePartitionType partition: ServicePartitionType.values()){
                String namespace = namespaceResolver.resolve(stage, partition, BookablePreferenceService.class.getName());
                register.registerBean(namespace, BookablePreferencesServiceBeanFactory::creatUnitTest);
            }
        }
    }

    @Override
    public BookablePreferenceService getService(ServiceStageType stage, ServicePartitionType partition) {
        BookablePreferenceService bean = (BookablePreferenceService) register.getBean(stage, partition, BookablePreferenceService.class.getName());
        log.debug("Looking up for bookable preference service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return bean;
    }
}
