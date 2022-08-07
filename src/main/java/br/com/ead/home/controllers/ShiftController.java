package br.com.ead.home.controllers;

import br.com.ead.home.common.injectables.Controller;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.WorkScheduleService;

public record ShiftController(WorkScheduleService workScheduleService) implements Controller {

    public Shift createShift(ClinicianId clinicianId, TimeSlot timeSlot) {
        Shift shift = new Shift(clinicianId, timeSlot);
        return workScheduleService.scheduleShift(shift);
    }
}
