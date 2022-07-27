package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.configurations.Environment;
import br.com.ead.home.services.api.BookablePreferenceService;
import br.com.ead.home.services.api.ScheduleService;
import br.com.ead.home.services.api.WorkScheduleService;

public final class ApplicationDelegateFactory {

    private ApplicationDelegateFactory() {
    }

    private static final Environment environment = new Environment();
    private static final ScheduleServiceDelegateFactory scheduleService = new ScheduleServiceDelegateFactory(environment);
    private static final WorkScheduleServiceDelegateFactory shiftService = new WorkScheduleServiceDelegateFactory(environment);
    private static final BookablePreferenceServiceDelegateFactory clinicianPreferencesService = new BookablePreferenceServiceDelegateFactory(environment);

    public static ScheduleService scheduleService() {
        return scheduleService.createScheduleService();
    }

    public static WorkScheduleService workScheduleService() {
        return shiftService.createWorkScheduleService();
    }

    public static BookablePreferenceService bookablePreferenceService() {
        return clinicianPreferencesService.createBookablePreferenceService();
    }
}
