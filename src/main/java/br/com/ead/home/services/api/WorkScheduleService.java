package br.com.ead.home.services.api;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.Service;

import java.util.Set;

public interface WorkScheduleService extends Service {

    Set<Shift> findAllByClinicianId(ClinicianId clinicianId);
    Set<Shift> findAllShifts();
}
