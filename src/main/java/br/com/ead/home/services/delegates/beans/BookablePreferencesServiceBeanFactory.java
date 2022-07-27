package br.com.ead.home.services.delegates.beans;

import br.com.ead.home.configurations.MockSystemClockProvider;
import br.com.ead.home.repositories.MockClinicianPreferencesRepository;
import br.com.ead.home.services.ClinicianPreferencesService;
import br.com.ead.home.services.api.BookablePreferenceService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BookablePreferencesServiceBeanFactory {

    public static BookablePreferenceService creatUnitTest() {
        return new ClinicianPreferencesService(new MockClinicianPreferencesRepository(new MockSystemClockProvider()));
    }
}
