package br.com.ead.home.services.delegates.register;

import br.com.ead.home.services.api.Service;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;

import java.util.function.Supplier;

public interface Register<T extends Service> {

    T getBean(ServiceStageType stage, ServicePartitionType partition, String beanClass);

    void registerBean(String namespace, Supplier<T> bean);
}
