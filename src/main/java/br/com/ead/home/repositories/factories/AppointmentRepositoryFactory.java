package br.com.ead.home.repositories.factories;

import br.com.ead.home.common.factories.AbstractRepositoryFactory;
import br.com.ead.home.common.injectables.Repository;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.configurations.system.ClockProvider;
import br.com.ead.home.repositories.implementations.MockAppointmentRepository;

import java.util.function.Function;

public class AppointmentRepositoryFactory extends AbstractRepositoryFactory {

    @Override
    public Function<ClockProvider, Repository> getRepository(StageType stage, PartitionType partition) {
        return switch (stage) {
            case UNIT_TEST, INTEGRATION_TEST, END_TO_END_TEST -> clockProvider -> new MockAppointmentRepository(clockProvider);
            default -> throw new IllegalArgumentException("No AppointmentRepository implementation for stage %s and partition %s".formatted(stage, partition));
        };
    }
}
