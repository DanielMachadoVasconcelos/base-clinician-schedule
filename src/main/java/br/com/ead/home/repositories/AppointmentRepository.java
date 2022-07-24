package br.com.ead.home.repositories;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;

import java.util.Set;

public interface AppointmentRepository {

    Set<Appointment> findAllByClinicianId(ClinicianId clinicianId);

    Set<Appointment> findAllAppointments();

}
