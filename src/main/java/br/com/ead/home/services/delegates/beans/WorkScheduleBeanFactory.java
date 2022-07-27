package br.com.ead.home.services.delegates.beans;

import br.com.ead.home.configurations.MockSystemClockProvider;
import br.com.ead.home.repositories.MockClinicianWorkScheduleRepository;
import br.com.ead.home.services.ClinicianWorkScheduleService;
import br.com.ead.home.services.api.WorkScheduleService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class WorkScheduleBeanFactory {

    public static WorkScheduleService creatUnitTest() {
        return new ClinicianWorkScheduleService(new MockClinicianWorkScheduleRepository(new MockSystemClockProvider()));
    }
}
