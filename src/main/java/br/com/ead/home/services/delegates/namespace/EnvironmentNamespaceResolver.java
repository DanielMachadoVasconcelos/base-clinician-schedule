package br.com.ead.home.services.delegates.namespace;

import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;

import java.util.Locale;

public class EnvironmentNamespaceResolver implements NamespaceResolver {

    private static final String DEFAULT_CONTEXT = "/context/beans/%s/%s/%s";

    @Override
    public String resolve(ServiceStageType stageType, ServicePartitionType partitionType, String beanName) {
        return DEFAULT_CONTEXT.formatted(
                stageType.name().toLowerCase(Locale.ROOT),
                partitionType.name().toLowerCase(Locale.ROOT),
                beanName);
    }
}
