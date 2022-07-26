package br.com.ead.home.services.api;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.Service;

import java.util.Set;

public interface ScheduleService extends Service {

    Set<Appointment> findAllByClinicianId(ClinicianId clinicianId);

    Set<Appointment> findAllAppointments();
}
