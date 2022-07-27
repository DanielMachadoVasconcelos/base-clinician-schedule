package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.configurations.Environment;
import br.com.ead.home.services.delegates.BookablePreferenceServiceDelegate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public record BookablePreferenceServiceDelegateFactory(Environment environment) {

    public BookablePreferenceServiceDelegate createBookablePreferenceService() {
        var partition = environment.getPartition();
        var stage = environment.getStage();
        log.debug("Delegating to work schedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return new BookablePreferenceServiceDelegate(stage, partition);
    }
}
