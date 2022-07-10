package br.com.ead.home.services;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.ScheduleRepository;

import java.util.Set;

public record ScheduleService(ScheduleRepository repository) {

    public Set<Shift> findByClinicianId(ClinicianId clinicianId) {
        return repository.findByClinicianId(clinicianId);
    }
}
