package br.com.ead.home.repositories;

import br.com.ead.home.configurations.SystemClockProvider;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public record MockClinicianWorkScheduleRepository(SystemClockProvider systemClockProvider) implements ShiftRepository {

    @Override
    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        log.debug("Getting all Shifts in the database for clinician={}", clinicianId.value());
        ZonedDateTime seed = ZonedDateTime.now(systemClockProvider.currentSystemClock());

        Predicate<ZonedDateTime> hasNext = item -> seed.plusDays(180).isAfter(item);
        UnaryOperator<ZonedDateTime> next = item -> item.plusDays(1);

        Set<Shift> shifts = Stream.iterate(seed, hasNext, next)
                .map(date -> (TimeSlot) new Slot(date, date.plusHours(8)))
                .map(timeSlot -> new Shift(clinicianId, timeSlot))
                .collect(Collectors.toSet());

        log.debug("Got a total={} shifts from Clinician={}", shifts.size(), clinicianId);
        return shifts;
    }

    @Override
    public Set<Shift> findAllShifts() {
        log.debug("Getting all Shifts from the database");
        return Stream.of("Thomas", "Sara", "Robert", "Anton", "Pedro", "Daniel", "Karl", "Harry", "Nikita", "Lee Niko")
                .map(ClinicianId::new)
                .map(this::findAllByClinicianId)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
