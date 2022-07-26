package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.services.api.ScheduleService;
import br.com.ead.home.services.delegates.register.ScheduleServiceRegister;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;
import lombok.extern.log4j.Log4j2;

@Log4j2
public record ScheduleServiceLookup(ScheduleServiceRegister register) implements ServiceLookup<ScheduleService> {

    public ScheduleService getService(ServiceStageType stage, ServicePartitionType partition) {
        ScheduleService bean = register.getBean(stage, partition);
        log.info("Looking up for work schedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return bean;
    }
}
