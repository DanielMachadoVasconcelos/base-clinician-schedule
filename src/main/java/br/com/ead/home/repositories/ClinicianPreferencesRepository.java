package br.com.ead.home.repositories;

import br.com.ead.home.common.injectables.Repository;
import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;

import java.util.Optional;

public interface ClinicianPreferencesRepository extends Repository {

    Optional<TimeSlotPreferences> findClinicianPreferences(ClinicianId clinicianId);
}
