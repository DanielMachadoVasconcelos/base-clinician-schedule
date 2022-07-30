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
        ClinicianId clinicianId = new ClinicianId("Thomas");
        PatientId patientId = new PatientId("Sara");
        TimeSlot timeSlot = Slot.from(ZonedDateTime.now(), Duration.ofHours(1));

        // when:
        Appointment appointment = classUnderTest.createAppointment(clinicianId, patientId, timeSlot);

        // then:
        Assertions.assertAll(
            () -> Assertions.assertNotNull(appointment, "Appointment response is mandatory"),
            () -> Assertions.assertEquals(clinicianId, appointment.clinicianId(), "Clinician is the expected")
        );
    }
}
