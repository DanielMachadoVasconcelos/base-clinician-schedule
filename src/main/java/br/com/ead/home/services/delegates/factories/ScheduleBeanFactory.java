package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.configurations.MockSystemClockProvider;
import br.com.ead.home.repositories.MockAppointmentRepository;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.implementations.AppointmentService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ScheduleBeanFactory {

    public static ScheduleService creatUnitTest() {
        return new AppointmentService(new MockAppointmentRepository(new MockSystemClockProvider()));
    }
}
