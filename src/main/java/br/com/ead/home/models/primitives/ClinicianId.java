package br.com.ead.home.models.primitives;

import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.UUID;

public record ClinicianId(String value) implements Comparable<ClinicianId> {

    public ClinicianId {
        Preconditions.checkNotNull(value, "Clinician identifier is mandatory");
    }

    public static ClinicianId from(UUID uuid) {
        Preconditions.checkNotNull(uuid, "Clinician identifier is mandatory");
        return new ClinicianId(uuid.toString());
    }

    @Override
    public int compareTo(ClinicianId o) {
        return Comparator.comparing(ClinicianId::value).compare(this, o);
    }
}
