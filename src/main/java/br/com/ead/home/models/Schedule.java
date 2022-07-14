package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.TimeSlotSplitter;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public record Schedule(ClinicianId clinicianId, Set<Shift> shift, Set<Appointment> bookings) {

    public Set<TimeSlot> getBookableAvailability(TimeSlotSplitter splitter) {
        return shift.stream()
            .map(current -> current.subtractAll(Sets.newTreeSet(CollectionUtils.collect(bookings, Appointment::timeSlot))))
            .flatMap(Set::stream)
            .map(splitter::split)
            .flatMap(Set::stream)
            .collect(Collectors.toCollection(TreeSet::new));
    }

    public static Schedule mergeByClinicianAndShift(Schedule accumulator, Schedule next) {
        return new Schedule(accumulator.clinicianId(),
                            new TreeSet(SetUtils.union(accumulator.shift(), next.shift())),
                            new TreeSet(SetUtils.union(accumulator.bookings(), next.bookings())));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("clinicianId", clinicianId)
                .append("shift", shift)
                .append("bookings", bookings)
                .toString();
    }
}
