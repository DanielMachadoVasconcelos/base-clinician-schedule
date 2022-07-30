package br.com.ead.home.configurations;

import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;

public interface Environment {

    PartitionType getPartition();

    StageType getStage();
}
