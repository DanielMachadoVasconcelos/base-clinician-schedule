package br.com.ead.home.services.delegates;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.api.ScheduleService;
import br.com.ead.home.services.delegates.lookups.ScheduleServiceLookup;
import br.com.ead.home.services.delegates.namespace.EnvironmentNamespaceResolver;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;
import lombok.extern.log4j.Log4j2;

import java.util.Set;

@Log4j2
public record ScheduleServiceDelegate(ServiceStageType stage, ServicePartitionType partition) implements ScheduleService {

    private static final ScheduleServiceLookup lookup = new ScheduleServiceLookup(new EnvironmentNamespaceResolver());

    @Override
    public Set<Appointment> findAllByClinicianId(ClinicianId clinicianId) {
        return lookup.getService(stage, partition).findAllByClinicianId(clinicianId);
    }

    @Override
    public Set<Appointment> findAllAppointments() {
        return lookup.getService(stage, partition).findAllAppointments();
    }
}
