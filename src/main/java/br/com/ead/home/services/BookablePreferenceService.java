package br.com.ead.home.services;

import br.com.ead.home.common.injectables.Service;
import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;

public interface BookablePreferenceService extends Service {

    TimeSlotPreferences findClinicianPreferences(ClinicianId clinicianId);
}
