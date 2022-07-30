package br.com.ead.home.services.delegates.factories;

import br.com.ead.home.configurations.MockSystemClockProvider;
import br.com.ead.home.repositories.MockClinicianPreferencesRepository;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.implementations.ClinicianPreferencesService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BookablePreferencesServiceBeanFactory {

    public static BookablePreferenceService creatUnitTest() {
        return new ClinicianPreferencesService(new MockClinicianPreferencesRepository(new MockSystemClockProvider()));
    }
}
