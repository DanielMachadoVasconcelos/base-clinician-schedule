package br.com.ead.home.repositories;

import br.com.ead.home.common.injectables.Repository;
import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;

import java.util.Set;

public interface AppointmentRepository extends Repository {

    Set<Appointment> findAllByClinicianId(ClinicianId clinicianId);

    Set<Appointment> findAllAppointments();

    Appointment bookAppointment(Appointment appointment);
}
