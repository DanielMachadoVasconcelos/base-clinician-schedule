package br.com.ead.home.models.api;

import br.com.ead.home.models.Slot;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

class TimeSlotOperationsTest {

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

  public ZonedDateTime time(String time) {
    return time(LocalDate.now(), time);
  }

  public ZonedDateTime time(LocalDate date, String time) {
    LocalTime now = LocalTime.parse(time);
    return ZonedDateTime.of(date, now, ZoneOffset.UTC);
  }
}
