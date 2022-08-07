package br.com.ead.home.services.delegates;

import br.com.ead.home.common.lookups.Lookup;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.WorkScheduleService;

import java.util.Set;

public class WorkScheduleDelegate implements WorkScheduleService {

    private final Lookup<WorkScheduleService> lookup;

    public WorkScheduleDelegate(Lookup<WorkScheduleService> lookup) {
        this.lookup = lookup;
    }

    @Override
    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        return lookup.lookup().findAllByClinicianId(clinicianId);
    }

    @Override
    public Set<Shift> findAllShifts() {
        return lookup.lookup().findAllShifts();
    }

    @Override
    public Shift scheduleShift(Shift shift) {
        return lookup.lookup().scheduleShift(shift);
    }
}
