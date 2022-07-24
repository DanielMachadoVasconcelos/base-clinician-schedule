package br.com.ead.home;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Schedule;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.MockScheduleRepository;
import br.com.ead.home.services.ClinicianScheduleConfiguration;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.TimeSlotSlicer;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class Application {

    private static final ScheduleService scheduleService = new ScheduleService(new MockScheduleRepository());
    private static final TimeSlotSlicer slicer = new ClinicianScheduleConfiguration.Builder()
            .setClock(Clock.fixed(ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 00), ZoneOffset.UTC).toInstant(), ZoneOffset.UTC))
            .setMeetingLength(Duration.ofHours(1))
            .build();

    public static void main(String[] args) {

        Map<ClinicianId, Schedule> shifts = scheduleService.findAllShifts().stream()
                .collect(Collectors.toMap(Shift::clinicianId,
                        shift -> new Schedule(shift.clinicianId(), Set.of(shift), Set.of()),
                        Schedule::mergeByClinician));

        Map<ClinicianId, Schedule> appointments = scheduleService.findAllAppointment().stream()
                .collect(Collectors.toMap(Appointment::clinicianId,
                        appointment -> new Schedule(appointment.clinicianId(), Set.of(), Set.of(appointment)),
                        Schedule::mergeByClinician));

        ZonedDateTime now = ZonedDateTime.now();
        Map<ClinicianId, Schedule> schedules = Stream.of(shifts, appointments)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Schedule::mergeByClinician));

        Set<TimeSlot> bookable = schedules.values().stream()
                .map(schedule -> schedule.getBookableAvailability(slicer))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        log.info("Completed! Took={}, Total={}",
                DurationFormatUtils.formatDurationHMS(Duration.between(now, ZonedDateTime.now()).toMillis()),
                bookable.size());
    }

}
