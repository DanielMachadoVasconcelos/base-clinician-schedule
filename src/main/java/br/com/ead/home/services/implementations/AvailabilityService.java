package br.com.ead.home.services.implementations;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Schedule;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.ScheduleAvailabilityService;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.WorkScheduleService;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public record AvailabilityService(ScheduleService scheduleService,
                                  WorkScheduleService shiftService,
                                  BookablePreferenceService clinicianPreferences) implements ScheduleAvailabilityService {

    public Set<TimeSlot> getBookableAvailabilities(ClinicianId clinicianId,
                                                   ZonedDateTime startAt,
                                                   ZonedDateTime entAt) {

        Preconditions.checkNotNull(clinicianId, "ClinicianId is mandatory");
        Preconditions.checkNotNull(clinicianId, "A StartAt is mandatory");
        Preconditions.checkNotNull(clinicianId, "A EndAt is mandatory");

        Map<ClinicianId, Schedule> shifts = shiftService.findAllByClinicianId(clinicianId).stream()
                .collect(Collectors.toMap(Shift::clinicianId,
                        shift -> new Schedule(shift.clinicianId(), Set.of(shift), Set.of()),
                        Schedule::mergeByClinician));

        Map<ClinicianId, Schedule> appointments = scheduleService.findAllByClinicianId(clinicianId).stream()
                .collect(Collectors.toMap(Appointment::clinicianId,
                        appointment -> new Schedule(appointment.clinicianId(), Set.of(), Set.of(appointment)),
                        Schedule::mergeByClinician));

        Map<ClinicianId, Schedule> schedules = Stream.of(shifts, appointments)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Schedule::mergeByClinician));

        Set<TimeSlot> bookable = schedules.values().stream()
                .map(schedule -> schedule.getBookableAvailability(clinicianPreferences.findClinicianPreferences(clinicianId)))
                .flatMap(Set::stream)
                .filter(timeSlot -> timeSlot.start().isAfter(startAt))
                .filter(timeSlot -> timeSlot.end().isBefore(entAt))
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.debug("Found a total of {} bookable availabilities", bookable.size());
        return bookable;
    }
}
