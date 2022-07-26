package br.com.ead.home.services.delegates.namespace;

import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;

public interface NamespaceResolver {

    String resolve(ServiceStageType stageType, ServicePartitionType partitionType, String serviceName);
}
