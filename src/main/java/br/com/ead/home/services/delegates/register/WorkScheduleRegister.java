package br.com.ead.home.services.delegates.register;

import br.com.ead.home.services.api.WorkScheduleService;
import br.com.ead.home.services.delegates.beans.WorkScheduleBeanFactory;
import br.com.ead.home.services.delegates.namespace.NamespaceResolver;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static br.com.ead.home.services.delegates.types.ServiceStageType.UNIT_TEST;

@Log4j2
public record WorkScheduleRegister(NamespaceResolver namespaceResolver) implements Register<WorkScheduleService> {

    private static final String WORK_SCHEDULE_SERVICE_NAME = "work_schedule_service";
    private static final Map<String, Supplier<WorkScheduleService>> BEAN_REGISTER = new ConcurrentHashMap<>();

    public WorkScheduleRegister {
        Arrays.stream(ServicePartitionType.values())
                .map(partition -> namespaceResolver.resolve(UNIT_TEST, partition, WORK_SCHEDULE_SERVICE_NAME))
                .forEach(namespace -> registerBean(namespace, WorkScheduleBeanFactory::creatUnitTest));
    }

    @Override
    public WorkScheduleService getBean(ServiceStageType stage, ServicePartitionType partition) {
        log.debug("Getting bean from stage {} and partition {}", stage, partition);
        String namespace = namespaceResolver.resolve(stage, partition, WORK_SCHEDULE_SERVICE_NAME);
        return Optional.ofNullable(BEAN_REGISTER.get(namespace))
                .map(Supplier::get)
                .orElseThrow(() -> new IllegalArgumentException("No bean registered for the given namespace '%s'".formatted(namespace)));
    }

    @Override
    public void registerBean(String namespace, Supplier<WorkScheduleService> bean) {
        log.debug("Registering bean on namespace {}", namespace);
        BEAN_REGISTER.put(namespace, bean);
    }
}
