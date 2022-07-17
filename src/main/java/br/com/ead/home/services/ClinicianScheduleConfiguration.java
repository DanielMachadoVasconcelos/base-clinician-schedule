package br.com.ead.home.services;

import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import com.google.common.base.Preconditions;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ClinicianScheduleConfiguration implements TimeSlotSlicer {

    private final ZonedDateTime now;
    private final Duration meetingLength;
    private final Duration bufferBetweenMeetings;
    private final Duration nextMeetingOnlyIn;
    private final Long onlyMaximumOfFreeSlots;
    private final ZoneId doctorTimeZone;

    public ClinicianScheduleConfiguration(Clock clock,
                                          Duration meetingLength,
                                          Duration bufferBetweenMeetings,
                                          Duration nextMeetingOnlyIn,
                                          Long onlyMaximumOfFreeSlots,
                                          ZoneId doctorTimeZone) {

        this.meetingLength = Optional.ofNullable(meetingLength).orElse(Duration.ofMinutes(30));
        Preconditions.checkState(!this.meetingLength.isNegative(), "Meeting length must be positive");

        this.doctorTimeZone = Optional.ofNullable(doctorTimeZone).orElse(ZoneOffset.UTC);
        this.bufferBetweenMeetings = Optional.ofNullable(bufferBetweenMeetings).orElse(Duration.ZERO);
        this.nextMeetingOnlyIn = Optional.ofNullable(nextMeetingOnlyIn).orElse(Duration.ZERO);
        this.onlyMaximumOfFreeSlots = onlyMaximumOfFreeSlots;

        this.now = Optional.ofNullable(clock)
                            .map(ZonedDateTime::now)
                            .orElseGet(() -> getCurrentDateTime(this.doctorTimeZone));
    }

    private static ZonedDateTime getCurrentDateTime(ZoneId zoneId) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        return ZonedDateTime.of(date, time, zoneId);
    }

    @Override
    public Set<TimeSlot> split(TimeSlot timeSlot) {

        if (timeSlot == null) {
            return Set.of();
        }

        if (Duration.between(timeSlot.start(), timeSlot.end()).compareTo(meetingLength) < 0) {
            return Set.of();
        }

        TimeSlot seed = new Slot(timeSlot.start().plus(nextMeetingOnlyIn), timeSlot.start().plus(meetingLength).plus(nextMeetingOnlyIn));
        Predicate<TimeSlot> hasNext = nextSlot -> nextSlot.end().isBefore(timeSlot.end()) || nextSlot.end().equals(timeSlot.end());
        UnaryOperator<TimeSlot> next = nextSlot -> nextSlot.of(
                nextSlot.start().plus(bufferBetweenMeetings).plus(meetingLength),
                nextSlot.end().plus(bufferBetweenMeetings).plus(meetingLength));

       return Optional.ofNullable(onlyMaximumOfFreeSlots)
                .filter(it -> it > 0)
                .map(limitTo -> Stream.iterate(seed, hasNext, next).limit(limitTo))
                .orElseGet(() -> Stream.iterate(seed, hasNext, next))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public static class Builder {
        private Clock clock;
        private Duration meetingLength;
        private Duration bufferBetweenMeetings;
        private Duration nextMeetingOnlyIn;
        private Long onlyMaximumOfFreeSlots;
        private ZoneId doctorTimeZone;

        public Builder setClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public Builder setMeetingLength(Duration meetingLength) {
            this.meetingLength = meetingLength;
            return this;
        }

        public Builder setBufferBetweenMeetings(Duration bufferBetweenMeetings) {
            this.bufferBetweenMeetings = bufferBetweenMeetings;
            return this;
        }

        public Builder setNextMeetingOnlyIn(Duration nextMeetingOnlyIn) {
            this.nextMeetingOnlyIn = nextMeetingOnlyIn;
            return this;
        }

        public Builder setOnlyMaximumOfFreeSlots(Long onlyMaximumOfFreeSlots) {
            this.onlyMaximumOfFreeSlots = onlyMaximumOfFreeSlots;
            return this;
        }

        public Builder setDoctorTimeZone(ZoneId doctorTimeZone) {
            this.doctorTimeZone = doctorTimeZone;
            return this;
        }

        public ClinicianScheduleConfiguration build() {
            return new ClinicianScheduleConfiguration(clock,
                    meetingLength,
                    bufferBetweenMeetings,
                    nextMeetingOnlyIn,
                    onlyMaximumOfFreeSlots,
                    doctorTimeZone);
        }
    }
}
