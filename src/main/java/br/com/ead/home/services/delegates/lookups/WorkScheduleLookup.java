package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.configurations.SystemClockProvider;
import br.com.ead.home.repositories.MockClinicianWorkScheduleRepository;
import br.com.ead.home.services.ClinicianWorkScheduleService;
import br.com.ead.home.services.api.WorkScheduleService;
import br.com.ead.home.services.delegates.types.WorkScheduleServiceType;

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

    public WorkScheduleService getService(WorkScheduleServiceType type) {
        switch (type) {
            case MOCKED:
            default:
             return new ClinicianWorkScheduleService(new MockClinicianWorkScheduleRepository(systemClockProvider));
        }
    }
}
