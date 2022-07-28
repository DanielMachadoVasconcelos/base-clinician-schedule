package br.com.ead.home.controllers;

import br.com.ead.home.Application;
import br.com.ead.home.configurations.ClockProvider;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.ZonedDateTime;
import java.util.Set;

class ScheduleAvailabilityControllerTest {

    private ScheduleAvailabilityController classUnderTest;
    private ClockProvider clockProvider;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class.getPackageName());
        classUnderTest = applicationContext.getBean(ScheduleAvailabilityController.class);
        clockProvider = applicationContext.getBean(ClockProvider.class);
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