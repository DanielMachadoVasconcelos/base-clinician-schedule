package br.com.ead.home.services.delegates.factories.producers;

import br.com.ead.home.common.factories.AbstractServiceFactory;
import br.com.ead.home.common.injectables.Service;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.delegates.factories.services.BookablePreferenceServiceDelegateFactory;
import br.com.ead.home.services.delegates.factories.services.ScheduleServiceDelegateFactory;
import br.com.ead.home.services.delegates.factories.services.WorkScheduleServiceDelegateFactory;
import org.apache.commons.lang3.NotImplementedException;

public class ServiceDelegateProducer {

    public static <T extends Service> AbstractServiceFactory getFactory(Class<T> service) {

        if (BookablePreferenceService.class.equals(service)) {
            return new BookablePreferenceServiceDelegateFactory();
        }

        if(ScheduleService.class.equals(service)){
            return new ScheduleServiceDelegateFactory();
        }

        if (WorkScheduleService.class.equals(service)) {
            return new WorkScheduleServiceDelegateFactory();
        }

        throw new NotImplementedException("No delegate factory register for service %s".formatted(service.getName()));
    }
}
