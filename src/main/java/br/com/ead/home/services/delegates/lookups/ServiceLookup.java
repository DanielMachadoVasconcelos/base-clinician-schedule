package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.common.injectables.Service;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;

public interface ServiceLookup<T extends Service> {

    T getService(StageType stage, PartitionType partition);
}
