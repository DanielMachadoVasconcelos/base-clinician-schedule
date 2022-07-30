package br.com.ead.home.common.namespace;

import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;

public interface NamespaceResolver {

    String namespace(StageType stageType, PartitionType partitionType, String serviceName);
}
