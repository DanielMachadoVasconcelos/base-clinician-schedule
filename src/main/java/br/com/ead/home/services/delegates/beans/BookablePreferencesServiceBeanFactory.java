package br.com.ead.home.services.delegates.beans;

import br.com.ead.home.configurations.SystemClockProvider;
import br.com.ead.home.repositories.MockClinicianPreferencesRepository;
import br.com.ead.home.services.ClinicianPreferencesService;
import br.com.ead.home.services.api.BookablePreferenceService;
import lombok.extern.log4j.Log4j2;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Log4j2
public class BookablePreferencesServiceBeanFactory {

    public static BookablePreferenceService creatUnitTest() {
        LocalDate today = LocalDate.now();
        LocalTime eight = LocalTime.of(8,0 ,0);

        Instant virtualToday = ZonedDateTime.of(today, eight, ZoneOffset.UTC).toInstant();
        SystemClockProvider systemClockProvider = () -> Clock.fixed(virtualToday, ZoneOffset.UTC);

        return new ClinicianPreferencesService(new MockClinicianPreferencesRepository(systemClockProvider));
    }
}
