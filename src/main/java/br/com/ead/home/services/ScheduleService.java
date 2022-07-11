package br.com.ead.home.services;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.MockScheduleRepository;

import java.util.Set;

public record ScheduleService(MockScheduleRepository repository) {

    public Set<Shift> findByClinicianId(ClinicianId clinicianId) {
        return repository.findByClinicianId(clinicianId);
    }
}
