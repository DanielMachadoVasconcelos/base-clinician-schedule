package br.com.ead.home.common.loader;

import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.injectables.Bean;
import br.com.ead.home.common.injectables.Repository;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.system.ClockProvider;
import br.com.ead.home.configurations.system.MockSystemClockProvider;
import br.com.ead.home.controllers.AppointmentController;
import br.com.ead.home.controllers.ScheduleAvailabilityController;
import br.com.ead.home.controllers.ShiftController;
import br.com.ead.home.repositories.AppointmentRepository;
import br.com.ead.home.repositories.ClinicianPreferencesRepository;
import br.com.ead.home.repositories.ShiftRepository;
import br.com.ead.home.repositories.producers.RepositoryProducer;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.ScheduleAvailabilityService;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.implementations.AppointmentService;
import br.com.ead.home.services.implementations.AvailabilityService;
import br.com.ead.home.services.implementations.ClinicianPreferencesService;
import br.com.ead.home.services.implementations.ClinicianWorkScheduleService;
import lombok.extern.log4j.Log4j2;

import java.time.Clock;

@Log4j2
public record InMemoryBeanLoader(Environment environment,
                                 InitialContext initialContext,
                                 NamespaceResolver resolver) implements BeanLoader {

    @Override
    public void refresh() {
        init();
    }

    @Override
    public void init() {
        // Components
        ClockProvider clockProvider = bindSystemClock();

        //Repositories
        AppointmentRepository appointmentRepository = bindRepository(AppointmentRepository.class, clockProvider);
        ClinicianPreferencesRepository clinicianPreferencesRepository = bindRepository(ClinicianPreferencesRepository.class, clockProvider);
        ShiftRepository shiftRepository = bindRepository(ShiftRepository.class, clockProvider);

        // Services
        ScheduleService scheduleService =
                bindBean(new AppointmentService(appointmentRepository));
        BookablePreferenceService bookablePreferenceService =
                bindBean(new ClinicianPreferencesService(clinicianPreferencesRepository));
        WorkScheduleService workScheduleService =
                bindBean(new ClinicianWorkScheduleService(shiftRepository));
        ScheduleAvailabilityService scheduleAvailabilityService =
                bindBean(new AvailabilityService(scheduleService, workScheduleService, bookablePreferenceService));

        // Controllers
        ShiftController shiftController =
                bindBean(new ShiftController(workScheduleService));
        AppointmentController appointmentController =
                bindBean(new AppointmentController(scheduleService));
        ScheduleAvailabilityController scheduleAvailabilityController =
                bindBean(new ScheduleAvailabilityController(scheduleAvailabilityService));
    }

    private <T extends Bean> T bindBean(T bean) {
        String jndi = getJndiName(bean.getClass().getName());
        initialContext.bind(jndi, bean);
        log.info("{} bind with the name: {}", bean.getClass().getName(), jndi);
        return bean;
    }

    private String getJndiName(String beanName) {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();
        return resolver.namespace(stage, partition, beanName);
    }

    private  <T extends Repository> T bindRepository(Class<T> clazz, ClockProvider clockProvider) {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        String jndiName = getJndiName(clazz.getName());
        T repository = (T) RepositoryProducer.getFactory(clazz).getRepository(stage, partition).apply(clockProvider);

        initialContext.bind(jndiName, repository);
        log.info("{} bind with the name: {}", clazz.getName(), jndiName);

        return repository;
    }

    public  ClockProvider bindSystemClock() {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        ClockProvider clockProvider = newClockProvider(stage);

        String className = ClockProvider.class.getName();
        String jndiName = getJndiName(className);
        initialContext.bind(jndiName, clockProvider);

        log.info("{} bind with the name: {}", className, jndiName);

        return clockProvider;
    }

    private  ClockProvider newClockProvider(StageType stage) {
        return switch (stage) {
            case UNIT_TEST, INTEGRATION_TEST, END_TO_END_TEST -> new MockSystemClockProvider();
            default -> Clock::systemUTC;
        };
    }
}
