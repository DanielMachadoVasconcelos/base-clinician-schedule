package br.com.ead.home.services;

import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record DoctorScheduleConfiguration(Duration meetingLength,
                                          Duration bufferBetweenMeetings,
                                          Duration nextMeetingOnlyIn,
                                          Long onlyMaximumOfFreeSlots,
                                          ZoneId doctorTimeZone) implements TimeSlotSplitter {

    @Override
    public Set<TimeSlot> split(TimeSlot timeSlot) {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        ZonedDateTime from = ZonedDateTime.of(today, now, doctorTimeZone);
        ZonedDateTime nextMeetingOnlyAfter = from.plus(nextMeetingOnlyIn);

        ZonedDateTime seed = timeSlot.start();
        Predicate<ZonedDateTime> hasNext = nextSlot -> nextSlot.isBefore(timeSlot.end());
        UnaryOperator<ZonedDateTime> next = nextSlot -> nextSlot.plus(meetingLength);
        Function<ZonedDateTime, TimeSlot> mapToTimeSlot = date -> new Slot(date, date.plus(meetingLength));

        return Stream.iterate(seed, hasNext, next)
                .map(mapToTimeSlot)
                .filter(slot -> nextMeetingOnlyAfter.isBefore(slot.start()))
                .limit(onlyMaximumOfFreeSlots)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
