package br.com.ead.home.services.implementations;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.AppointmentRepository;
import br.com.ead.home.services.ScheduleService;
import com.google.common.base.Preconditions;

import java.util.Set;

public record AppointmentService(AppointmentRepository repository) implements ScheduleService {

    public Set<Appointment> findAllByClinicianId(ClinicianId clinicianId){
        return repository.findAllByClinicianId(clinicianId);
    }

    public Set<Appointment> findAllAppointments(){
        return repository.findAllAppointments();
    }

    @Override
    public Appointment bookAppointment(Appointment appointment) {
        Preconditions.checkNotNull(appointment, "Appointment is mandatory");
        Preconditions.checkNotNull(appointment.clinicianId(), "Appointment clinicianId is mandatory");
        Preconditions.checkNotNull(appointment.patientId(), "Appointment patientId is mandatory");
        Preconditions.checkNotNull(appointment.timeSlot(), "Appointment timeSlot is mandatory");

        boolean overlapsOtherAppointment = repository.findAllByClinicianId(appointment.clinicianId()).stream()
                .map(Appointment::timeSlot)
                .noneMatch(appointment.timeSlot()::overlaps);

        Preconditions.checkState(overlapsOtherAppointment, "Time slot overlap other appointment");

        return repository.bookAppointment(appointment);
    }
}
