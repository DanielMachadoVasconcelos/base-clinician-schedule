package br.com.ead.home.services.delegates.beans;

import br.com.ead.home.configurations.MockSystemClockProvider;
import br.com.ead.home.repositories.MockAppointmentRepository;
import br.com.ead.home.services.AppointmentService;
import br.com.ead.home.services.api.ScheduleService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ScheduleBeanFactory {

    public static ScheduleService creatUnitTest() {
        return new AppointmentService(new MockAppointmentRepository(new MockSystemClockProvider()));
    }
}
