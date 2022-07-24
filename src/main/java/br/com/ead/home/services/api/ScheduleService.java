package br.com.ead.home.services.api;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;

import java.util.Set;

public interface ScheduleService {

    Set<Appointment> findAllByClinicianId(ClinicianId clinicianId);

    Set<Appointment> findAllAppointments();
}
