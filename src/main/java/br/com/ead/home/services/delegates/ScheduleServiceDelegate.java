package br.com.ead.home.services.delegates;

import br.com.ead.home.common.lookups.Lookup;
import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.ScheduleService;
import lombok.extern.log4j.Log4j2;

import java.util.Set;

@Log4j2
public class ScheduleServiceDelegate implements ScheduleService {

    private final Lookup<ScheduleService> lookup;

    public ScheduleServiceDelegate(Lookup<ScheduleService> lookup) {
        this.lookup = lookup;
    }

    @Override
    public Set<Appointment> findAllByClinicianId(ClinicianId clinicianId) {
        return lookup.lookup().findAllByClinicianId(clinicianId);
    }

    @Override
    public Set<Appointment> findAllAppointments() {
        return lookup.lookup().findAllAppointments();
    }

    @Override
    public Appointment bookAppointment(Appointment appointment) {
        return lookup.lookup().bookAppointment(appointment);
    }
}
