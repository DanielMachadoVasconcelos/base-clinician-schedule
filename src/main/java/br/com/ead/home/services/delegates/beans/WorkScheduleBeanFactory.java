package br.com.ead.home.services.delegates.beans;

import br.com.ead.home.configurations.SystemClockProvider;
import br.com.ead.home.repositories.MockClinicianWorkScheduleRepository;
import br.com.ead.home.services.ClinicianWorkScheduleService;
import br.com.ead.home.services.api.WorkScheduleService;
import lombok.extern.log4j.Log4j2;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Log4j2
public class WorkScheduleBeanFactory {

    public static WorkScheduleService creatUnitTest() {
        LocalDate today = LocalDate.now();
        LocalTime eight = LocalTime.of(8,0 ,0);

        Instant virtualToday = ZonedDateTime.of(today, eight, ZoneOffset.UTC).toInstant();
        SystemClockProvider systemClockProvider = () -> Clock.fixed(virtualToday, ZoneOffset.UTC);

        return new ClinicianWorkScheduleService(new MockClinicianWorkScheduleRepository(systemClockProvider));
    }
}
