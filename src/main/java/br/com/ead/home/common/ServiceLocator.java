package br.com.ead.home.common;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.injectors.Bean;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.ScheduleService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public record ServiceLocator() {

    private static InitialContext inMemoryInitialContext = new InMemoryInitialContext(new Environment());

    public static <T extends Bean> T getBean(Class<T> bean) {
        return inMemoryInitialContext.lookup(bean);
    }

    public static <T extends Bean> void bind(T bean) {
        inMemoryInitialContext.bind(bean);
    }

    public static void main(String[] args) {
        ScheduleService scheduleService = ServiceLocator.getBean(ScheduleService.class);
        System.out.println("Bean locator: %s".formatted(scheduleService));
        System.out.println("Bean locator: %s".formatted(scheduleService.findAllByClinicianId(new ClinicianId("Thomas")).size()));
    }
}
