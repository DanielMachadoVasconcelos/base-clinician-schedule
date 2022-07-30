package br.com.ead.home.configurations;

import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public record Environment() {

    public PartitionType getPartition() {
       return Optional.ofNullable(System.getenv("PARTITION"))
                .filter(StringUtils::isNotBlank)
                .map(StringUtils::upperCase)
                .map(PartitionType::valueOf)
                .orElse(PartitionType.SWEDEN);
    }

    public StageType getStage() {
        return Optional.ofNullable(System.getenv("STAGE"))
                .filter(StringUtils::isNotBlank)
                .map(StringUtils::upperCase)
                .map(StageType::valueOf)
                .orElse(StageType.UNIT_TEST);
    }
}
