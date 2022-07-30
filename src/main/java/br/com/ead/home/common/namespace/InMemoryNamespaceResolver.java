package br.com.ead.home.common.namespace;

import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.configurations.Environment;
import org.apache.commons.lang3.RegExUtils;

import java.util.Locale;

public record InMemoryNamespaceResolver(Environment environment) implements NamespaceResolver {

    private static final String DEFAULT_CONTEXT = "java:beans/stage/%s/partition/%s/%s";

    @Override
    public String namespace(StageType stageType, PartitionType partitionType, String beanName) {
        return DEFAULT_CONTEXT.formatted(
                environment.getStage().name().toLowerCase(Locale.ROOT),
                environment.getPartition().name().toLowerCase(Locale.ROOT),
                RegExUtils.replaceAll(beanName, "\\.", "/"));
    }
}
