package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.common.lookups.InMemoryLookup;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.delegates.beans.BookablePreferencesServiceBeanFactory;
import br.com.ead.home.services.delegates.register.ApplicationBeanRegister;
import lombok.extern.log4j.Log4j2;

import static br.com.ead.home.common.types.PartitionType.FRANCE;
import static br.com.ead.home.common.types.PartitionType.SWEDEN;
import static br.com.ead.home.common.types.StageType.UNIT_TEST;

@Log4j2
public record BookablePreferencesServiceLookup(NamespaceResolver resolver) implements InMemoryLookup<BookablePreferenceService> {

    private static final ApplicationBeanRegister register = ApplicationBeanRegister.getInstance();

    public BookablePreferencesServiceLookup {
        register.registerBean(resolver.resolve(UNIT_TEST, SWEDEN, BookablePreferenceService.class.getName()), BookablePreferencesServiceBeanFactory::creatUnitTest);
        register.registerBean(resolver.resolve(UNIT_TEST, FRANCE, BookablePreferenceService.class.getName()), BookablePreferencesServiceBeanFactory::creatUnitTest);
    }

    @Override
    public BookablePreferenceService lookup(StageType stage, PartitionType partition) {
        BookablePreferenceService bean = (BookablePreferenceService) register.getBean(stage, partition, BookablePreferenceService.class.getName());
        log.debug("Looking up for bookable preference service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return bean;
    }

}
