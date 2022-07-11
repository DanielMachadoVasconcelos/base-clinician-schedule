package br.com.ead.home.models.api;

import br.com.ead.home.models.OverlapPossibility;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
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

    /**
     * The total Duration of this time slot
     *
     * @return Duration of this time slot
     */
    default Duration length() {
        return Duration.between(start(), end());
    }

    /**
     * <p>A time slot contains a moment in time:
     * if that moment occurs after the time slot has started and before it has ended.<p/>
     *
     * <p>Example of:<p/>
     * <p>
     * ```
     * <p>  this:   |-------------------------------------| <p/>
     * <p> other: &nbsp;&nbsp; [------------------]         <p/>
     * ```
     * ```
     * <p>  this:   |--------------------------| <p/>
     * <p> other:   |------------------|         <p/>
     * ```
     * ```
     * <p>  this:   |--------------------------| <p/>
     * <p> other:           |------------------|  <p/>
     * ```
     *
     * @param other The 'B' date to compare to the current date.
     * @return true if the given dateTime is between this
     */
    default boolean contains(TimeSlot other) {
        return (this.start().isBefore(other.start()) && this.end().isAfter(other.end())) || (this.start().equals(other.start()) && this.end().isAfter(other.end())) || (this.start().isBefore(other.end()) && this.end().isEqual(other.end()));
    }

    /**
     * <p>A time slot starts within and ends After in time: <br/>
     * if that moment occurs after this time slot has started and the other ends after this timeslot.<p/>
     *
     * <p>Example of:<p/>
     * <p>
     * ```
     * <p> this: &nbsp;&nbsp; |----------------------|    <p/>
     * <p> other:   |------------------|                 <p/>
     * ```
     *
     * @param other The 'other' date to compare to this current date.
     * @return true if the given dateTime starts within other and ends after it
     */
    default boolean startsWithinEndsAfter(TimeSlot other) {
        return this.start().isAfter(other.start()) && this.start().isBefore(other.end()) && this.end().isAfter(other.end()) && other.end().isBefore(this.end()) && other.start().isBefore(this.start());
    }

    /**
     * <p>This time slot starts before and it ends within the other time slot: <br/>
     * if that moment occurs before the other time slot has started and this ends before the other time slot ends.<p/>
     *
     * <p>Example of:<p/>
     * <p>
     * ```
     * <p>  this:     |------------------|                 <p/>
     * <p> other: &nbsp;&nbsp; |----------------------|    <p/>
     * ```
     *
     * @param other The 'B' date to compare to the current date.
     * @return true if the given dateTime starts before other and ends within it
     */
    default boolean startsBeforeEndsWithin(TimeSlot other) {
        return this.start().isBefore(other.start()) && this.end().isBefore(other.end()) && this.end().isAfter(other.start()) && other.start().isAfter(this.start()) && other.end().isAfter(this.end());
    }

    /**
     * Verify if this time slot overlaps the other
     *
     * @param other to verify the collision
     * @return true if both time slots overlap
     */
    default boolean overlaps(TimeSlot other) {
        return !this.checkOverlap(other).equals(OverlapPossibility.NO_OVERLAP);
    }

    /**
     * Verify if this time slot overlaps with the list of the given othes time slots
     *
     * @param others List of time slots to check against to
     * @return The list of time slots that overlap
     */
    default Set<TimeSlot> overlapsWith(Set<TimeSlot> others) {
        return others.stream()
                .filter(this::overlaps)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Sum all the timeslots, by performing the A union B operation
     *
     * <p>A + B:
     * <p>A:          |------------------|        <p/>
     * <p>B:                |------------------|  <p/>
     * <p>Union:      |------------------------|  <p/>
     *
     * <p>A + B:
     * <p>A:          |-------------|             <p/>
     * <p>B:                        |----------|  <p/>
     * <p>Union:      |------------------------|  <p/>
     *
     * @param others list of others time slits to combine
     * @return Set of all time slots combined
     */
    default Set<TimeSlot> sumAll(TimeSlot... others) {
        if (others == null || others.length == 0) {
            return Set.of(this);
        }
        return this.sumAll(Sets.newHashSet(others));
    }

    default Set<TimeSlot> sumAll(Set<TimeSlot> others) {
        if (others == null || others.isEmpty()) {
            return Set.of(this);
        }

        Set<TimeSlot> identity = Sets.newHashSet(this);
        BiFunction<Set<TimeSlot>, TimeSlot, Set<TimeSlot>> mapper = (acc, next) -> acc.stream().map(timeSlot -> timeSlot.sum(next)).flatMap(Set::stream).collect(Collectors.toSet());
        BinaryOperator<Set<TimeSlot>> combiner = (acc, next) ->  Sets.newHashSet(Iterables.concat(acc, next));

        return others.stream()
                .sorted(TimeSlot::compareTo)
                .reduce(identity, mapper, combiner);
    }

    default TimeSlot merge(TimeSlot overlap){
        OverlapPossibility possibility = this.checkOverlap(overlap);
        return switch (possibility) {
            case IS_CONTAINED -> overlap;
            case STARTS_BEFORE_ENDS_WITHIN -> this.of(this.start(), overlap.end());
            case STARTS_WITHIN_ENDS_AFTER -> this.of(overlap.start(), this.end());
            default -> this;
        };
    }

    default Set<TimeSlot> sum(TimeSlot other) {
        if (other == null) {
            return Set.of();
        }

        Set<TimeSlot> result = new TreeSet<>();
        OverlapPossibility possibility = this.checkOverlap(other);

        switch (possibility) {
            case EQUALS, CONTAINS -> result.add(this);
            case IS_CONTAINED -> result.add(other);
            case STARTS_BEFORE_ENDS_WITHIN -> result.add(this.of(this.start(), other.end()));
            case STARTS_WITHIN_ENDS_AFTER -> result.add(this.of(other.start(), this.end()));
            default -> {
                if (this.start().equals(other.end())) {
                    result.add(this.of(other.start(), this.end()));
                    break;
                }
                if (this.end().equals(other.start())) {
                    result.add(this.of(this.start(), other.end()));
                    break;
                }
                result.add(this);
                result.add(other);
            }
        }
        return result;
    }

    /**
     * Intersect the timeslots, by performing the A disjunction of B operation
     * <p>
     * <p/>This - Other (A - B):
     * <p>  this:          |------------------|       <p/>
     * <p> other:                 |------------------|<p/>
     * <p> Subtraction:    |------|                   <p/>
     * <p>
     * <p/>This - Other (A - B):
     * <p>  this:          |------------------|       <p/>
     * <p> other:                 |-----------|       <p/>
     * <p> Subtraction:    |------|                   <p/>
     * <p>
     * <p/>This - Other (A - B):
     * <p>  this:          |----------|                   <p/>
     * <p> other:                          |-----------|  <p/>
     * <p> Subtraction:    |----------|                   <p/>
     *
     * @param other other time slits to subtract from this
     * @return Set of all subtracted result
     */
    default Set<TimeSlot> subtract(TimeSlot other) {
        Set<TimeSlot> results = new TreeSet<>();
        OverlapPossibility possibility = this.checkOverlap(other);
        switch (possibility) {
            case CONTAINS -> {
                if (this.start().equals(other.start())) {
                    results.add(this.of(other.end(), this.end()));
                    break;
                }
                if (this.end().equals(other.end())) {
                    results.add(this.of(this.start(), other.start()));
                    break;
                }
                results.add(this.of(this.start(), other.start()));
                results.add(this.of(other.end(), this.end()));
            }
            case STARTS_BEFORE_ENDS_WITHIN -> results.add(this.of(this.start(), other.start()));
            case STARTS_WITHIN_ENDS_AFTER -> results.add(this.of(other.end(), this.end()));
            case EQUALS -> results.addAll(Set.of());
            default -> results.add(this);
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
