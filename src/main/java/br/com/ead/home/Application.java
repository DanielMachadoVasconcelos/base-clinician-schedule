package br.com.ead.home;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.loader.InMemoryBeanLoader;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.SystemEnvironmentVariables;
import br.com.ead.home.controllers.AppointmentController;
import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.ZonedDateTime;

@Log4j2
public class Application {

    public static void main(String[] args) {
        InitialContext initialContext = InMemoryInitialContext.getInstance();
        Environment environment = new SystemEnvironmentVariables();
        NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);
        InMemoryBeanLoader loader = new InMemoryBeanLoader(environment, initialContext, resolver);
        loader.init();

        ClinicianId clinicianId = new ClinicianId("Thomas");
        PatientId patientId = new PatientId("Sara");
        TimeSlot timeSlot = Slot.from(ZonedDateTime.now(), Duration.ofHours(1));

        String jndiName = resolver.namespace(StageType.UNIT_TEST, PartitionType.SWEDEN, AppointmentController.class.getName());
        AppointmentController controller = (AppointmentController) initialContext.lookup(jndiName);

        Appointment appointment = controller.createAppointment(clinicianId, patientId, timeSlot);
        log.info("Appointment: {}", appointment);
    }
}
