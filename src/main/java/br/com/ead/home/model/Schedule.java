package br.com.ead.home.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;

public class Schedule {

    String clinicianId;
    Set<TimeSlot> available;
    Set<TimeSlot> reserved;

    public Schedule(String clinicianId, Set<TimeSlot> available, Set<TimeSlot> reserved) {
        this.clinicianId = Preconditions.checkNotNull(clinicianId, "ClinicianId is mandatory");
        this.reserved = Sets.newHashSet(CollectionUtils.emptyIfNull(reserved));
        this.available = Sets.newHashSet(CollectionUtils.removeAll(CollectionUtils.emptyIfNull(available), reserved));
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public Set<TimeSlot> getAvailable() {
        return available;
    }

    public Set<TimeSlot> getReserved() {
        return reserved;
    }

    public Schedule merge(Schedule other) {
        Preconditions.checkState(clinicianId.equals(other.clinicianId), "ClinicianId is not the same");
        this.available = Sets.newHashSet(Iterables.concat(other.available, this.available));
        this.reserved = Sets.newHashSet(Iterables.concat(other.reserved, this.reserved));
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("clinicianId", clinicianId)
                .add("available", available)
                .add("reserved", reserved)
                .toString();
    }
}
