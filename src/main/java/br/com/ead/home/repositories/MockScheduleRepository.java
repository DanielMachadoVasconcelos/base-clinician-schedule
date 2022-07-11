package br.com.ead.home.repositories;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.ScheduleService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MockScheduleRepository implements ScheduleRepository {

    @Override
    public Set<Shift> findByClinicianId(ClinicianId clinicianId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.of(8, 0, 0);
        ZonedDateTime seed = ZonedDateTime.of(today, now, ZoneOffset.UTC);

        Predicate<ZonedDateTime> hasNext = item -> seed.plusDays(7).equals(item);
        UnaryOperator<ZonedDateTime> next = item -> item.plusDays(1);

       return Stream.iterate(seed, hasNext, next)
               .map(date -> (TimeSlot) new Slot(date, date.plusHours(8)))
               .map(Set::of)
               .map(Shift::new)
               .collect(Collectors.toSet());
    }

    public static ZonedDateTime time(String time) {
        return time(LocalDate.now(), time);
    }

    public static ZonedDateTime time(LocalDate date, String time) {
        LocalTime now = LocalTime.parse(time);
        return ZonedDateTime.of(date, now, ZoneOffset.UTC);
    }
}
