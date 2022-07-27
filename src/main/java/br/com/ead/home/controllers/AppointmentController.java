package br.com.ead.home.controllers;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import br.com.ead.home.services.AppointmentService;
import org.apache.commons.lang3.NotImplementedException;

public record AppointmentController(AppointmentService appointmentService) {

    // TODO: create a service method to validate, create and persist an appointment.
    public Appointment createAppointment(ClinicianId clinicianId, PatientId patientId, TimeSlot timeSlot) {
        throw new NotImplementedException("Not implemented yet");
    }
}
