package br.com.ead.home.models.api;

import br.com.ead.home.models.OverlapPossibility;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public interface TimeSlot extends Comparable<TimeSlot>, Cloneable, Serializable {

  ZonedDateTime start();
  ZonedDateTime end();

  <T extends TimeSlot> T of(ZonedDateTime start, ZonedDateTime end);

  @Override
  default int compareTo(TimeSlot other) {
    return Comparator.comparing(TimeSlot::start)
        .thenComparing(TimeSlot::end)
        .compare(this, other);
  }

  default Duration length() {
    return Duration.between(start(), end());
  }

  /**
   *
   * <p>A time slot contains a moment in time:
   *  if that moment occurs after the time slot has started and before it has ended.<p/>
   *
   * <p>Example of:<p/>
   *
   * ```
   *    <p>  this:   |-------------------------------------| <p/>
   *    <p> other: &nbsp;&nbsp; [------------------]         <p/>
   *  ```
   * ```
   *    <p>  this:   |--------------------------| <p/>
   *    <p> other:   |------------------|         <p/>
   *  ```
   * ```
   *    <p>  this:   |--------------------------| <p/>
   *    <p> other:           |------------------|  <p/>
   *  ```
   * @param other The 'B' date to compare to the current date.
   * @return true if the given dateTime is between this
   */
  default boolean contains(TimeSlot other) {
    return (this.start().isBefore(other.start()) && this.end().isAfter(other.end()))
        || (this.start().equals(other.start()) && this.end().isAfter(other.end()))
        || (this.start().isBefore(other.end()) && this.end().isEqual(other.end()));
  }

  /**
   *
   * <p>A time slot starts within and ends After in time: <br/>
   *  if that moment occurs after this time slot has started and the other ends after this timeslot.<p/>
   *
   * <p>Example of:<p/>
   *
   * ```
   *  <p> this: &nbsp;&nbsp; |----------------------|    <p/>
   *  <p> other:   |------------------|                 <p/>
   *  ```
   *
   * @param other The 'other' date to compare to this current date.
   * @return true if the given dateTime starts within other and ends after it
   */
  default boolean startsWithinEndsAfter(TimeSlot other) {
    return this.start().isAfter(other.start())
        && this.start().isBefore(other.end())
        && this.end().isAfter(other.end())
        && other.end().isBefore(this.end())
        && other.start().isBefore(this.start());
  }

  /**
   *
   * <p>This time slot starts before and it ends within the other time slot: <br/>
   *  if that moment occurs before the other time slot has started and this ends before the other time slot ends.<p/>
   *
   * <p>Example of:<p/>
   *
   * ```
   *  <p>  this:     |------------------|                 <p/>
   *  <p> other: &nbsp;&nbsp; |----------------------|    <p/>
   *  ```
   * @param other The 'B' date to compare to the current date.
   * @return true if the given dateTime starts before other and ends within it
   */
  default boolean startsBeforeEndsWithin(TimeSlot other) {
    return this.start().isBefore(other.start())
        && this.end().isBefore(other.end())
        && this.end().isAfter(other.start())
        && other.start().isAfter(this.start())
        && other.end().isAfter(this.end());
  }

  /**
   *    Sum all the timeslots, by performing the A union B operation
   *
   *    <p>A + B:
   *    <p>A:          |------------------|        <p/>
   *    <p>B:                |------------------|  <p/>
   *    <p>Union:      |------------------------|  <p/>
   *
   *   <p>A + B:
   *   <p>A:          |-------------|             <p/>
   *   <p>B:                        |----------|  <p/>
   *   <p>Union:      |------------------------|  <p/>
   *
   * @param others list of others time slits to combine
   * @return Set of all time slots combined
   */
  default Set<TimeSlot> unionAll(Set<TimeSlot> others) {
    Set<TimeSlot> results = new TreeSet<>();
    results.add(this);
    for (TimeSlot other : others) {
      for (TimeSlot overlap : other.overlapsWith(results)) {
        other = other.sum(overlap);
        results.remove(overlap);
      }
      results.add(other);
    }
    return results;
  }

  /**
   * Verify if this time slot overlaps the other
   * @param other to verify the collision
   * @return true if both time slots overlap
   */
  default boolean overlaps(TimeSlot other) {
    return !this.checkOverlap(other).equals(OverlapPossibility.NO_OVERLAP);
  }

  default Set<TimeSlot> overlapsWith(Set<TimeSlot> others) {
    return others.stream()
                 .filter(other -> other.overlaps(this))
                 .collect(Collectors.toCollection(TreeSet::new));
  }

  default TimeSlot sum(TimeSlot other) {
    switch (this.checkOverlap(other)) {
      case IS_CONTAINED:
        return other;
      case STARTS_BEFORE_ENDS_WITHIN:
        return this.of(this.start(), other.end());
      case STARTS_WITHIN_ENDS_AFTER:
        return this.of(other.start(), this.end());
      default:
        return this;
    }
  }

  /**
   *    Intersect the timeslots, by performing the A disjunction of B operation
   *
   *   <p/>This - Other (A - B):
   *   <p>  this:          |------------------|       <p/>
   *   <p> other:                 |------------------|<p/>
   *   <p> Subtraction:    |------|                   <p/>
   *
   *   <p/>This - Other (A - B):
   *   <p>  this:          |------------------|       <p/>
   *   <p> other:                 |-----------|       <p/>
   *   <p> Subtraction:    |------|                   <p/>
   *
   *   <p/>This - Other (A - B):
   *   <p>  this:          |----------|                   <p/>
   *   <p> other:                          |-----------|  <p/>
   *   <p> Subtraction:    |----------|                   <p/>
   *
   * @param other other time slits to subtract from this
   * @return Set of all subtracted result
   */
  default Set<TimeSlot> subtract(TimeSlot other) {
    Set<TimeSlot> results = new TreeSet<>();
    OverlapPossibility possibility = this.checkOverlap(other);
    switch (possibility) {
      case CONTAINS:
        if(this.start().equals(other.start())) {
          results.add(this.of(other.end(), this.end()));
          break;
        }
        if(this.end().equals(other.end())) {
          results.add(this.of(this.start(), other.start()));
          break;
        }
        results.add(this.of(this.start(), other.start()));
        results.add(this.of(other.end(), this.end()));
        break;
      case STARTS_BEFORE_ENDS_WITHIN:
        results.add(this.of(this.start(), other.start()));
        break;
      case STARTS_WITHIN_ENDS_AFTER:
        results.add(this.of(other.end(), this.end()));
        break;
      default:
        results.add(this);
        break;
    }
    return results;
  }


  default OverlapPossibility checkOverlap(TimeSlot other) {
    if (this.equals(other)) {
      return OverlapPossibility.EQUALS;
    }
    if (this.contains(other)) {
      return OverlapPossibility.CONTAINS;
    }
    if (other.contains(this)) {
      return OverlapPossibility.IS_CONTAINED;
    }
    if (this.startsWithinEndsAfter(other)) {
      return OverlapPossibility.STARTS_WITHIN_ENDS_AFTER;
    }
    if (this.startsBeforeEndsWithin(other)) {
      return OverlapPossibility.STARTS_BEFORE_ENDS_WITHIN;
    }
    return OverlapPossibility.NO_OVERLAP;
  }
}
