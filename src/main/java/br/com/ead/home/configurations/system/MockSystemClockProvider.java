package br.com.ead.home.configurations.system;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public record MockSystemClockProvider() implements ClockProvider {

    @Override
    public Clock currentSystemClock() {
        LocalDate today = LocalDate.now();
        LocalTime eight = LocalTime.of(8,0 ,0);
        Instant virtualToday = ZonedDateTime.of(today, eight, ZoneOffset.UTC).toInstant();
        return Clock.fixed(virtualToday, ZoneOffset.UTC);
    }
}
