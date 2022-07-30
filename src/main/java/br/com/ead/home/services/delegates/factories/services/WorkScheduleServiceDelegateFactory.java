package br.com.ead.home.services.delegates.factories.services;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.factories.AbstractServiceFactory;
import br.com.ead.home.common.injectables.Service;
import br.com.ead.home.common.lookups.InMemoryLookup;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.delegates.WorkScheduleDelegate;
import br.com.ead.home.services.delegates.lookups.WorkScheduleServiceLookup;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class WorkScheduleServiceDelegateFactory extends AbstractServiceFactory {

    @Override
    public Service getService(Environment environment) {
        log.debug("Delegating to work schedule service. Using Stage %s and Partition %s bean".formatted(environment.getStage(), environment.getPartition()));
        InMemoryLookup<WorkScheduleService> lookup = new WorkScheduleServiceLookup(environment,
                InMemoryInitialContext.getInstance(),
                new InMemoryNamespaceResolver(environment));
        return new WorkScheduleDelegate(lookup);
    }
}
