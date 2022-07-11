package br.com.ead.home.models.api;

import br.com.ead.home.models.Slot;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static helpers.TimeSlotHelper.time;

class TimeSlotSumOperationTest {

    @Test
    @DisplayName("Should return two time slots when summing non overlying time slots")
    void shouldReturnOneTimeSlotWhenBothTimeSlotsAreEquals() {
        // given: one time slots ends when the other starts
        TimeSlot first = new Slot(time("09:00"), time("12:00"));
        TimeSlot second = new Slot(time("09:00"), time("12:00"));

        // when: summing both time slots
        Set<TimeSlot> result = first.sum(second);

        // then: there is a single merged result
        TimeSlot expectedTimeSlot = new Slot(time("09:00"), time("12:00"));
        Assertions.assertAll(
                () -> Assertions.assertFalse(result.isEmpty(), "Must not return null"),
                () -> Assertions.assertEquals(1, result.size(), "Must return only one time slot"),
                () -> Assertions.assertNotNull(IterableUtils.find(result, expectedTimeSlot::equals), "Must return the 09:00 to 12:00 time slot"));
    }

    @Test
    @DisplayName("Should return two time slots when summing non overlying time slots")
    void shouldReturnTwoTimeSlotsWhenSummingNonOverlapping() {
        // given: the first time slot start before the second and end ends within
        TimeSlot first = new Slot(time("08:00"), time("09:00"));
        TimeSlot second = new Slot(time("10:00"), time("11:00"));

        // when: summing both time slots
        Set<TimeSlot> result = first.sum(second);

        // then: there is a single merged result
        Assertions.assertAll(
                () -> Assertions.assertFalse(result.isEmpty(), "Must not return null"),
                () -> Assertions.assertEquals(2, result.size(), "Must return only two time slot"),
                () -> Assertions.assertNotNull(IterableUtils.find(result, first::equals), "Must return the 08:00 to 09:00 time slot"),
                () -> Assertions.assertNotNull(IterableUtils.find(result, second::equals), "Must return the 10:00 to 11:00 time slot"));
    }

    @Test
    @DisplayName("Should merge both time slots when summing the first that contains the second")
    void shouldReturnOneTimeSlotsWhenSummingFirstThatContainsTheSecond() {
        TimeSlot first = new Slot(time("08:00"), time("09:00"));
        TimeSlot second = new Slot(time("08:30"), time("08:45"));

        Set<TimeSlot> result = first.sum(second);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertNotNull(IterableUtils.find(result, first::equals));
    }

    @Test
    @DisplayName("Should merge both time slots when summing first that is contained in the second")
    void shouldReturnOneTimeSlotsWhenSummingNonOverlapping() {
        TimeSlot first = new Slot(time("08:30"), time("08:45"));
        TimeSlot second = new Slot(time("08:00"), time("09:00"));

        Set<TimeSlot> result = first.sum(second);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertNotNull(IterableUtils.find(result, second::equals));
    }

    @Test
    @DisplayName("Should return two time slots when summing non overlying time slots")
    void shouldMergeNonOverlyingTimeSlotsWhenTheFirstStartWhenTheSecondEnds() {
        TimeSlot first = new Slot(time("08:00"), time("09:00"));
        TimeSlot second = new Slot(time("09:00"), time("11:00"));

        Set<TimeSlot> result = first.sum(second);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        TimeSlot expectedTimeSlot = new Slot(time("08:00"), time("11:00"));
        Assertions.assertNotNull(IterableUtils.find(result, expectedTimeSlot::equals));
    }

    @Test
    @DisplayName("Should return two time slots when summing non overlying time slots")
    void shouldMergeOverlyingTimeSlotsWhenFirstStartBeforeAndEndsWithinTheSecond() {
        TimeSlot first = new Slot(time("08:00"), time("10:00"));
        TimeSlot second = new Slot(time("09:00"), time("11:00"));

        Set<TimeSlot> result = first.sum(second);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        TimeSlot expectedTimeSlot = new Slot(time("08:00"), time("11:00"));
        Assertions.assertNotNull(IterableUtils.find(result, expectedTimeSlot::equals));
    }

    @Test
    @DisplayName("Should return two time slots when summing non overlying time slots")
    void shouldMergeOverlyingTimeSlotsWhenFirstStartWithinAndEndsAfterTheSecond() {
        TimeSlot first = new Slot(time("09:00"), time("12:00"));
        TimeSlot second = new Slot(time("08:00"), time("11:00"));

        Set<TimeSlot> result = first.sum(second);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        TimeSlot expectedTimeSlot = new Slot(time("08:00"), time("12:00"));
        Assertions.assertNotNull(IterableUtils.find(result, expectedTimeSlot::equals));
    }

    @Test
    @DisplayName("Should return two time slots when summing non overlying time slots")
    void shouldSumAllTimeSlotsWhenGivenAListOfBookings() {
        // given: there are multiple time slots
        TimeSlot first = new Slot(time("09:00"), time("10:00"));
        TimeSlot second = new Slot(time("10:00"), time("11:00"));
        TimeSlot third = new Slot(time("11:00"), time("12:00"));
        TimeSlot fourth = new Slot(time("11:00"), time("12:00"));

        // and: the fifth time slot do not overlap
        TimeSlot fifth = new Slot(time("15:00"), time("18:00"));

        // when: summing all the time slots
        Set<TimeSlot> result = first.sumAll(second, third, fourth, fifth);

        // then: there is a sum of the first slots and a second non overlying
        TimeSlot expectedFirstTimeSlot = new Slot(time("09:00"), time("12:00"));
        TimeSlot expectedSecondTimeSlot = new Slot(time("15:00"), time("18:00"));
        Assertions.assertAll(
            () -> Assertions.assertFalse(result.isEmpty()),
            () -> Assertions.assertEquals(2, result.size()),
            () -> Assertions.assertNotNull(IterableUtils.find(result, expectedFirstTimeSlot::equals)),
            () -> Assertions.assertNotNull(IterableUtils.find(result, expectedSecondTimeSlot::equals))
        );
    }
}
