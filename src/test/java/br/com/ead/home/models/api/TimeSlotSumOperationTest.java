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
        TimeSlot first = new Slot(time("09:00"), time("12:00"));
        TimeSlot second = new Slot(time("09:00"), time("12:00"));

        Set<TimeSlot> result = first.sum(second);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        TimeSlot expectedTimeSlot = new Slot(time("09:00"), time("12:00"));
        Assertions.assertNotNull(IterableUtils.find(result, expectedTimeSlot::equals));
    }

    @Test
    @DisplayName("Should return two time slots when summing non overlying time slots")
    void shouldReturnTwoTimeSlotsWhenSummingNonOverlapping() {
        TimeSlot first = new Slot(time("08:00"), time("09:00"));
        TimeSlot second = new Slot(time("10:00"), time("11:00"));

        Set<TimeSlot> result = first.sum(second);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
        Assertions.assertNotNull(IterableUtils.find(result, first::equals));
        Assertions.assertNotNull(IterableUtils.find(result, second::equals));
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
}
