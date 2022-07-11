package br.com.ead.home.services;

import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import com.google.common.collect.Iterables;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Set;

import static helpers.TimeSlotHelper.time;

class DoctorScheduleConfigurationTest {

    @Test
    @DisplayName("Should find all availability when requesting with Doctor Schedule Configuration")
    void shouldReturnTheDoctorConfigurationScheduleWhenFetchingTheBookableAvailabilities() {
        // setup: The doctor has a shift of 10 hours
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Slot shift = new Slot(time(tomorrow, "08:00"), time(tomorrow, "18:00"));

        // given: the doctor has a schedule configuration
        DoctorScheduleConfiguration configuration = new DoctorScheduleConfiguration(
                Duration.ofHours(1),
                Duration.ofMinutes(15),
                Duration.ofHours(4),
                6L,
                ZoneOffset.UTC);

        // when: using the configuration to split the shift in time slots
        Set<TimeSlot> expectedResult = configuration.split(shift);

        // then: the expected result should be
        TimeSlot expectedFirstSlot = new Slot(time(tomorrow, "08:00"), time(tomorrow, "09:00"));
        TimeSlot expectedLastSlot = new Slot(time(tomorrow, "14:15"), time(tomorrow, "15:15"));
        Assertions.assertAll(
            () -> Assertions.assertNotNull(expectedResult, "Must not return null"),
            () -> Assertions.assertEquals(6, expectedResult.size(), "Must return at maximum 6 slots"),
            () -> Assertions.assertNotNull(IterableUtils.find(expectedResult, expectedFirstSlot::equals)),
            () -> Assertions.assertNotNull(IterableUtils.find(expectedResult, expectedLastSlot::equals))
        );
    }
}