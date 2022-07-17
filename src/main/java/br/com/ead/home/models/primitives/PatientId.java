package br.com.ead.home.models.primitives;

import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.UUID;

public record PatientId(String value) implements Comparable<PatientId>{

    public PatientId {
        Preconditions.checkNotNull(value, "Patient identifier is mandatory");
    }

    public static PatientId from(UUID uuid) {
        Preconditions.checkNotNull(uuid, "Clinician identifier is mandatory");
        return new PatientId(uuid.toString());
    }

    @Override
    public int compareTo(PatientId other) {
        return Comparator.comparing(PatientId::value).compare(this, other);
    }
}
