package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.configurations.Environment;
import br.com.ead.home.services.delegates.ScheduleServiceDelegate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public record ScheduleServiceDelegateFactory(Environment environment) {

    public ScheduleServiceDelegate createScheduleService() {
        var partition = environment.getPartition();
        var stage = environment.getStage();
        log.debug("Delegating to schedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return new ScheduleServiceDelegate(stage, partition);
    }
}
