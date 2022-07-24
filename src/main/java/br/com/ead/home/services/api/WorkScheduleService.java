package br.com.ead.home.services.api;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;

import java.util.Set;

public interface WorkScheduleService {

    Set<Shift> findAllByClinicianId(ClinicianId clinicianId);
    Set<Shift> findAllShifts();
}
