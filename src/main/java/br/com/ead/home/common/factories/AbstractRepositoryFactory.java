package br.com.ead.home.common.factories;

import br.com.ead.home.common.injectables.Repository;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.configurations.system.ClockProvider;

import java.util.function.Function;

public abstract class AbstractRepositoryFactory {

    public abstract <T extends Repository> Function<ClockProvider, Repository> getRepository(StageType stage, PartitionType partition);

}
