package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.services.api.WorkScheduleService;
import br.com.ead.home.services.delegates.register.WorkScheduleRegister;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;
import lombok.extern.log4j.Log4j2;

@Log4j2
public record WorkScheduleServiceLookup(WorkScheduleRegister register) implements ServiceLookup<WorkScheduleService> {

    public WorkScheduleService getService(ServiceStageType stage, ServicePartitionType partition) {
        WorkScheduleService bean = register.getBean(stage, partition);
        log.info("Looking up for work schedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return bean;
    }
}
