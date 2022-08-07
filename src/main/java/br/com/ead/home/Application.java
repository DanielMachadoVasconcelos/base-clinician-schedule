package br.com.ead.home;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.injectables.Bean;
import br.com.ead.home.common.loader.InMemoryBeanLoader;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.SystemEnvironmentVariables;
import br.com.ead.home.configurations.system.MockSystemClockProvider;
import br.com.ead.home.controllers.AppointmentController;
import br.com.ead.home.controllers.ScheduleAvailabilityController;
import br.com.ead.home.controllers.ShiftController;
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

    private static final ZonedDateTime today = new MockSystemClockProvider().currentDay();
    private static final ZonedDateTime tomorrow = today.plusDays(1);

    private static final ClinicianId clinicianId = new ClinicianId("Hermonie");
    private static final PatientId patientId = new PatientId("Sara");

    private static InitialContext initialContext = InMemoryInitialContext.getInstance();
    private static Environment environment = new SystemEnvironmentVariables();
    private static NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);
    private static InMemoryBeanLoader loader = new InMemoryBeanLoader(environment, initialContext, resolver);

    static {
        loader.init();
    }

    public static void main(String[] args) {
        InitialContext initialContext = InMemoryInitialContext.getInstance();
        Environment environment = new SystemEnvironmentVariables();
        NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);
        InMemoryBeanLoader loader = new InMemoryBeanLoader(environment, initialContext, resolver);
        loader.init();

        TimeSlot timeSlot = Slot.from(today, Duration.ofHours(1));
        AppointmentController controller = getAppointmentController();

        Appointment appointment = controller.createAppointment(clinicianId, patientId, timeSlot);
        log.info("New Appointment created: {}", appointment);
    }

    private static AppointmentController getAppointmentController() {
        AppointmentController controller = getBean(AppointmentController.class);
        log.debug("AppointmentController successfully recovery from initial context");
        return controller;
    }

    private static ShiftController getShiftController() {
        ShiftController controller = getBean(ShiftController.class);
        log.debug("ShiftController successfully recovery from initial context");
        return controller;
    }

    private static ScheduleAvailabilityController getScheduleAvailabilityController() {
        ScheduleAvailabilityController controller = getBean(ScheduleAvailabilityController.class);
        log.debug("ScheduleAvailabilityController successfully recovery from initial context");
        return controller;
    }

    private static <T extends Bean> T getBean(Class<T> clazz) {
        String jndiName = resolver.namespace(StageType.UNIT_TEST, PartitionType.SWEDEN, clazz.getName());
        return (T) initialContext.lookup(jndiName);
    }
}
