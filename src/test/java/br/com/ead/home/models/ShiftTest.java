package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

class ShiftTest {

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

    public ZonedDateTime time(String time) {
        LocalTime now = LocalTime.parse(time);
        LocalDate today = LocalDate.now();
        return ZonedDateTime.of(today, now, ZoneOffset.UTC);
    }
}