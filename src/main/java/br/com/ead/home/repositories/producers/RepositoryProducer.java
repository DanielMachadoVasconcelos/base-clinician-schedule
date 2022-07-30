package br.com.ead.home.repositories.producers;

import br.com.ead.home.common.factories.AbstractRepositoryFactory;
import br.com.ead.home.common.injectables.Repository;
import br.com.ead.home.repositories.AppointmentRepository;
import br.com.ead.home.repositories.ClinicianPreferencesRepository;
import br.com.ead.home.repositories.ShiftRepository;
import br.com.ead.home.repositories.factories.AppointmentRepositoryFactory;
import br.com.ead.home.repositories.factories.ClinicianPreferencesRepositoryFactory;
import br.com.ead.home.repositories.factories.ShiftRepositoryFactory;
import org.apache.commons.lang3.NotImplementedException;

public class RepositoryProducer {

    public static <T extends Repository> AbstractRepositoryFactory getFactory(Class<T> repository){
        if (AppointmentRepository.class.equals(repository)) {
            return new AppointmentRepositoryFactory();
        }

        if(ClinicianPreferencesRepository.class.equals(repository)){
            return new ClinicianPreferencesRepositoryFactory();
        }

        if (ShiftRepository.class.equals(repository)) {
            return new ShiftRepositoryFactory();
        }

        throw new NotImplementedException("No delegate factory register for repository %s"
                .formatted(repository.getClass().getName()));
    }
}
