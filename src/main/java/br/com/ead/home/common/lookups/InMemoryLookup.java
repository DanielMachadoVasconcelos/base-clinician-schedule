package br.com.ead.home.common.lookups;

import br.com.ead.home.common.injectables.Bean;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;

public interface InMemoryLookup<T extends Bean> {

    T lookup(StageType stage, PartitionType partition);
}
