package br.com.ead.home.common.factories;

import br.com.ead.home.common.injectables.Controller;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;

public abstract class AbstractControllerFactory {

    public abstract Controller getService(StageType stage, PartitionType partition);
}
