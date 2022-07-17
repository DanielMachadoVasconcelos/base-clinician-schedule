package br.com.ead.home;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Schedule;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.MockScheduleRepository;
import br.com.ead.home.services.ClinicianScheduleConfiguration;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.TimeSlotSlicer;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.flowables.GroupedFlowable;

import java.time.*;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {

    private static final ScheduleService scheduleService = new ScheduleService(new MockScheduleRepository());
    private static final TimeSlotSlicer slicer =  new ClinicianScheduleConfiguration.Builder()
            .setClock(Clock.fixed(ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 00), ZoneOffset.UTC).toInstant(), ZoneOffset.UTC))
            .setMeetingLength(Duration.ofMinutes(15))
            .build();

    public static void main(String[] args) {

        Flowable<GroupedFlowable<ClinicianId, Schedule>> shiftsPerClinician =
                Flowable.fromIterable(scheduleService.findAllShift())
                        .groupBy(Shift::clinicianId, shift -> new Schedule(shift.clinicianId(), Set.of(shift), Set.of()));

        Flowable<GroupedFlowable<ClinicianId, Schedule>> meetingsPerClinician =
                Flowable.fromIterable(scheduleService.findAllAppointmentByClinicianId(new ClinicianId("Thomas")))
                        .groupBy(Appointment::clinicianId, meeting -> new Schedule(meeting.clinicianId(), Set.of(), Set.of(meeting)));

        Flowable.concat(meetingsPerClinician, shiftsPerClinician)
                .flatMapSingle(groupByClinician -> groupByClinician.collect(Collectors.reducing(Schedule::mergeByClinicianAndShift)))
                .flatMap(Flowable::fromOptional)
                .groupBy(Schedule::clinicianId, schedule -> new Schedule(schedule.clinicianId(), schedule.shift(), schedule.bookings()))
                .flatMapSingle(groupByClinician -> groupByClinician.collect(Collectors.reducing(Schedule::mergeByClinicianAndShift)))
                .flatMap(Flowable::fromOptional)
                .flatMapIterable(schedule -> schedule.getBookableAvailability(slicer))
                .subscribe(item -> System.out.println(String.format("Item: %s", item)),
                           error -> System.err.println(String.format("Error: %s", error)),
                           () -> System.out.println("Completed!"));
    }
}
