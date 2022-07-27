package br.com.ead.home.configurations;

import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class Environment {

    public ServicePartitionType getPartition() {
       return Optional.ofNullable(System.getenv("PARTITION"))
                .filter(StringUtils::isNotBlank)
                .map(StringUtils::upperCase)
                .map(ServicePartitionType::valueOf)
                .orElse(ServicePartitionType.SWEDEN);
    }

    public ServiceStageType getStage() {
        return Optional.ofNullable(System.getenv("STAGE"))
                .filter(StringUtils::isNotBlank)
                .map(StringUtils::upperCase)
                .map(ServiceStageType::valueOf)
                .orElse(ServiceStageType.UNIT_TEST);
    }
}
