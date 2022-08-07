package br.com.ead.home.repositories.implementations;

import br.com.ead.home.configurations.system.ClockProvider;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.ShiftRepository;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class MockClinicianWorkScheduleRepository implements ShiftRepository {

    private static final Set<String> CLINICIANS = Set.of("Thomas", "Sara", "Robert", "Anton", "Pedro", "Daniel", "Karl", "Harry", "Nikita", "Lee Niko");

    private final Map<ClinicianId, Set<Shift>> database;
    private final ClockProvider clockProvider;

    public MockClinicianWorkScheduleRepository(ClockProvider clockProvider) {
        this.clockProvider = clockProvider;
        this.database = CLINICIANS.stream()
                .map(ClinicianId::new)
                .map(this::createFakeShift)
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(Shift::clinicianId, Collectors.toSet()));
    }

    @Override
    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        return database.getOrDefault(clinicianId, Set.of());
    }

    @Override
    public Set<Shift> findAllShifts() {
        log.debug("Getting all Shifts from the database");
        return database.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
    }

    @Override
    public Shift scheduleShift(Shift shift) {
        if(database.containsKey(shift.clinicianId())) {
            Set<Shift> shifts = database.getOrDefault(shift.clinicianId(), Sets.newHashSet());
            shifts.add(shift);
            database.put(shift.clinicianId(), shifts);
            return shift;
        }
        database.put(shift.clinicianId(), Sets.newHashSet(shift));
        return shift;
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
