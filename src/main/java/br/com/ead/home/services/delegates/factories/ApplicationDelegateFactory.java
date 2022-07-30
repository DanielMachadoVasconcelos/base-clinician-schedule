package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.configurations.Environment;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.delegates.factories.producers.ServiceDelegateProducer;

public final class ApplicationDelegateFactory {

    private final Environment environment;

    public ApplicationDelegateFactory(Environment environment) {
        this.environment = environment;
    }

    public ScheduleService scheduleService() {
        return (ScheduleService) ServiceDelegateProducer.getFactory(ScheduleService.class)
                            .getService(environment);
    }

    public WorkScheduleService workScheduleService() {
        return (WorkScheduleService) ServiceDelegateProducer.getFactory(WorkScheduleService.class)
                .getService(environment);
    }

    public BookablePreferenceService bookablePreferenceService() {
        return (BookablePreferenceService) ServiceDelegateProducer.getFactory(BookablePreferenceService.class)
                .getService(environment);
    }
}
