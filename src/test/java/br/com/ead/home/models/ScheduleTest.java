package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;

import static helpers.TimeSlotHelper.time;

class ScheduleTest {

    @Test
    void shouldGetAllBookableAvailabilityWhenClinicianHasNoBookings() {
        // given: clinician has a one day of shift
        ClinicianId expectedClinicianId = new ClinicianId("Harry");
        TimeSlot singleDayShift =  Slot.from(time("08:00"), Duration.ofHours(8));
        Shift shift = new Shift(expectedClinicianId, singleDayShift);
        Schedule schedule = new Schedule(expectedClinicianId, Set.of(shift), Set.of());

        // when: getting the bookable availability
        Set<TimeSlot> available = schedule.getBookableAvailability(
            SchedulePreferences.builder()
                    .meetingLength(Duration.ofHours(1))
                    .build());

        // then: the is full day of availability
        Assertions.assertAll(
            () -> Assertions.assertNotNull(available, "Must not return null"),
            () -> Assertions.assertFalse(available.isEmpty(), "Must not be empty"),
            () -> Assertions.assertEquals(8, available.size(), "Must return one time slot")
        );
    }
}