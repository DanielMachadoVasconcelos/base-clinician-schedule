package br.com.ead.home.controllers;

import br.com.ead.home.AbstractIntegrationTest;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;

public class AppointmentControllerTest extends AbstractIntegrationTest {

    private AppointmentController classUnderTest;

    @BeforeEach
    void setUp() {
        String jndi = resolver.namespace(StageType.UNIT_TEST, PartitionType.SWEDEN, AppointmentController.class.getName());
        classUnderTest = (AppointmentController) initialContext.lookup(jndi);
    }

    @Test
    @DisplayName("Should save an Appointment when clinician has availability")
    void shouldSaveAnAppointmentWhenClinicianHasAvailability() {
        // given:
        ClinicianId clinicianId = new ClinicianId("Stevenson");
        PatientId patientId = new PatientId("Sara");
        TimeSlot timeSlot = Slot.from(ZonedDateTime.now(), Duration.ofHours(1));
        Appointment request = new Appointment(clinicianId, patientId, timeSlot);

        // when:
        Appointment appointment = classUnderTest.createAppointment(request);

        // then:
        Assertions.assertAll(
            () -> Assertions.assertNotNull(appointment, "Appointment response is mandatory"),
            () -> Assertions.assertEquals(clinicianId, appointment.clinicianId(), "ClinicianId is the expected"),
            () -> Assertions.assertEquals(patientId, appointment.patientId(), "PatientId is the expected"),
            () -> Assertions.assertEquals(timeSlot, appointment.timeSlot(), "Time Slot is the expected")
        );
    }

    @Test
    @DisplayName("Should save an Appointment when clinician has availability")
    void shouldNotSaveAnAppointmentWhenTimeSlotOverlaps() {
        // given:
        ClinicianId clinicianId = new ClinicianId("Bara");
        PatientId patientId = new PatientId("Sara");
        TimeSlot timeSlot = Slot.from(ZonedDateTime.now(), Duration.ofHours(1));
        Appointment request = new Appointment(clinicianId, patientId, timeSlot);

        // and:
        Appointment firstAppointment = classUnderTest.createAppointment(request);
        Assertions.assertNotNull(firstAppointment);

        // when:
        PatientId anotherPatientId = new PatientId("Bruno");
        IllegalStateException validation = Assertions.assertThrows(IllegalStateException.class,
                () -> classUnderTest.createAppointment(request));

        // then:
        Assertions.assertEquals("Time slot overlap other appointment", validation.getMessage());
    }
}
