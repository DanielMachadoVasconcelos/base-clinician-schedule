package br.com.ead.home.services;

import br.com.ead.home.common.injectors.Service;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;

import java.util.Set;

public interface WorkScheduleService extends Service {

    Set<Shift> findAllByClinicianId(ClinicianId clinicianId);
    Set<Shift> findAllShifts();
}
