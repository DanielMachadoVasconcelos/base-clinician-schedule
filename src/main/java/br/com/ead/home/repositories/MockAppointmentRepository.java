package br.com.ead.home.repositories;

import br.com.ead.home.configurations.ClockProvider;
import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public record MockAppointmentRepository(ClockProvider clockProvider) implements AppointmentRepository {

    private static final Set<String> CLINICIANS = Set.of("Thomas", "Sara", "Robert", "Anton", "Pedro", "Daniel", "Karl", "Harry", "Nikita", "Lee Niko");

    @Override
    public Set<Appointment> findAllByClinicianId(ClinicianId clinicianId) {
        return CLINICIANS.stream()
                .map(ClinicianId::new)
                .filter(clinicianId::equals)
                .map(this::createFakeAppointments)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public Set<Appointment> findAllAppointments() {
        log.debug("Getting all Appointments in the database");
        return CLINICIANS.stream()
                .map(ClinicianId::new)
                .map(this::findAllByClinicianId)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private Set<Appointment> createFakeAppointments(ClinicianId clinicianId) {
        log.debug("Getting all Appointments in the database for clinician={}", clinicianId.value());
        ZonedDateTime seed = ZonedDateTime.now(clockProvider.currentSystemClock());

        Predicate<ZonedDateTime> hasNext = item -> seed.plusDays(180).isAfter(item);
        UnaryOperator<ZonedDateTime> next = item -> item.plusMinutes(30);

        Set<Appointment> appointments = Stream.iterate(seed, hasNext, next)
                .map(date -> (TimeSlot) new Slot(date, date.plusMinutes(30)))
                .filter(MockAppointmentRepository::filterOutRandomly)
                .map(timeSlot -> new Appointment(clinicianId, generatePatientId(), timeSlot))
                .collect(Collectors.toSet());

        log.debug("Got total={} appointments from Clinician={}", appointments.size(), clinicianId);
        return appointments;
    }

    private static boolean filterOutRandomly(TimeSlot timeSlot) {
        var first = RandomUtils.nextBoolean();
        var second = RandomUtils.nextBoolean();
        var third = RandomUtils.nextBoolean();
        return first && second && third;
    }

    private static PatientId generatePatientId() {
        List<String> names = List.of("Sara", "Max", "Thomas", "Lee", "Robert", "Anna");
        return new PatientId(names.get(RandomUtils.nextInt(0, names.size())));
    }
}
