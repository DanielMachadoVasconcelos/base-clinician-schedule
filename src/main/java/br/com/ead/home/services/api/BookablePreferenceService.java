package br.com.ead.home.services.api;

import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;

public interface BookablePreferenceService {

    TimeSlotPreferences findClinicianPreferences(ClinicianId clinicianId);
}
