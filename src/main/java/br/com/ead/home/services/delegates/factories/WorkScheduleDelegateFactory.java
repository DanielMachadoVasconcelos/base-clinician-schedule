package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.services.delegates.WorkScheduleDelegate;

import static br.com.ead.home.services.delegates.types.ServicePartitionType.SWEDEN;
import static br.com.ead.home.services.delegates.types.ServiceStageType.UNIT_TEST;

public final class WorkScheduleDelegateFactory {

    public static WorkScheduleDelegate createWorkScheduleService(){
        // TODO: future improvement is to load this from a configuration file.
        return new WorkScheduleDelegate(UNIT_TEST, SWEDEN);
    }
}
