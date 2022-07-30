package br.com.ead.home.common.loader;

import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.injectables.Repository;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.system.ClockProvider;
import br.com.ead.home.configurations.system.MockSystemClockProvider;
import br.com.ead.home.controllers.AppointmentController;
import br.com.ead.home.controllers.ScheduleAvailabilityController;
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
        AppointmentRepository appointmentRepository = bindRepository(AppointmentRepository.class);
        ClinicianPreferencesRepository clinicianPreferencesRepository = bindRepository(ClinicianPreferencesRepository.class);
        ShiftRepository shiftRepository = bindRepository(ShiftRepository.class);

        // Services
        ScheduleService scheduleService = bindAppointmentService(appointmentRepository);
        BookablePreferenceService bookablePreferenceService = bindClinicianPreferencesService(clinicianPreferencesRepository);
        WorkScheduleService workScheduleService = bindClinicianWorkScheduleService(shiftRepository);

        ScheduleAvailabilityService scheduleAvailabilityService =
                bindScheduleAvailabilityService(scheduleService, workScheduleService, bookablePreferenceService);

        // Controllers
        AppointmentController appointmentController = bindAppointmentController(scheduleService);
        ScheduleAvailabilityController scheduleAvailabilityController =
                bindScheduleAvailabilityController(scheduleAvailabilityService);
    }

    private  ScheduleAvailabilityController bindScheduleAvailabilityController(ScheduleAvailabilityService service) {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        ScheduleAvailabilityController controller = new ScheduleAvailabilityController(service);
        String className = ScheduleAvailabilityController.class.getName();
        String jndi = resolver.namespace(stage, partition, className);
        initialContext.bind(jndi, controller);

        log.info("{} bind with the name: {}", className, jndi);
        return controller;
    }

    private  AppointmentController bindAppointmentController(ScheduleService service) {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        AppointmentController controller = new AppointmentController(service);
        String className = AppointmentController.class.getName();
        String jndi = resolver.namespace(stage, partition, className);
        initialContext.bind(jndi, controller);

        log.info("{} bind with the name: {}", className, jndi);
        return controller;
    }

    private  ScheduleAvailabilityService bindScheduleAvailabilityService(ScheduleService scheduleService,
                                                                               WorkScheduleService shiftService,
                                                                               BookablePreferenceService clinicianPreferences) {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        ScheduleAvailabilityService service = new AvailabilityService(scheduleService, shiftService, clinicianPreferences);
        String className = ScheduleAvailabilityService.class.getName();
        String jndi = resolver.namespace(stage, partition, className);
        initialContext.bind(jndi, service);

        log.info("{} bind with the name: {}", className, jndi);
        return service;
    }

    private  ScheduleService bindAppointmentService(AppointmentRepository repository) {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        ScheduleService service = new AppointmentService(repository);
        String className = ScheduleService.class.getName();
        String jndi = resolver.namespace(stage, partition, className);
        initialContext.bind(jndi, service);

        log.info("{} bind with the name: {}", className, jndi);
        return service;
    }

    private  BookablePreferenceService bindClinicianPreferencesService(ClinicianPreferencesRepository repository) {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        BookablePreferenceService service = new ClinicianPreferencesService(repository);
        String className = BookablePreferenceService.class.getName();
        String jndi = resolver.namespace(stage, partition, className);
        initialContext.bind(jndi, service);

        log.info("{} bind with the name: {}", className, jndi);
        return service;
    }

    private  WorkScheduleService bindClinicianWorkScheduleService(ShiftRepository repository) {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        WorkScheduleService service = new ClinicianWorkScheduleService(repository);
        String className = WorkScheduleService.class.getName();
        String jndi = resolver.namespace(stage, partition, className);
        initialContext.bind(jndi, service);

        log.info("{} bind with the name: {}", className, jndi);
        return service;
    }

    private  <T extends Repository> T bindRepository(Class<T> clazz) {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        String jndiName = resolver.namespace(stage, partition, clazz.getName());
        T repository = RepositoryProducer.getFactory(clazz).getRepository(stage, partition);

        initialContext.bind(jndiName, repository);
        log.info("{} bind with the name: {}", clazz.getName(), jndiName);

        return repository;
    }

    public  ClockProvider bindSystemClock() {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        ClockProvider clockProvider = newClockProvider(stage);

        String className = ClockProvider.class.getName();
        String jndi = resolver.namespace(stage, partition, className);
        initialContext.bind(jndi, clockProvider);

        log.info("{} bind with the name: {}", className, jndi);

        return clockProvider;
    }

    private  ClockProvider newClockProvider(StageType stage) {
        return switch (stage) {
            case UNIT_TEST, INTEGRATION_TEST, END_TO_END_TEST -> new MockSystemClockProvider();
            default -> Clock::systemUTC;
        };
    }
}
