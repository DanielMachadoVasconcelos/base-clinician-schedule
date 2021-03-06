package br.com.ead.home.repositories.implementations;

import br.com.ead.home.configurations.system.ClockProvider;
import br.com.ead.home.models.SchedulePreferences;
import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.ClinicianPreferencesRepository;

import java.time.Duration;
import java.util.Optional;

public record MockClinicianPreferencesRepository(ClockProvider clockProvider) implements ClinicianPreferencesRepository {

    @Override
    public Optional<TimeSlotPreferences> findClinicianPreferences(ClinicianId clinicianId) {
        return Optional.of(SchedulePreferences.builder()
                .meetingLength(Duration.ofHours(1))
                .build());
    }
}
