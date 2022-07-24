package br.com.ead.home.repositories;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class MockScheduleRepository implements ScheduleRepository {

    private static final Random random = new Random();

    @Override
    public Set<Shift> findAllShiftsByClinicianId(ClinicianId clinicianId) {
        log.debug("Getting all Shifts in the database for clinician={}", clinicianId.value());
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.of(8, 0, 0);
        ZonedDateTime seed = ZonedDateTime.of(today, now, ZoneOffset.UTC);

        Predicate<ZonedDateTime> hasNext = item -> seed.plusDays(180).isAfter(item);
        UnaryOperator<ZonedDateTime> next = item -> item.plusDays(1);

        Set<Shift> shifts = Stream.iterate(seed, hasNext, next)
                .map(date -> (TimeSlot) new Slot(date, date.plusHours(8)))
                .map(timeSlot -> new Shift(clinicianId, timeSlot))
                .collect(Collectors.toSet());

        log.debug("Got total={} shifts from Clinician={}", shifts.size(), clinicianId);
        return shifts;
    }

    @Override
    public Set<Appointment> findAllAppointmentByClinicianId(ClinicianId clinicianId) {
        log.debug("Getting all Appointments in the database for clinician={}", clinicianId.value());
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.of(8, 0, 0);
        ZonedDateTime seed = ZonedDateTime.of(today, now, ZoneOffset.UTC);

        Predicate<ZonedDateTime> hasNext = item -> seed.plusDays(180).isAfter(item);
        UnaryOperator<ZonedDateTime> next = item -> item.plusMinutes(30);

        Set<Appointment> appointments = Stream.iterate(seed, hasNext, next)
                .map(date -> (TimeSlot) new Slot(date, date.plusMinutes(30)))
                .filter(this::filterOutRandomly)
                .map(timeSlot -> new Appointment(clinicianId, generatePatientId(), timeSlot))
                .collect(Collectors.toSet());

        log.debug("Got total={} appointments from Clinician={}", appointments.size(), clinicianId);
        return appointments;
    }

    private boolean filterOutRandomly(TimeSlot timeSlot) {
        var first = random.nextBoolean();
        var second = random.nextBoolean();
        var third = random.nextBoolean();
        return first && second && third;
    }

    @Override
    public Set<Shift> findAllShift() {
        log.debug("Getting all Shifts in the database");
        return Stream.of("Thomas", "Sara", "Robert", "Anton", "Pedro", "Daniel", "Karl", "Harry", "Nikita", "Lee Niko")
                .map(ClinicianId::new)
                .map(this::findAllShiftsByClinicianId)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public Set<Appointment> findAllAppointment() {
        log.debug("Getting all Appointments in the database");
        return Stream.of("Thomas", "Sara", "Robert", "Anton", "Pedro", "Daniel", "Karl", "Harry", "Nikita", "Lee Niko")
                .map(ClinicianId::new)
                .map(this::findAllAppointmentByClinicianId)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }


    private static PatientId generatePatientId() {
        List<String> names = List.of("Sara", "Max", "Thomas", "Lee", "Robert", "Anna");
        return new PatientId(names.get(RandomUtils.nextInt(0, names.size())));
    }

    public static ZonedDateTime time(String time) {
        return time(LocalDate.now(), time);
    }

    public static ZonedDateTime time(LocalDate date, String time) {
        LocalTime now = LocalTime.parse(time);
        return ZonedDateTime.of(date, now, ZoneOffset.UTC);
    }
}
