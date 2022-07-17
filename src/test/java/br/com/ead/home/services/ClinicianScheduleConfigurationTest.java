package br.com.ead.home.services;

import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Set;

import static helpers.TimeSlotHelper.time;

class ClinicianScheduleConfigurationTest {

    @Test
    @DisplayName("Should reject Doctor Schedule when meeting length is negative")
    void shouldNotAcceptDoctorShiftConfigurationWhenMeetingLengthNegative() {
        Assertions.assertThrowsExactly(IllegalStateException.class,
                () ->  new ClinicianScheduleConfiguration(
                        null,
                        Duration.ofHours(-1),
                        Duration.ofMinutes(15),
                        Duration.ofHours(4),
                        6L,
                        ZoneOffset.UTC)
        );
    }

    @Test
    @DisplayName("Should find all availability when requesting with Doctor Schedule Configuration")
    void shouldReturnTheDoctorConfigurationScheduleWhenFetchingTheBookableAvailabilities() {
        // setup: The doctor has a shift of 10 hours
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Slot shift = new Slot(time(tomorrow, "08:00"), time(tomorrow, "18:00"));

        // given: the doctor has a schedule configuration
        ClinicianScheduleConfiguration configuration = new ClinicianScheduleConfiguration.Builder()
                .setBufferBetweenMeetings(Duration.ofMinutes(15))
                .setMeetingLength(Duration.ofHours(1))
                .setOnlyMaximumOfFreeSlots(6L)
                .build();

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

    @Test
    @DisplayName("Should have no surprise meetings when configuring next meeting only after some duration")
    void shouldSkipTimeSlotsWhenDoctorConfiguredNoSurpriseMeetings() {
        // setup: The doctor has a shift of 10 hours
        Slot shift = new Slot(time("08:00"), time("18:00"));
        Clock clock = Clock.fixed(time("08:00").toInstant(), ZoneOffset.UTC);

        // and: the doctor configure no surprise meeting in next 4 hours
        Duration expectedNextMeetingOnlyIn = Duration.ofHours(4);

        // given: the doctor has a schedule configuration
        ClinicianScheduleConfiguration configuration = new ClinicianScheduleConfiguration.Builder()
                .setBufferBetweenMeetings(Duration.ofMinutes(15))
                .setNextMeetingOnlyIn(expectedNextMeetingOnlyIn)
                .setMeetingLength(Duration.ofHours(1))
                .setOnlyMaximumOfFreeSlots(6L)
                .setClock(clock)
                .build();

        // when: using the configuration to split the shift in time slots
        Set<TimeSlot> expectedResult = configuration.split(shift);

        // then: the expected result should be
        TimeSlot expectedFirstSlot = new Slot(time("12:00"), time("13:00"));
        Assertions.assertAll(
                () -> Assertions.assertNotNull(expectedResult, "Must not return null"),
                () -> Assertions.assertEquals(5, expectedResult.size(), "Must return at maximum 5 slots"),
                () -> Assertions.assertNotNull(IterableUtils.find(expectedResult, expectedFirstSlot::equals), "Must have the next time slot from 12:00 to 13:00")
        );
    }
}