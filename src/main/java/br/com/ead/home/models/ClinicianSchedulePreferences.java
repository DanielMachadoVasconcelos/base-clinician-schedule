package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.api.TimeSlotPreferences;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
@RequiredArgsConstructor
public final class ClinicianSchedulePreferences implements TimeSlotPreferences {

    private final Duration meetingLength;
    private final Duration bufferBetweenMeetings;
    private final Duration nextMeetingOnlyIn;
    private final Long onlyMaximumOfFreeSlots;

    @Override
    public Set<TimeSlot> slice(TimeSlot timeSlot) {

        if (timeSlot == null) {
            return Set.of();
        }

        if (Duration.between(timeSlot.start(), timeSlot.end()).compareTo(meetingLength) < 0) {
            return Set.of();
        }

        Duration spaceBetweenMeetings = Optional.ofNullable(this.bufferBetweenMeetings).orElse(Duration.ZERO);
        Duration meetingDuration = Optional.ofNullable(this.meetingLength).orElse(Duration.ofMinutes(30));
        Duration anyMeetingOnlyAfter = Optional.ofNullable(this.nextMeetingOnlyIn).orElse(Duration.ZERO);

        TimeSlot seed = new Slot(timeSlot.start().plus(anyMeetingOnlyAfter), timeSlot.start().plus(meetingDuration).plus(anyMeetingOnlyAfter));
        Predicate<TimeSlot> hasNext = nextSlot -> nextSlot.end().isBefore(timeSlot.end()) || nextSlot.end().equals(timeSlot.end());
        UnaryOperator<TimeSlot> next = nextSlot -> nextSlot.of(nextSlot.start().plus(spaceBetweenMeetings).plus(meetingDuration), nextSlot.end().plus(spaceBetweenMeetings).plus(meetingDuration));

        return Optional.ofNullable(onlyMaximumOfFreeSlots)
                        .filter(it -> it > 0)
                        .map(limitTo -> Stream.iterate(seed, hasNext, next)
                                              .limit(limitTo))
                                              .orElseGet(() -> Stream.iterate(seed, hasNext, next))
                                              .collect(Collectors.toCollection(TreeSet::new));
    }
}
