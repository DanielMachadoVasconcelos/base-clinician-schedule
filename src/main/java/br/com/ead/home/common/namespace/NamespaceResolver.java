package br.com.ead.home.common.namespace;

import br.com.ead.home.types.ServicePartitionType;
import br.com.ead.home.types.ServiceStageType;

public interface NamespaceResolver {

    String resolve(ServiceStageType stageType, ServicePartitionType partitionType, String serviceName);
}
