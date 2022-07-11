package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

public record Shift(ClinicianId clinicianId, TimeSlot timeSlot) {

    public Shift(ClinicianId clinicianId, TimeSlot timeSlot) {
        this.clinicianId = Preconditions.checkNotNull(clinicianId, "Clinician is Mandatory");
        this.timeSlot = Preconditions.checkNotNull(timeSlot, "Time slot is mandatory");
    }

    public Set<TimeSlot> subtractAll(Set<TimeSlot> bookings) {
        return CollectionUtils.emptyIfNull(bookings)
                .stream()
                .sorted(TimeSlot::compareTo)
                .reduce(Set.of(timeSlot),
                        (acc, next) -> acc.stream().flatMap(item -> item.subtract(next).stream()).collect(Collectors.toSet()),
                        (acc, next) -> Sets.newTreeSet(CollectionUtils.union(acc, next)));
    }

    public Set<TimeSlot> subtract(TimeSlot meeting) {
        return this.timeSlot.subtract(meeting);
    }
}
