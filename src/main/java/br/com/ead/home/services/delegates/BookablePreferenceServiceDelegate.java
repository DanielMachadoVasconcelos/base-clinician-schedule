package br.com.ead.home.services.delegates;

import br.com.ead.home.common.lookups.Lookup;
import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.BookablePreferenceService;

public class BookablePreferenceServiceDelegate implements BookablePreferenceService {

    private final Lookup<BookablePreferenceService> lookup;

    public BookablePreferenceServiceDelegate(Lookup<BookablePreferenceService> lookup) {
        this.lookup = lookup;
    }

    @Override
    public TimeSlotPreferences findClinicianPreferences(ClinicianId clinicianId) {
        return lookup.lookup().findClinicianPreferences(clinicianId);
    }
}
