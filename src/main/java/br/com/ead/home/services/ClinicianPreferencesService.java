package br.com.ead.home.services;

import br.com.ead.home.models.ClinicianSchedulePreferences;
import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.ClinicianPreferencesRepository;

import java.time.Duration;

public record ClinicianPreferencesService(ClinicianPreferencesRepository repository) {

    public TimeSlotPreferences findClinicianPreferences(ClinicianId clinicianId){
        return repository.findClinicianPreferences(clinicianId)
                .orElseGet(ClinicianSchedulePreferences.builder()
                        .meetingLength(Duration.ofHours(1))
                        .bufferBetweenMeetings(Duration.ofMinutes(5))
                        ::build);
    }
}
