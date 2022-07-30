package br.com.ead.home.common.register;

import br.com.ead.home.common.injectors.Service;
import br.com.ead.home.types.ServicePartitionType;
import br.com.ead.home.types.ServiceStageType;

import java.util.function.Supplier;

public interface Register<T extends Service> {

    T getBean(ServiceStageType stage, ServicePartitionType partition, String beanClass);

    void registerBean(String namespace, Supplier<T> bean);
}
