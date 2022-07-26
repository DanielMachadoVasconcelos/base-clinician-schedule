package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.configurations.Environment;
import br.com.ead.home.services.delegates.WorkScheduleDelegate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public record WorkScheduleServiceDelegateFactory(Environment environment) {

    public WorkScheduleDelegate createWorkScheduleService() {
        var partition = environment.getPartition();
        var stage = environment.getStage();
        log.info("Delegating to work schedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return new WorkScheduleDelegate(stage, partition);
    }
}
