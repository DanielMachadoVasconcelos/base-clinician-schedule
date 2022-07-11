package br.com.ead.home.models.primitives;

import com.google.common.base.Preconditions;

import java.util.UUID;

public record ClinicianId(UUID value) {

    public ClinicianId(UUID value) {
        this.value = Preconditions.checkNotNull(value, "UUID is mandatory");
    }
}
