package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.configurations.SystemClockProvider;
import br.com.ead.home.services.delegates.WorkScheduleDelegate;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static br.com.ead.home.services.delegates.types.WorkScheduleServiceType.*;

public class WorkScheduleDelegateFactory {

    private static final LocalDate today = LocalDate.now();
    private static final LocalTime eight = LocalTime.of(8,0 ,0);

    private static final Instant virtualToday = ZonedDateTime.of(today, eight, ZoneOffset.UTC).toInstant();
    private static final SystemClockProvider systemClockProvider = () -> Clock.fixed(virtualToday, ZoneOffset.UTC);

    public static WorkScheduleDelegate createClinicianWorkScheduleService(){
        return new WorkScheduleDelegate(MOCKED);
    }
}
