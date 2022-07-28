package br.com.ead.home.services.implementations;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.AppointmentRepository;
import br.com.ead.home.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public record AppointmentService(AppointmentRepository repository) implements ScheduleService {

    public Set<Appointment> findAllByClinicianId(ClinicianId clinicianId){
        return repository.findAllByClinicianId(clinicianId);
    }

    public Set<Appointment> findAllAppointments(){
        return repository.findAllAppointments();
    }
}
