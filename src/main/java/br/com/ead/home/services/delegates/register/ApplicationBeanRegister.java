package br.com.ead.home.services.delegates.register;

import br.com.ead.home.common.injectables.Service;
import br.com.ead.home.common.namespace.EnvironmentNamespaceResolver;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Log4j2
public final class ApplicationBeanRegister implements Register<Service> {

    private static final String SCHEDULE_SERVICE_NAME = "schedule_service";
    private static final Map<String, Service> BEAN_REGISTER = new ConcurrentHashMap<>();
    private final NamespaceResolver namespaceResolver;

    private static class InstanceHolder {
        private static final ApplicationBeanRegister INSTANCE = new ApplicationBeanRegister(new EnvironmentNamespaceResolver());
    }

    private ApplicationBeanRegister(NamespaceResolver namespaceResolver) {
        this.namespaceResolver = namespaceResolver;
    }

    public static ApplicationBeanRegister getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Service getBean(StageType stage, PartitionType partition, String beanClass) {
        log.debug("Getting bean {} from stage {} and partition {}", beanClass, stage, partition);
        String namespace = namespaceResolver.resolve(stage, partition, beanClass);
        return Optional.ofNullable(BEAN_REGISTER.get(namespace))
                .orElseThrow(() -> new IllegalArgumentException("No bean registered for the given namespace '%s'".formatted(namespace)));
    }

    @Override
    public void registerBean(String namespace, Supplier<Service> beanProvider) {
        Preconditions.checkNotNull(namespace, "Namespace is mandatory to register a bean");
        Preconditions.checkNotNull(beanProvider, "Bean Provider is mandatory to register a bean");

        if(BEAN_REGISTER.containsKey(namespace)) {
            log.error("Attempt to register duplicated bean namespace {}", namespace);
            throw new IllegalArgumentException("Namespace %s is already register".formatted(namespace));
        }

        Service bean = beanProvider.get();
        log.info("Registering bean {} on namespace {}", bean.getClass().getName(), namespace);
        BEAN_REGISTER.put(namespace, bean);
    }
}
