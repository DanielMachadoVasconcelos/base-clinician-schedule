package br.com.ead.home.common.lookups;

import br.com.ead.home.common.injectors.Bean;
import br.com.ead.home.types.ServicePartitionType;
import br.com.ead.home.types.ServiceStageType;

public interface ServiceLookup<T extends Bean> {

    T getService(ServiceStageType stage, ServicePartitionType partition);
}
