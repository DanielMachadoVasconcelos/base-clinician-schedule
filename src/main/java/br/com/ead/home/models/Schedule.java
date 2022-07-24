package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.TimeSlotSlicer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public record Schedule(ClinicianId clinicianId, Set<Shift> shift, Set<Appointment> bookings) {

    public Set<TimeSlot> getBookableAvailability(TimeSlotSlicer slicer) {
        log.debug("Getting Bookable availabilities. Shifts={}, Appointments={}",
                this.shift.size(), this.bookings.size());
       var bookable = shift.stream()
            .map(current -> current.subtractAll(Sets.newTreeSet(CollectionUtils.collect(bookings, Appointment::timeSlot))))
            .flatMap(Set::stream)
            .map(slicer::split)
            .flatMap(Set::stream)
            .collect(Collectors.toCollection(TreeSet::new));
        log.debug("Got all Bookable Availabilities of size={} from Clinician={} Schedule",
                bookable.size() ,clinicianId().value());
        return bookable;
    }

    public static Schedule mergeByClinician(Schedule accumulator, Schedule next) {
        Preconditions.checkState(accumulator.clinicianId().equals(next.clinicianId()), "Clinician must be the same");
        log.debug("Merging into Schedule {} the Schedule from clinician={}",
                accumulator.clinicianId().value(),
                next.clinicianId().value());

        var shifts = Stream.concat(accumulator.shift().stream(), next.shift().stream()).collect(Collectors.toSet());
        log.debug("Merged all shifts from clinician={}. Accumulator size={}, Next size={}",
                accumulator.clinicianId().value(),
                accumulator.shift().size(),
                next.shift().size());

        var appointments = Stream.concat(accumulator.bookings().stream(), next.bookings().stream()).collect(Collectors.toSet());
        log.debug("Merged all Appointments from clinician={}. Accumulator size={}, Next size={}",
                accumulator.clinicianId().value(),
                accumulator.bookings().size(),
                next.bookings().size());

        return new Schedule(accumulator.clinicianId(), shifts, appointments);
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
