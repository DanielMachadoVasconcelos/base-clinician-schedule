package br.com.ead.home.services;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.ScheduleRepository;

import java.util.Set;

public record ScheduleService(ScheduleRepository repository) {

    public Set<Shift> findByClinicianId(ClinicianId clinicianId) {
        return repository.findAllByClinicianId(clinicianId);
    }

    public Set<Shift> findAllShift() {
        return repository.findAllShift();
    }

    public Set<Appointment> findAllAppointment() {
        return repository.findAllAppointment();
    }

    public Set<Appointment> findAllAppointmentByClinicianId(ClinicianId clinicianId) {
        return repository.findAllAppointmentByClinicianId(clinicianId);
    }
}
