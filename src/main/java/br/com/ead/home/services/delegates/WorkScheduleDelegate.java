package br.com.ead.home.services.delegates;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.api.WorkScheduleService;
import br.com.ead.home.services.delegates.lookups.WorkScheduleLookup;
import br.com.ead.home.services.delegates.types.WorkScheduleServiceType;

import java.util.Set;

public record WorkScheduleDelegate(WorkScheduleServiceType type) implements WorkScheduleService {

    private static WorkScheduleLookup lookup = new WorkScheduleLookup();

    @Override
    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        return lookup.getService(type).findAllByClinicianId(clinicianId);
    }

    @Override
    public Set<Shift> findAllShifts() {
        return lookup.getService(type).findAllShifts();
    }
}
