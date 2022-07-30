package br.com.ead.home.common.context;

import br.com.ead.home.common.caches.BeanCache;
import br.com.ead.home.common.caches.InMemoryBeanCache;
import br.com.ead.home.common.injectors.Bean;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.MockSystemClockProvider;
import br.com.ead.home.repositories.MockAppointmentRepository;
import br.com.ead.home.repositories.MockClinicianPreferencesRepository;
import br.com.ead.home.repositories.MockClinicianWorkScheduleRepository;
import br.com.ead.home.services.delegates.BookablePreferenceServiceDelegate;
import br.com.ead.home.services.delegates.ScheduleServiceDelegate;
import br.com.ead.home.services.delegates.WorkScheduleDelegate;
import br.com.ead.home.types.ServicePartitionType;
import br.com.ead.home.types.ServiceStageType;
import com.google.common.base.Preconditions;

public record InMemoryInitialContext(Environment environment) implements InitialContext {

    private static final BeanCache cache = new InMemoryBeanCache();

    public InMemoryInitialContext {
        cache.putBean(new MockSystemClockProvider());
        cache.putBean(new MockAppointmentRepository(cache.getBean(MockSystemClockProvider.class.getName())));
        cache.putBean(new MockClinicianPreferencesRepository(cache.getBean(MockSystemClockProvider.class.getName())));
        cache.putBean(new MockClinicianWorkScheduleRepository(cache.getBean(MockSystemClockProvider.class.getName())));

        ServiceStageType stage = environment.getStage();
        ServicePartitionType partition = environment.getPartition();

        cache.putBean(new ScheduleServiceDelegate(stage, partition));
        cache.putBean(new WorkScheduleDelegate(stage, partition));
        cache.putBean(new BookablePreferenceServiceDelegate(stage, partition));
    }

    public <T extends Bean> T lookup(Class<T> bean) {
        Preconditions.checkNotNull(bean, "Bean class is mandatory");
        return (T) cache.getBean(bean.getName());
    }

    public <T extends Bean> void bind(T bean) {
        Preconditions.checkNotNull(bean, "Bean to bind is mandatory");
        cache.putBean(bean);
    }
}
