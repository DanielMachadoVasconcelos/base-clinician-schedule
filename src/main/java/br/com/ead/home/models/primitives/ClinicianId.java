package br.com.ead.home.models.primitives;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Comparator;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
public final class ClinicianId implements Comparable<ClinicianId> {

    private String value;

    public ClinicianId(String value) {
        Preconditions.checkNotNull(value, "Clinician identifier is mandatory");
        this.value = value;
    }

    public static ClinicianId from(UUID uuid) {
        Preconditions.checkNotNull(uuid, "Clinician identifier is mandatory");
        return new ClinicianId(uuid.toString());
    }

    @Override
    public int compareTo(ClinicianId o) {
        return Comparator.comparing(ClinicianId::getValue).compare(this, o);
    }
}
