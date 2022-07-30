package br.com.ead.home.services.delegates.beans;

import br.com.ead.home.configurations.system.MockSystemClockProvider;
import br.com.ead.home.repositories.implementations.MockAppointmentRepository;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.implementations.AppointmentService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ScheduleBeanFactory {

    public static ScheduleService creatUnitTest() {
        return new AppointmentService(new MockAppointmentRepository(new MockSystemClockProvider()));
    }
}
