package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.TimeSlotSplitter;

import java.util.Set;
import java.util.stream.Collectors;

public record Schedule(ClinicianId clinicianId, Shift shift, Set<TimeSlot> bookings) {

    public Set<TimeSlot> getBookableAvailability(TimeSlotSplitter splitter) {
        return shift.subtractAll(bookings)
                    .stream()
                    .map(splitter::split)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
    }
}
