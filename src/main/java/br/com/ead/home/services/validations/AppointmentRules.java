package br.com.ead.home.services.validations;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.services.exceptions.AppointmentException;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static br.com.ead.home.services.validations.AppointmentErrorCodes.APPOINTMENT_IS_NULL;
import static br.com.ead.home.services.validations.AppointmentErrorCodes.CLINICIAN_IS_NULL;
import static br.com.ead.home.services.validations.AppointmentErrorCodes.PATIENT_IS_NULL;
import static br.com.ead.home.services.validations.AppointmentErrorCodes.TIME_SLOT_IS_NULL;
import static br.com.ead.home.services.validations.AppointmentValidations.validateAppointmentIsNotNull;
import static br.com.ead.home.services.validations.AppointmentValidations.validateClinicianIdNotNull;
import static br.com.ead.home.services.validations.AppointmentValidations.validatePatientIdNotNull;
import static br.com.ead.home.services.validations.AppointmentValidations.validateTimeSlotNotNull;

@Getter
@Log4j2
@ToString
public enum AppointmentRules {

    APPOINTMENT_IS_MANDATORY(APPOINTMENT_IS_NULL, validateAppointmentIsNotNull),
    TIME_SLOT_IS_MANDATORY(TIME_SLOT_IS_NULL, validateTimeSlotNotNull),
    CLINICIAN_ID_IS_MANDATORY(CLINICIAN_IS_NULL, validateClinicianIdNotNull),
    PATIENT_ID_IS_MANDATORY(PATIENT_IS_NULL, validatePatientIdNotNull);

    private AppointmentErrorCodes errorCode;
    private Predicate<Appointment> validation;

    AppointmentRules(AppointmentErrorCodes errorCode, Predicate<Appointment> validation) {
        this.errorCode = errorCode;
        this.validation = validation;
    }
    public static void validateAndThrow(Appointment appointment) {

        AppointmentException exception = validate(appointment).stream()
                .reduce(new AppointmentException("Appointment is invalid!"),
                        AppointmentException::addErrorCode,
                        AppointmentException::merge);

        if (exception.hasErrors()) {
            throw exception;
        }
    }

    public static Set<AppointmentErrorCodes> validate(Appointment appointment) {
        return Arrays.stream(AppointmentRules.values())
                .filter(entry -> !entry.getValidation().test(appointment))
                .map(AppointmentRules::getErrorCode)
                .collect(Collectors.toSet());
    }
}
