package br.com.ead.home.services;

import br.com.ead.home.common.injectables.Service;
import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;

import java.util.Set;

public interface ScheduleService extends Service {

    Set<Appointment> findAllByClinicianId(ClinicianId clinicianId);

    Set<Appointment> findAllAppointments();
}
