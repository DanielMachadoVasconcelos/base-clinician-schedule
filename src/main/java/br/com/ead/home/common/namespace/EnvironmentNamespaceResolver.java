package br.com.ead.home.common.namespace;

import br.com.ead.home.types.ServicePartitionType;
import br.com.ead.home.types.ServiceStageType;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class EnvironmentNamespaceResolver implements NamespaceResolver {

    private static final String DEFAULT_CONTEXT = "/context/beans/%s/%s/%s";

    @Override
    public String resolve(ServiceStageType stageType, ServicePartitionType partitionType, String beanName) {
        Preconditions.checkNotNull(stageType, "Stage is mandatory");
        Preconditions.checkNotNull(stageType, "Partition is mandatory");
        Preconditions.checkNotNull(stageType, "Bean name is mandatory");
        Preconditions.checkState(StringUtils.isNotBlank(beanName), "Bean name is mandatory");
        return DEFAULT_CONTEXT.formatted(
                stageType.name().toLowerCase(Locale.ROOT),
                partitionType.name().toLowerCase(Locale.ROOT),
                RegExUtils.replaceAll(beanName, "\\.", "/"));
    }
}
