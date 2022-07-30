package br.com.ead.home.common.namespace;

import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;

public interface NamespaceResolver {

    String resolve(StageType stageType, PartitionType partitionType, String serviceName);
}
