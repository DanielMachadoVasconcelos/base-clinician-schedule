package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static helpers.TimeSlotHelper.time;

class ShiftTest {

    @Test
    @DisplayName("Should fail when creating a shift without time slots or clinician")
    void shouldNotFailWhenCreatingShiftsWithoutTimeSlots() {
        Assertions.assertAll(
                () -> Assertions.assertThrowsExactly(NullPointerException.class, () -> new Shift(null, null)),
                () -> Assertions.assertThrowsExactly(NullPointerException.class, () -> new Shift(ClinicianId.from(UUID.randomUUID()), null)));
    }

    @Test
    @DisplayName("Should remove all availability when shift is fully booked")
    void shouldHasNoAvailabilityWhenShiftIsFullyBooked() {

        //given: an 8 hours shift
        Shift shifts = new Shift(ClinicianId.from(UUID.randomUUID()), new Slot(time("08:00"), time("17:00")));

        //and: the shift has only one meeting booked of one hour
        TimeSlot meeting = new Slot(time("08:00"), time("17:00"));

        //when: removing the booked time slots
        Set<TimeSlot> availableSlots = shifts.subtract(meeting);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(availableSlots, "Must not return null"),
                () -> Assertions.assertTrue(availableSlots.isEmpty(), "Must return no available time slot")
        );
    }

    @Test
    @DisplayName("Should find all available bookable slots when the shift is not fully booked")
    void shouldFindAllAvailableBookableSlotsWhenShiftIsNotFull() {

        //given: an 8 hours shift
        Shift shifts = new Shift(ClinicianId.from(UUID.randomUUID()), new Slot(time("08:00"), time("17:00")));

        //and: the shift has only one meeting booked of one hour
        TimeSlot meeting = new Slot(time("08:00"), time("09:00"));

        //when: removing the booked time slots
        Set<TimeSlot> availableSlots = shifts.subtract(meeting);

        //then: there should be available bookable slots
        TimeSlot expectedBookableAvailability = new Slot(time("09:00"), time("17:00"));
        Assertions.assertAll(
            () -> Assertions.assertNotNull(availableSlots, "Must not return null"),
            () -> Assertions.assertFalse(availableSlots.isEmpty(), "Must return some bookable availability"),
            () -> Assertions.assertEquals(1, availableSlots.size(), "Must return one bookable availability"),
            () -> Assertions.assertNotNull(IterableUtils.find(availableSlots, expectedBookableAvailability::equals), "Must have the bookable availability from 09:00 to 17:00")
        );
    }

    @Test
    @DisplayName("Should return the bookable availabilities when removing all booked meetings")
    void shouldReturnTheAvailableTimeSlotsWhenRemovingAllBookedMeetings() {

        //given: an 1 hours shift
        Shift shifts = new Shift(ClinicianId.from(UUID.randomUUID()), new Slot(time("08:00"), time("09:00")));

        //and: the shift has two booked meetings
        TimeSlot firstMeeting = new Slot(time("08:00"), time("08:15"));
        TimeSlot secondMeeting = new Slot(time("08:45"), time("09:00"));

        //when: getting the bookable availability
        Set<TimeSlot> availableSlots = shifts.subtractAll(Set.of(firstMeeting, secondMeeting));

        //then: there should be available bookable slots
        TimeSlot expectedBookableAvailability = new Slot(time("08:15"), time("08:45"));
        Assertions.assertAll(
            () -> Assertions.assertNotNull(availableSlots, "Must not return null"),
            () -> Assertions.assertFalse(availableSlots.isEmpty(), "Must return some bookable availability"),
            () -> Assertions.assertEquals(1, availableSlots.size(), "Must return one bookable availability"),
            () -> Assertions.assertNotNull(IterableUtils.find(availableSlots, expectedBookableAvailability::equals), "Must have the bookable availability from 09:00 to 17:00")
        );
    }

    @Test
    @DisplayName("Should return all bookable availabilities when non subsequent meetings")
    void shouldReturnTheAvailableTimeSlotsWhenRemovingAllNonConnectedBookedMeetings() {

        //given: an 10 hours shift
        Shift shifts = new Shift(ClinicianId.from(UUID.randomUUID()), new Slot(time("08:00"), time("18:00")));

        //and: the shift has two booked meetings
        TimeSlot firstMeeting = new Slot(time("09:00"), time("10:00"));
        TimeSlot secondMeeting = new Slot(time("12:00"), time("13:00"));

        //when: getting the bookable availability
        Set<TimeSlot> availableSlots = shifts.subtractAll(Set.of(firstMeeting, secondMeeting));

        //then: there should be available bookable slots
        Assertions.assertAll(
            () -> Assertions.assertNotNull(availableSlots, "Must not return null"),
            () -> Assertions.assertFalse(availableSlots.isEmpty(), "Must return some bookable availability"),
            () -> Assertions.assertEquals(3, availableSlots.size(), "Must return one bookable availability"),
            () -> Assertions.assertNotNull(IterableUtils.find(availableSlots, new Slot(time("08:00"), time("09:00"))::equals), "Must have the bookable availability from 08:00 to 09:00"),
            () -> Assertions.assertNotNull(IterableUtils.find(availableSlots, new Slot(time("10:00"), time("12:00"))::equals), "Must have the bookable availability from 10:00 to 12:00"),
            () -> Assertions.assertNotNull(IterableUtils.find(availableSlots, new Slot(time("13:00"), time("18:00"))::equals), "Must have the bookable availability from 13:00 to 18:00")
        );
    }
}