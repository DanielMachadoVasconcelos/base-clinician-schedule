package br.com.ead.home.services;

import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import com.google.common.base.Preconditions;

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

public record DoctorScheduleConfiguration(Duration meetingLength,
                                          Duration bufferBetweenMeetings,
                                          Duration nextMeetingOnlyIn,
                                          Long onlyMaximumOfFreeSlots,
                                          ZoneId doctorTimeZone) implements TimeSlotSplitter {

    public DoctorScheduleConfiguration(Duration meetingLength,
                                       Duration bufferBetweenMeetings,
                                       Duration nextMeetingOnlyIn,
                                       Long onlyMaximumOfFreeSlots,
                                       ZoneId doctorTimeZone) {

        Preconditions.checkNotNull(meetingLength, "Minimum meeting length is mandatory");
        Preconditions.checkState(!meetingLength.isNegative(), "Meeting length must be positive");
        Preconditions.checkState(onlyMaximumOfFreeSlots > 0, "Number of free slot must be grater then zero");

        this.doctorTimeZone = Optional.ofNullable(doctorTimeZone).orElse(ZoneOffset.UTC);
        this.meetingLength = meetingLength;
        this.bufferBetweenMeetings = bufferBetweenMeetings;
        this.nextMeetingOnlyIn = nextMeetingOnlyIn;
        this.onlyMaximumOfFreeSlots = onlyMaximumOfFreeSlots;
    }

    @Override
    public Set<TimeSlot> split(TimeSlot timeSlot) {

        if(timeSlot == null){
            return Set.of();
        }

        if(Duration.between(timeSlot.start(), timeSlot.end()).compareTo(meetingLength) < 0) {
            return Set.of();
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        ZonedDateTime from = ZonedDateTime.of(today, now, doctorTimeZone);
        ZonedDateTime nextMeetingOnlyAfter = from.plus(nextMeetingOnlyIn);

        TimeSlot seed = new Slot(timeSlot.start(), timeSlot.start().plus(meetingLength));
        Predicate<TimeSlot> hasNext = nextSlot -> nextSlot.end().isBefore(timeSlot.end());
        UnaryOperator<TimeSlot> next = nextSlot -> nextSlot.of(
                nextSlot.start().plus(bufferBetweenMeetings).plus(meetingLength),
                nextSlot.end().plus(bufferBetweenMeetings).plus(meetingLength));

        return Stream.iterate(seed, hasNext, next)
                .filter(slot -> nextMeetingOnlyAfter.isBefore(slot.start()))
                .limit(onlyMaximumOfFreeSlots)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
