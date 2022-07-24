package br.com.ead.home.services;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.AppointmentRepository;
import br.com.ead.home.services.api.ScheduleService;

import java.util.Set;

public record AppointmentService(AppointmentRepository repository) implements ScheduleService {

    public Set<Appointment> findAllByClinicianId(ClinicianId clinicianId){
        return repository.findAllByClinicianId(clinicianId);
    }

    public Set<Appointment> findAllAppointments(){
        return repository.findAllAppointments();
    }
}
