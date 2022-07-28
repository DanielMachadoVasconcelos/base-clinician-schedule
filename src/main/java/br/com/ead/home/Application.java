package br.com.ead.home;

import br.com.ead.home.controllers.ScheduleAvailabilityController;
import br.com.ead.home.models.primitives.ClinicianId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.ZonedDateTime;

public class Application {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class.getPackageName());
        ScheduleAvailabilityController controller = applicationContext.getBean(ScheduleAvailabilityController.class);

        ZonedDateTime until = ZonedDateTime.now();
        ZonedDateTime from = ZonedDateTime.now().minusDays(10);
        ClinicianId clinicianId = new ClinicianId("Thomas");

        System.out.println(controller.getBookableAvailabilities(clinicianId, from, until));
    }
}
