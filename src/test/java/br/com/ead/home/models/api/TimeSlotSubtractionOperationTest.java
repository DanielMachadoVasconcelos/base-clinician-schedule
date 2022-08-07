package br.com.ead.home.models.api;

import br.com.ead.home.models.Slot;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static helpers.TimeSlotHelper.time;

class TimeSlotSubtractionOperationTest {

  @Test
  @DisplayName("Should return the same time slots when intersecting non overlapping time slots")
  void shouldReturnBothSlotsWhenSubtractingTwoNonOverlappingSlots() {
    TimeSlot first = new Slot(time("08:00"), time("09:00"));
    TimeSlot second = new Slot(time("09:00"), time("10:00"));

    Set<TimeSlot> result = first.subtract(second);

    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(1, result.size());
    Assertions.assertNotNull(IterableUtils.find(result, first::equals));

  }

  @Test
  @DisplayName("Should return one time slots when other times slot is in the beginning of the first")
  void shouldReturnOneTimeSlotWhenOtherIsContainedAndStartAtSameTime() {
    TimeSlot first = new Slot(time("08:00"), time("09:00"));
    TimeSlot second = new Slot(time("08:00"), time("08:15"));

    Set<TimeSlot> result = first.subtract(second);

    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(1, result.size());

    TimeSlot expectedTimeSlot = new Slot(time("08:15"), time("09:00"));
    Assertions.assertNotNull(IterableUtils.find(result, expectedTimeSlot::equals));

  }

  @Test
  @DisplayName("Should return two time slots when other is in between the start and end of the first")
  void shouldReturnTwoTimeSlotWhenOtherIsInBetweenTheTimeSlot() {
    TimeSlot first = new Slot(time("08:00"), time("10:00"));
    TimeSlot second = new Slot(time("08:45"), time("09:15"));

    Set<TimeSlot> result = first.subtract(second);

    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(2, result.size());

    TimeSlot expectedFirstTimeSlot = new Slot(time("08:00"), time("08:45"));
    TimeSlot expectedSecondTimeSlot = new Slot(time("09:15"), time("10:00"));
    Assertions.assertNotNull(IterableUtils.find(result, expectedFirstTimeSlot::equals));
    Assertions.assertNotNull(IterableUtils.find(result, expectedSecondTimeSlot::equals));
  }

  @Test
  @DisplayName("Should return no time slots when this is contained in the other")
  void shouldReturnTwsTimeSlotWhenFirstIsInBetweenTheSecondTimeSlot() {
    TimeSlot first = new Slot(time("08:45"), time("09:15"));
    TimeSlot second = new Slot(time("08:00"), time("10:00"));

    Set<TimeSlot> result = first.subtract(second);

    Assertions.assertTrue(result.isEmpty());
  }
}
