package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.configurations.MockSystemClockProvider;
import br.com.ead.home.repositories.MockClinicianWorkScheduleRepository;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.implementations.ClinicianWorkScheduleService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class WorkScheduleBeanFactory {

    public static WorkScheduleService creatUnitTest() {
        return new ClinicianWorkScheduleService(new MockClinicianWorkScheduleRepository(new MockSystemClockProvider()));
    }
}
