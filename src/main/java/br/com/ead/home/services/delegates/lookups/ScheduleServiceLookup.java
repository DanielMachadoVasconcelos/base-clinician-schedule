package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.common.lookups.InMemoryLookup;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.delegates.beans.ScheduleBeanFactory;
import br.com.ead.home.services.delegates.register.ApplicationBeanRegister;
import lombok.extern.log4j.Log4j2;

import static br.com.ead.home.common.types.PartitionType.FRANCE;
import static br.com.ead.home.common.types.PartitionType.SWEDEN;
import static br.com.ead.home.common.types.StageType.UNIT_TEST;

@Log4j2
public record ScheduleServiceLookup(NamespaceResolver namespaceResolver) implements InMemoryLookup<ScheduleService> {

    private static final ApplicationBeanRegister register = ApplicationBeanRegister.getInstance();

    public ScheduleServiceLookup {
        register.registerBean(namespaceResolver.resolve(UNIT_TEST, SWEDEN, ScheduleService.class.getName()), ScheduleBeanFactory::creatUnitTest);
        register.registerBean(namespaceResolver.resolve(UNIT_TEST, FRANCE, ScheduleService.class.getName()), ScheduleBeanFactory::creatUnitTest);
    }

    public ScheduleService lookup(StageType stage, PartitionType partition) {
        ScheduleService bean = (ScheduleService) register.getBean(stage, partition, ScheduleService.class.getName());
        log.debug("Looking up for work schedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return bean;
    }
}
