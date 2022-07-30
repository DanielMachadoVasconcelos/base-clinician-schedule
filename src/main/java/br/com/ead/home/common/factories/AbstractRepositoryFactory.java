package br.com.ead.home.common.factories;

import br.com.ead.home.common.injectables.Repository;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;

public abstract class AbstractRepositoryFactory {

    public abstract <T extends Repository> T getRepository(StageType stage, PartitionType partition);

}
