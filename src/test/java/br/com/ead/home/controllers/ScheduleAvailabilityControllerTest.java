package br.com.ead.home.controllers;

import br.com.ead.home.AbstractIntegrationTest;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Set;

class ScheduleAvailabilityControllerTest extends AbstractIntegrationTest {

    private ScheduleAvailabilityController classUnderTest;

    @BeforeEach
    void setUp() {
        String jndi = resolver.namespace(StageType.UNIT_TEST, PartitionType.SWEDEN, ScheduleAvailabilityController.class.getName());
        classUnderTest = (ScheduleAvailabilityController) initialContext.lookup(jndi);
    }

    @Test
    @DisplayName("Should find all bookable availabilities when requesting by clinicianId")
    void shouldReturnAllBookableAvailabilityWhenProvidedClinicianId() {

        // given: the expected clinicianId
        ClinicianId expectedClinicianId = new ClinicianId("Thomas");
        ZonedDateTime today = ZonedDateTime.now(clockProvider.currentSystemClock());

        // when: getting the clinician bookable availability
        Set<TimeSlot> availabilities =
                classUnderTest.getBookableAvailabilities(expectedClinicianId, today, today.plusDays(1));

        // then: some availability is found
        Assertions.assertAll(
            () -> Assertions.assertNotNull(availabilities, "Must not return null"),
            () -> Assertions.assertFalse(availabilities.isEmpty(), "Must return some availability")
        );
    }

    @Test
    @DisplayName("Should find no bookable availabilities when requesting with unknown clinicianId")
    void shouldFindNoBookableAvailabilityWhenRequestingWithUnknownClinicianId() {

        // given: the expected clinicianId
        ClinicianId expectedClinicianId = new ClinicianId("unknown");
        ZonedDateTime today = ZonedDateTime.now(clockProvider.currentSystemClock());

        // when: getting the clinician bookable availability
        Set<TimeSlot> availabilities =
                classUnderTest.getBookableAvailabilities(expectedClinicianId, today, today.plusDays(1));

        // then: some availability is found
        Assertions.assertAll(
                () -> Assertions.assertNotNull(availabilities, "Must not return null"),
                () -> Assertions.assertTrue(availabilities.isEmpty(), "Must return no availability")
        );
    }
}