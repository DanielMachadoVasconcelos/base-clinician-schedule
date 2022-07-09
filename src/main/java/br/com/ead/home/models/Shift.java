package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Shift(Set<TimeSlot> timeSlots) {

  public Shift(Set<TimeSlot> timeSlots) {
    this.timeSlots = new TreeSet<>(CollectionUtils.emptyIfNull(timeSlots));
  }

  public static Shift of(Set<TimeSlot> timeSlots) {
    return new Shift(timeSlots);
  }

  public static Shift of(ZonedDateTime start, ZonedDateTime end) {
    TimeSlot timeSlot = new Slot(start, end);
    return new Shift(Set.of(timeSlot));
  }

  public Set<TimeSlot> getTimeSlots() {
    return timeSlots;
  }

  public Shift add(TimeSlot timeSlot) {
    return new Shift(timeSlot.unionAll(this.timeSlots));
  }

  public Shift addAll(Set<TimeSlot> others) {
    return new Shift(SetUtils.union(this.timeSlots, others));
  }

  public Shift subtract(TimeSlot other) {
    return new Shift(this.timeSlots
            .stream()
            .map(timeSlot -> timeSlot.subtract(other))
            .flatMap(Set::stream)
            .collect(Collectors.toCollection(TreeSet::new)));

  }

  public Shift subtractAll(TimeSlot ...other) {

    if(other == null || other.length == 0) {
      return this;
    }

    Shift current = new Shift(this.timeSlots);
    for (TimeSlot slot: other){
      current = current.subtract(slot);
    }
    return current;
  }
}
