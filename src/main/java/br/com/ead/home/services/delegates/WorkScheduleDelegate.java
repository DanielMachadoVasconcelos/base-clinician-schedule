package br.com.ead.home.services.delegates;

import br.com.ead.home.common.namespace.EnvironmentNamespaceResolver;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.delegates.lookups.WorkScheduleServiceLookup;
import br.com.ead.home.types.ServicePartitionType;
import br.com.ead.home.types.ServiceStageType;

import java.util.Set;

public record WorkScheduleDelegate(ServiceStageType stage, ServicePartitionType partition) implements WorkScheduleService {

    private static final WorkScheduleServiceLookup lookup = new WorkScheduleServiceLookup(new EnvironmentNamespaceResolver());

    @Override
    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        return lookup.getService(stage, partition).findAllByClinicianId(clinicianId);
    }

    @Override
    public Set<Shift> findAllShifts() {
        return lookup.getService(stage, partition).findAllShifts();
    }
}
