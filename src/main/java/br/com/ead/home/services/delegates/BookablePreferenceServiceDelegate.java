package br.com.ead.home.services.delegates;

import br.com.ead.home.models.api.TimeSlotPreferences;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.api.BookablePreferenceService;
import br.com.ead.home.services.delegates.lookups.BookablePreferencesServiceLookup;
import br.com.ead.home.services.delegates.namespace.EnvironmentNamespaceResolver;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;

public record BookablePreferenceServiceDelegate(ServiceStageType stage, ServicePartitionType partition) implements BookablePreferenceService {

    private static final BookablePreferencesServiceLookup lookup = new BookablePreferencesServiceLookup(new EnvironmentNamespaceResolver());

    @Override
    public TimeSlotPreferences findClinicianPreferences(ClinicianId clinicianId) {
        return lookup.getService(stage, partition).findClinicianPreferences(clinicianId);
    }
}
