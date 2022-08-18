package br.com.ead.home.models.primitives;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Comparator;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public final class PatientId implements Comparable<PatientId> {

    private String value;

    public PatientId(String value) {
        Preconditions.checkNotNull(value, "Patient identifier is mandatory");
        this.value = value;
    }

    public static PatientId from(UUID uuid) {
        Preconditions.checkNotNull(uuid, "Clinician identifier is mandatory");
        return new PatientId(uuid.toString());
    }

    @Override
    public int compareTo(PatientId other) {
        return Comparator.comparing(PatientId::getValue).compare(this, other);
    }
}
