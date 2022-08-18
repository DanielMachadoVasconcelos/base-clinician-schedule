package br.com.ead.home.services.validations;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum AppointmentErrorCodes {

    APPOINTMENT_IS_NULL("appointment", "Appointment is mandatory"),
    CLINICIAN_IS_NULL("clinician_id", "Appointment clinicianId is mandatory"),
    PATIENT_IS_NULL("patient_id", "Appointment patientId is mandatory"),
    TIME_SLOT_IS_NULL("time_slot", "Appointment timeSlot is mandatory"),
    ;

    private String field;
    private String message;

    AppointmentErrorCodes(String field, String message) {
        this.field = field;
        this.message = message;
    }
}