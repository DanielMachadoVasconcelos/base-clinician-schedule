package br.com.ead.home.repositories.implementations;

import br.com.ead.home.configurations.system.ClockProvider;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.ShiftRepository;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public record MockClinicianWorkScheduleRepository(ClockProvider clockProvider) implements ShiftRepository {

    private static final Set<String> CLINICIANS = Set.of("Thomas", "Sara", "Robert", "Anton", "Pedro", "Daniel", "Karl", "Harry", "Nikita", "Lee Niko");

    @Override
    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        return CLINICIANS.stream()
                .map(ClinicianId::new)
                .filter(clinicianId::equals)
                .map(this::createFakeShift)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Shift> findAllShifts() {
        log.debug("Getting all Shifts from the database");
        return CLINICIANS.stream()
                .map(ClinicianId::new)
                .map(this::findAllByClinicianId)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private Set<Shift> createFakeShift(ClinicianId clinicianId) {
        log.debug("Getting all Shifts in the database for clinician={}", clinicianId.value());
        ZonedDateTime seed = ZonedDateTime.now(clockProvider.currentSystemClock());

        Predicate<ZonedDateTime> hasNext = item -> seed.plusDays(180).isAfter(item);
        UnaryOperator<ZonedDateTime> next = item -> item.plusDays(1);

        Set<Shift> shifts = Stream.iterate(seed, hasNext, next)
                .map(date -> (TimeSlot) new Slot(date, date.plusHours(8)))
                .map(timeSlot -> new Shift(clinicianId, timeSlot))
                .collect(Collectors.toSet());

        log.debug("Got a total={} shifts from Clinician={}", shifts.size(), clinicianId);
        return shifts;
    }
}
