package br.com.ead.home;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Schedule;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.flowables.GroupedFlowable;
import org.apache.commons.lang3.RandomUtils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {

    public static void main(String[] args) {
       Duration duration = Duration.ofHours(1);

        Flowable<GroupedFlowable<ClinicianId, Schedule>> shiftsPerClinician = Flowable.fromStream(generator(duration))
                .map(partition -> Slot.from(partition, duration))
                .map(timeSlot -> new Shift(generateClinicianId(), timeSlot))
                .groupBy(Shift::clinicianId, shift -> new Schedule(shift.clinicianId(), Set.of(shift), Set.of()));

        Flowable<GroupedFlowable<ClinicianId, Schedule>> meetingsPerClinician = Flowable.fromStream(generator(duration))
                .filter(slot -> RandomUtils.nextBoolean())
                .map(partition -> Slot.from(partition, duration))
                .map(timeSlot -> new Appointment(generateClinicianId(), generatePatientId() ,timeSlot))
                .groupBy(Appointment::clinicianId, meeting -> new Schedule(meeting.clinicianId(), Set.of(), Set.of(meeting)));


        Flowable.concat(meetingsPerClinician, shiftsPerClinician)
                .flatMapSingle(groupByClinician -> groupByClinician.collect(Collectors.reducing(Schedule::mergeByClinicianAndShift)))
                .flatMap(Flowable::fromOptional)
                .groupBy(Schedule::clinicianId, schedule -> new Schedule(schedule.clinicianId(), schedule.shift(), schedule.bookings()))
                .flatMapSingle(groupByClinician -> groupByClinician.collect(Collectors.reducing(Schedule::mergeByClinicianAndShift)))
                .flatMap(Flowable::fromOptional)
                .subscribe(item -> System.out.println(String.format("Item: %s", item)),
                           error -> System.err.println(String.format("Error: %s", error)),
                           () -> System.out.println("Completed!"));


    }

    private static ClinicianId generateClinicianId() {
        return new Random().nextBoolean() ? new ClinicianId("Thomas") : new ClinicianId("Jakob");
    }

    private static PatientId generatePatientId() {
        return new Random().nextBoolean() ? new PatientId("Sara") : new PatientId("Daniel");
    }

    private static Stream<ZonedDateTime> generator(Duration duration) {

        ZoneId zone = ZoneId.of("Europe/Stockholm");
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.of(12, 00, 00);

        ZonedDateTime startAt = ZonedDateTime.of(today, now, zone).truncatedTo(ChronoUnit.MINUTES);
        ZonedDateTime endsAt = startAt.plusDays(1);

        return Stream.iterate(startAt, slot -> slot.isBefore(endsAt), slot -> slot.plus(duration));
    }
}
