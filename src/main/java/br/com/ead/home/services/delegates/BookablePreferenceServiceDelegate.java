package br.com.ead.home.services.delegates;

import br.com.ead.home.common.namespace.EnvironmentNamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.delegates.lookups.BookablePreferencesServiceLookup;

public record BookablePreferenceServiceDelegate(StageType stage, PartitionType partition) implements BookablePreferenceService {

    private static final BookablePreferencesServiceLookup lookup = new BookablePreferencesServiceLookup(new EnvironmentNamespaceResolver());

    @Override
    public TimeSlotPreferences findClinicianPreferences(ClinicianId clinicianId) {
        return lookup.lookup(stage, partition).findClinicianPreferences(clinicianId);
    }
}
