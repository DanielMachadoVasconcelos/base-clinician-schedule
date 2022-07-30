package br.com.ead.home.services.delegates.namespace;

import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import org.apache.commons.lang3.RegExUtils;

import java.util.Locale;

public class EnvironmentNamespaceResolver implements NamespaceResolver {

    private static final String DEFAULT_CONTEXT = "/context/beans/%s/%s/%s";

    @Override
    public String resolve(StageType stageType, PartitionType partitionType, String beanName) {
        return DEFAULT_CONTEXT.formatted(
                stageType.name().toLowerCase(Locale.ROOT),
                partitionType.name().toLowerCase(Locale.ROOT),
                RegExUtils.replaceAll(beanName, "\\.", "/"));
    }
}
