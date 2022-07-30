package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.delegates.beans.WorkScheduleBeanFactory;
import br.com.ead.home.services.delegates.namespace.NamespaceResolver;
import br.com.ead.home.services.delegates.register.ApplicationBeanRegister;
import lombok.extern.log4j.Log4j2;

import static br.com.ead.home.common.types.PartitionType.FRANCE;
import static br.com.ead.home.common.types.PartitionType.SWEDEN;
import static br.com.ead.home.common.types.StageType.UNIT_TEST;

@Log4j2
public record WorkScheduleServiceLookup(NamespaceResolver namespaceResolver) implements ServiceLookup<WorkScheduleService> {

    private static final ApplicationBeanRegister register = ApplicationBeanRegister.getInstance();

    public WorkScheduleServiceLookup {
        register.registerBean(namespaceResolver.resolve(UNIT_TEST, SWEDEN, WorkScheduleService.class.getName()), WorkScheduleBeanFactory::creatUnitTest);
        register.registerBean(namespaceResolver.resolve(UNIT_TEST, FRANCE, WorkScheduleService.class.getName()), WorkScheduleBeanFactory::creatUnitTest);
    }

    public WorkScheduleService getService(StageType stage, PartitionType partition) {
        WorkScheduleService bean = (WorkScheduleService) register.getBean(stage, partition, WorkScheduleService.class.getName());
        log.debug("Looking up for work schedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return bean;
    }
}
