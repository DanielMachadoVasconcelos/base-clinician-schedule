package br.com.ead.home.services.delegates;

import br.com.ead.home.common.namespace.EnvironmentNamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.delegates.lookups.WorkScheduleServiceLookup;

import java.util.Set;

public record WorkScheduleDelegate(StageType stage, PartitionType partition) implements WorkScheduleService {

    private static final WorkScheduleServiceLookup lookup = new WorkScheduleServiceLookup(new EnvironmentNamespaceResolver());

    @Override
    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        return lookup.lookup(stage, partition).findAllByClinicianId(clinicianId);
    }

    @Override
    public Set<Shift> findAllShifts() {
        return lookup.lookup(stage, partition).findAllShifts();
    }
}
