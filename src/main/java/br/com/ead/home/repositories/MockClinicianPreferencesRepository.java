package br.com.ead.home.repositories;

import br.com.ead.home.configurations.ClockProvider;
import br.com.ead.home.models.SchedulePreferences;
import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public record MockClinicianPreferencesRepository(ClockProvider clockProvider) implements ClinicianPreferencesRepository {

    @Override
    public Optional<TimeSlotPreferences> findClinicianPreferences(ClinicianId clinicianId) {
        return Optional.of(SchedulePreferences.builder()
                .meetingLength(Duration.ofHours(1))
                .build());
    }
}
