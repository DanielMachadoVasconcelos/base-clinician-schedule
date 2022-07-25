package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.configurations.SystemClockProvider;
import br.com.ead.home.repositories.MockClinicianWorkScheduleRepository;
import br.com.ead.home.services.ClinicianWorkScheduleService;
import br.com.ead.home.services.api.WorkScheduleService;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class WorkScheduleLookup {

    private static final LocalDate today = LocalDate.now();
    private static final LocalTime eight = LocalTime.of(8,0 ,0);

    private static final Instant virtualToday = ZonedDateTime.of(today, eight, ZoneOffset.UTC).toInstant();
    private static final SystemClockProvider systemClockProvider = () -> Clock.fixed(virtualToday, ZoneOffset.UTC);

    //TODO: Improve this code to consider the partition
    public WorkScheduleService getService(ServiceStageType stage, ServicePartitionType partition) {
        switch (stage) {
            case UNIT_TEST:
            case INTEGRATION_TEST:
            case END_TO_END_TEST:
            default:
             return new ClinicianWorkScheduleService(new MockClinicianWorkScheduleRepository(systemClockProvider));
        }
    }
}
