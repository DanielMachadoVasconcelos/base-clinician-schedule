package br.com.ead.home.repositories;

import br.com.ead.home.configurations.SystemClockProvider;
import br.com.ead.home.models.SchedulePreferences;
import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;

import java.time.Duration;
import java.util.Optional;

public record MockClinicianPreferencesRepository(SystemClockProvider systemClockProvider) implements ClinicianPreferencesRepository {

    @Override
    public Optional<TimeSlotPreferences> findClinicianPreferences(ClinicianId clinicianId) {
        return Optional.of(SchedulePreferences.builder()
                .meetingLength(Duration.ofHours(1))
                .build());
    }
}
