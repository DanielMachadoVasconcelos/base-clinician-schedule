package br.com.ead.home.services.delegates.register;

import br.com.ead.home.services.api.ScheduleService;
import br.com.ead.home.services.delegates.beans.ScheduleBeanFactory;
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
public record ScheduleServiceRegister(NamespaceResolver namespaceResolver) implements Register<ScheduleService> {

    private static final String SCHEDULE_SERVICE_NAME = "schedule_service";
    private static final Map<String, Supplier<ScheduleService>> BEAN_REGISTER = new ConcurrentHashMap<>();

    public ScheduleServiceRegister {
        Arrays.stream(ServicePartitionType.values())
              .map(partition -> namespaceResolver.resolve(UNIT_TEST, partition, SCHEDULE_SERVICE_NAME))
              .forEach(namespace -> registerBean(namespace, ScheduleBeanFactory::creatUnitTest));
    }

    @Override
    public ScheduleService getBean(ServiceStageType stage, ServicePartitionType partition) {
        log.debug("Getting bean from stage {} and partition {}", stage, partition);
        String namespace = namespaceResolver.resolve(stage, partition, SCHEDULE_SERVICE_NAME);
        return Optional.ofNullable(BEAN_REGISTER.get(namespace))
                .map(Supplier::get)
                .orElseThrow(() -> new IllegalArgumentException("No bean registered for the given namespace '%s'".formatted(namespace)));
    }

    @Override
    public void registerBean(String namespace, Supplier<ScheduleService> bean) {
        log.debug("Registering bean on namespace {}", namespace);
        BEAN_REGISTER.put(namespace, bean);
    }
}
