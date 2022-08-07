package br.com.ead.home;

import io.vertx.core.Launcher;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Application {

    public static void main(String[] args) {
        InitialContext initialContext = InMemoryInitialContext.getInstance();
        Environment environment = new SystemEnvironmentVariables();
        NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);
        InMemoryBeanLoader loader = new InMemoryBeanLoader(environment, initialContext, resolver);
        loader.init();

        TimeSlot timeSlot = Slot.from(today, Duration.ofHours(1));

        Appointment appointment = controller.createAppointment(clinicianId, patientId, timeSlot);
        log.info("New Appointment created: {}", appointment);

        return appointment;
    }
}
