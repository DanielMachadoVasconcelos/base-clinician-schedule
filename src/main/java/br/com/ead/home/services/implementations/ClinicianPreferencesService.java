package br.com.ead.home.services.implementations;

import br.com.ead.home.models.SchedulePreferences;
import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.ClinicianPreferencesRepository;
import br.com.ead.home.services.BookablePreferenceService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public record ClinicianPreferencesService(ClinicianPreferencesRepository repository) implements BookablePreferenceService {

    public TimeSlotPreferences findClinicianPreferences(ClinicianId clinicianId){
        return repository.findClinicianPreferences(clinicianId)
                .orElseGet(SchedulePreferences.builder()
                        .meetingLength(Duration.ofHours(1))
                        .bufferBetweenMeetings(Duration.ofMinutes(5))
                        ::build);
    }
}
