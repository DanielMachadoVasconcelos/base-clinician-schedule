package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.services.Service;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;

public interface ServiceLookup<T extends Service> {

    T getService(ServiceStageType stage, ServicePartitionType partition);
}
