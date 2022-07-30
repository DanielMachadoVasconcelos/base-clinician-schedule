package br.com.ead.home.services.delegates.register;

import br.com.ead.home.common.injectables.Service;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;

import java.util.function.Supplier;

public interface Register<T extends Service> {

    T getBean(StageType stage, PartitionType partition, String beanClass);

    void registerBean(String namespace, Supplier<T> bean);
}
