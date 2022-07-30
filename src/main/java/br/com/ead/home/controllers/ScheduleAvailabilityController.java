package br.com.ead.home.controllers;

import br.com.ead.home.common.injectables.Controller;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.ScheduleAvailabilityService;

import java.time.ZonedDateTime;
import java.util.Set;

public record ScheduleAvailabilityController(ScheduleAvailabilityService availabilityService) implements Controller {

    public Set<TimeSlot> getBookableAvailabilities(ClinicianId clinicianId,
                                                   ZonedDateTime from,
                                                   ZonedDateTime until) {
        return availabilityService.getBookableAvailabilities(clinicianId, from, until);
    }
}
