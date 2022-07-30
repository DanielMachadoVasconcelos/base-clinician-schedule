package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.common.lookups.ServiceLookup;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.register.ApplicationBeanRegister;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.delegates.factories.WorkScheduleBeanFactory;
import br.com.ead.home.types.ServicePartitionType;
import br.com.ead.home.types.ServiceStageType;
import lombok.extern.log4j.Log4j2;

import static br.com.ead.home.types.ServicePartitionType.FRANCE;
import static br.com.ead.home.types.ServicePartitionType.SWEDEN;
import static br.com.ead.home.types.ServiceStageType.UNIT_TEST;

@Log4j2
public record WorkScheduleServiceLookup(NamespaceResolver namespaceResolver) implements ServiceLookup<WorkScheduleService> {

    private static final ApplicationBeanRegister register = ApplicationBeanRegister.getInstance();

    public WorkScheduleServiceLookup {
        register.registerBean(namespaceResolver.resolve(UNIT_TEST, SWEDEN, WorkScheduleService.class.getName()), WorkScheduleBeanFactory::creatUnitTest);
        register.registerBean(namespaceResolver.resolve(UNIT_TEST, FRANCE, WorkScheduleService.class.getName()), WorkScheduleBeanFactory::creatUnitTest);
    }

    public WorkScheduleService getService(ServiceStageType stage, ServicePartitionType partition) {
        WorkScheduleService bean = (WorkScheduleService) register.getBean(stage, partition, WorkScheduleService.class.getName());
        log.debug("Looking up for work schedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return bean;
    }
}
