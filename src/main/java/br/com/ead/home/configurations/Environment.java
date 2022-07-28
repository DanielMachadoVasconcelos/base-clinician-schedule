package br.com.ead.home.configurations;

import br.com.ead.home.configurations.types.ServicePartitionType;
import br.com.ead.home.configurations.types.ServiceStageType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
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
