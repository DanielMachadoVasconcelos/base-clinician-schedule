package br.com.ead.home.services.implementations;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.ShiftRepository;
import br.com.ead.home.services.WorkScheduleService;

import java.util.Set;

public record ClinicianWorkScheduleService(ShiftRepository repository) implements WorkScheduleService {

    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        return repository.findAllByClinicianId(clinicianId);
    }

    public Set<Shift> findAllShifts(){
        return repository.findAllShifts();
    }

    @Override
    public Shift scheduleShift(Shift shift) {
        return repository.scheduleShift(shift);
    }
}
