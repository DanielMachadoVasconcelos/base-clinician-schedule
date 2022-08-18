package br.com.ead.home.controllers;

import br.com.ead.home.common.injectables.Controller;
import br.com.ead.home.models.Appointment;
import br.com.ead.home.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class AppointmentController implements Controller {

    private final ScheduleService scheduleService;

    // TODO: create a service method to validate, create and persist an appointment. Using different validations for every partition
    public Appointment createAppointment(Appointment appointment) {
        return scheduleService.bookAppointment(appointment);
    }
}
