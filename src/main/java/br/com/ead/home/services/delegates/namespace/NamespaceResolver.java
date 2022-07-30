package br.com.ead.home.services.delegates.namespace;

import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;

public interface NamespaceResolver {

    String resolve(StageType stageType, PartitionType partitionType, String serviceName);
}
