package br.com.ead.home.services.implementations;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.AppointmentRepository;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.validations.AppointmentRules;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;

import java.util.Set;

@Log4j2
public record AppointmentService(AppointmentRepository repository) implements ScheduleService {

    public Set<Appointment> findAllByClinicianId(ClinicianId clinicianId){
        return repository.findAllByClinicianId(clinicianId);
    }

    public Set<Appointment> findAllAppointments(){
        return repository.findAllAppointments();
    }

    @Override
    public Appointment bookAppointment(Appointment appointment) {

        AppointmentRules.validateAndThrow(appointment);

        Set<Appointment> allAppointmentsForClinician = repository.findAllByClinicianId(appointment.clinicianId());
        boolean overlapsOtherAppointment = allAppointmentsForClinician.stream()
                .map(Appointment::timeSlot)
                .noneMatch(appointment.timeSlot()::overlaps);

        Preconditions.checkState(overlapsOtherAppointment, "Time slot overlap other appointment");

        return repository.bookAppointment(appointment);
    }
}
