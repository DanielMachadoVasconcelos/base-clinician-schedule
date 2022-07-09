package br.com.ead.home.models.api;

import br.com.ead.home.models.OverlapPossibility;
import br.com.ead.home.models.Slot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    @DisplayName("Should assert No Overlap when time slots has same hour but in different days")
    void shouldAssertNoOverlapWhenTwoTimeSlotsAtSameHourButDifferentDays() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        TimeSlot first = new Slot(time(today, "08:00"), time(today, "09:00"));
        TimeSlot second = new Slot(time(tomorrow, "08:00"), time(tomorrow, "09:00"));
        Assertions.assertEquals(OverlapPossibility.NO_OVERLAP, first.checkOverlap(second));
    }

    @Test
    @DisplayName("Should assert No Overlap when second starts when first ends")
    void shouldAssertNoOverlapWhenFirstEndsWhenSecondStart() {
        TimeSlot first = new Slot(time("08:00"), time("12:00"));
        TimeSlot second = new Slot(time("12:00"), time("18:00"));
        Assertions.assertEquals(OverlapPossibility.NO_OVERLAP, first.checkOverlap(second));
    }

    @Test
    @DisplayName("Should assert No Overlap when time slots do not collide")
    void shouldAssertNoOverlapWhenTwoTimeSlotsDoNotCollide() {
        TimeSlot first = new Slot(time("08:00"), time("09:00"));
        TimeSlot second = new Slot(time("12:00"), time("13:00"));
        Assertions.assertEquals(OverlapPossibility.NO_OVERLAP, first.checkOverlap(second));
    }

    @Test
    @DisplayName("Should assert Equals when time slots start and ends at same time")
    void shouldAssertEqualsWhenTwoTimeSlotsStartsAndEndsAtSameTime() {
        TimeSlot first = new Slot(time("08:00"), time("09:00"));
        TimeSlot second = new Slot(time("08:00"), time("09:00"));
        Assertions.assertEquals(OverlapPossibility.EQUALS, first.checkOverlap(second));
    }

    @Test
    @DisplayName("Should assert Contains when second time slots is within the first timeslot")
    void shouldAssertContainsWhenSecondTimeSlotIsWithinFirstTimeSlot() {
        TimeSlot first = new Slot(time("08:00"), time("12:00"));
        TimeSlot second = new Slot(time("09:00"), time("11:00"));
        Assertions.assertEquals(OverlapPossibility.CONTAINS, first.checkOverlap(second));
    }


    @Test
    @DisplayName("Should assert Contains when second time slots start at same time but ends within")
    void shouldAssertContainsWhenSecondTimeSlotEndsAtSameTimeButStartWithin() {
        TimeSlot first = new Slot(time("10:00"), time("11:00"));
        TimeSlot second = new Slot(time("10:45"), time("11:00"));
        Assertions.assertEquals(OverlapPossibility.CONTAINS, first.checkOverlap(second));
    }

    @Test
    @DisplayName("Should assert Is Contained when first time slots start and ends within the second time slot")
    void shouldAssertContainsWhenOtherTimeSlotIsWithinTheTimeSlot() {
        TimeSlot first = new Slot(time("09:00"), time("10:00"));
        TimeSlot second = new Slot(time("08:00"), time("11:00"));
        Assertions.assertEquals(OverlapPossibility.IS_CONTAINED, first.checkOverlap(second));
    }

    @Test
    @DisplayName("Should assert Contains when second time slots start at same time but ends within")
    void shouldAssertContainsWhenSecondTimeSlotStartAtSameTimeButEndsWithin() {
        TimeSlot first = new Slot(time("10:00"), time("11:00"));
        TimeSlot second = new Slot(time("10:00"), time("10:15"));
        Assertions.assertEquals(OverlapPossibility.CONTAINS, first.checkOverlap(second));
    }

    @Test
    @DisplayName("Should assert Start Before Ends Withing when first time slots start before the second and ends within")
    void shouldAssertStartBeforeAndEndsWithinWhenFirstTimeSlotStartBeforeAndEndsWithinTheSecond() {
        TimeSlot first = new Slot(time("08:00"), time("10:00"));
        TimeSlot second = new Slot(time("09:00"), time("11:00"));
        Assertions.assertEquals(OverlapPossibility.STARTS_BEFORE_ENDS_WITHIN, first.checkOverlap(second));
    }

    @Test
    @DisplayName("Should assert Start Within Ends After when first time slots start within the second and ends after")
    void shouldAssertStartWithinAndEndsAfterWhenFirstTimeSlotStartWithinAndEndsAfterTheSecond() {
        TimeSlot first = new Slot(time("10:00"), time("12:00"));
        TimeSlot second = new Slot(time("09:00"), time("11:00"));
        Assertions.assertEquals(OverlapPossibility.STARTS_WITHIN_ENDS_AFTER, first.checkOverlap(second));
    }

    public ZonedDateTime time(String time) {
        return time(LocalDate.now(), time);
    }

    public ZonedDateTime time(LocalDate date, String time) {
        LocalTime now = LocalTime.parse(time);
        return ZonedDateTime.of(date, now, ZoneOffset.UTC);
    }
}