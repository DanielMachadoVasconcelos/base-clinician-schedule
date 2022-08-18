package br.com.ead.home.services.validations;

import br.com.ead.home.models.Appointment;

import java.util.Objects;
import java.util.function.Predicate;

public class AppointmentValidations {

    public static final Predicate<Appointment> validateAppointmentIsNotNull =
            Objects::nonNull;

    public static final Predicate<Appointment> validateClinicianIdNotNull =
            appointment -> Objects.nonNull(appointment.clinicianId());

    public static final Predicate<Appointment> validatePatientIdNotNull =
            appointment -> Objects.nonNull(appointment.patientId());

    public static final Predicate<Appointment> validateTimeSlotNotNull =
            appointment -> Objects.nonNull(appointment.timeSlot());
}
