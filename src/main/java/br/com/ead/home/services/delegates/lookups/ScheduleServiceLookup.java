package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.services.api.ScheduleService;
import br.com.ead.home.services.delegates.beans.ScheduleBeanFactory;
import br.com.ead.home.services.delegates.namespace.NamespaceResolver;
import br.com.ead.home.services.delegates.register.ApplicationBeanRegister;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;
import lombok.extern.log4j.Log4j2;

import static br.com.ead.home.services.delegates.types.ServicePartitionType.SWEDEN;
import static br.com.ead.home.services.delegates.types.ServiceStageType.UNIT_TEST;

@Log4j2
public record ScheduleServiceLookup(NamespaceResolver namespaceResolver) implements ServiceLookup<ScheduleService> {

    private static final ApplicationBeanRegister register = ApplicationBeanRegister.getInstance();

    public ScheduleServiceLookup {
        register.registerBean(namespaceResolver.resolve(UNIT_TEST, SWEDEN, ScheduleService.class.getName()), ScheduleBeanFactory::creatUnitTest);
    }

    public ScheduleService getService(ServiceStageType stage, ServicePartitionType partition) {
        ScheduleService bean = (ScheduleService) register.getBean(stage, partition, ScheduleService.class.getName());
        log.debug("Looking up for work schedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return bean;
    }
}
