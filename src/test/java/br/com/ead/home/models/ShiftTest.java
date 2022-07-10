package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static helpers.TimeSlotHelper.time;

class ShiftTest {

    @Test
    @DisplayName("Should not fail when creating a shift without time slots")
    void shouldNotFailWhenCreatingShiftsWithoutTimeSlots() {
        Assertions.assertAll(
            () -> Assertions.assertNotNull(new Shift(null).timeSlots(), "Must accept null value"),
            () -> Assertions.assertNotNull(new Shift(Set.of()).timeSlots(), "Must accept empty values")
        );
    }

    @Test
    @DisplayName("Should remove all availability when shift is fully booked")
    void shouldHasNoAvailabilityWhenShiftIsFullyBooked() {

        //given: an 8 hours shift
        Shift shifts = Shift.of(time("08:00"), time("17:00"));

        //and: the shift has only one meeting booked of one hour
        TimeSlot meeting = new Slot(time("08:00"), time("17:00"));

        //when: removing the booked time slots
        Shift availableSlots = shifts.subtract(meeting);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(availableSlots, "Must not return null"),
                () -> Assertions.assertNotNull(availableSlots.timeSlots(), "Must not return time slot list as null"),
                () -> Assertions.assertTrue(availableSlots.timeSlots().isEmpty(), "Must not return any bookable availability")
        );
    }

    @Test
    @DisplayName("Should find all available bookable slots when the shift is not fully booked")
    void shouldFindAllAvailableBookableSlotsWhenShiftIsNotFull() {

        //given: an 8 hours shift
        Shift shifts = Shift.of(time("08:00"), time("17:00"));

        //and: the shift has only one meeting booked of one hour
        TimeSlot meeting = new Slot(time("08:00"), time("09:00"));

        //when: removing the booked time slots
        Shift availableSlots = shifts.subtract(meeting);

        //then: there should be available bookable slots
        TimeSlot expectedBookableAvailability = new Slot(time("09:00"), time("17:00"));
        Assertions.assertAll(
            () -> Assertions.assertNotNull(availableSlots, "Must not return null"),
            () -> Assertions.assertNotNull(availableSlots.timeSlots(), "Must not return time slot list as null"),
            () -> Assertions.assertFalse(availableSlots.timeSlots().isEmpty(), "Must return some bookable availability"),
            () -> Assertions.assertEquals(1, availableSlots.timeSlots().size(), "Must return one bookable availability"),
            () -> Assertions.assertNotNull(IterableUtils.find(availableSlots.timeSlots(), expectedBookableAvailability::equals), "Must have the bookable availability from 09:00 to 17:00")
        );
    }

    @Test
    @DisplayName("Should return the bookable availabilities when removing all booked meetings")
    void shouldReturnTheAvailableTimeSlotsWhenRemovingAllBookedMeetings() {

        //given: an 8 hours shift
        Shift shifts = Shift.of(time("08:00"), time("09:00"));

        //and: the shift has two booked meetings
        TimeSlot firstMeeting = new Slot(time("08:00"), time("08:15"));
        TimeSlot secondMeeting = new Slot(time("08:45"), time("09:00"));

        //when: getting the bookable availability
        Shift availableSlots = shifts.subtractAll(firstMeeting, secondMeeting);

        //then: there should be available bookable slots
        TimeSlot expectedBookableAvailability = new Slot(time("08:15"), time("08:45"));
        Assertions.assertAll(
            () -> Assertions.assertNotNull(availableSlots, "Must not return null"),
            () -> Assertions.assertNotNull(availableSlots.timeSlots(), "Must not return time slot list as null"),
            () -> Assertions.assertFalse(availableSlots.timeSlots().isEmpty(), "Must return some bookable availability"),
            () -> Assertions.assertEquals(1, availableSlots.timeSlots().size(), "Must return one bookable availability"),
            () -> Assertions.assertNotNull(IterableUtils.find(availableSlots.timeSlots(), expectedBookableAvailability::equals), "Must have the bookable availability from 09:00 to 17:00")
        );
    }

    @Test
    @DisplayName("Should return the bookable availabilities when duplicated booking meetings")
    void shouldIgnoreDuplicatedBookingWhenSubtractingTheAvailability() {

        //given: an 8 hours shift
        Shift shifts = Shift.of(time("08:00"), time("12:00"));

        //and: the shift has two booked meetings at the same time
        TimeSlot firstMeeting = new Slot(time("09:00"), time("10:00"));
        TimeSlot secondMeeting = new Slot(time("09:00"), time("10:00"));

        //when: getting the bookable availability
        Shift availableSlots = shifts.subtractAll(firstMeeting, secondMeeting);

        //then: there should be available bookable slots
        TimeSlot expectedBookableAvailability = new Slot(time("08:00"), time("09:00"));
        Assertions.assertAll(
            () -> Assertions.assertNotNull(availableSlots, "Must not return null"),
            () -> Assertions.assertNotNull(availableSlots.timeSlots(), "Must not return time slot list as null"),
            () -> Assertions.assertFalse(availableSlots.timeSlots().isEmpty(), "Must return some bookable availability"),
            () -> Assertions.assertEquals(2, availableSlots.timeSlots().size(), "Must return one bookable availability"),
            () -> Assertions.assertNotNull(IterableUtils.find(availableSlots.timeSlots(), expectedBookableAvailability::equals), "Must have the bookable availability from 09:00 to 17:00")
        );
    }
}