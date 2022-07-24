package br.com.ead.home.controllers;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.ScheduleService;

import java.time.ZonedDateTime;
import java.util.Set;

public record ScheduleController(ScheduleService scheduleService) {

    public Set<TimeSlot> getBookableAvailabilities(ClinicianId clinicianId,
                                                   ZonedDateTime from,
                                                   ZonedDateTime until) {
        return scheduleService.getBookableAvailabilities(clinicianId, from, until);
    }
}
