package br.com.ead.home.services;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.ShiftRepository;
import br.com.ead.home.services.api.WorkScheduleService;

import java.util.Set;

public record ShiftService(ShiftRepository repository) implements WorkScheduleService {

    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        return repository.findAllByClinicianId(clinicianId);
    }

    public Set<Shift> findAllShifts(){
        return repository.findAllShifts();
    }
}
