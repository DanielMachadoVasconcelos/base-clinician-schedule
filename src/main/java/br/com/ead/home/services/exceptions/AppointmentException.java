package br.com.ead.home.services.exceptions;

import br.com.ead.home.services.validations.AppointmentErrorCodes;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;

@Getter
public class AppointmentException extends BusinessException {

    private Set<AppointmentErrorCodes> errorCodes;

    public AppointmentException(String message) {
        super(message);
        this.errorCodes = Sets.newHashSet();
    }

    public AppointmentException(String message, Set<AppointmentErrorCodes> errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }

    public static AppointmentException addErrorCode(AppointmentException exception, AppointmentErrorCodes error) {
        exception.errorCodes.add(error);
        return exception;
    }

    public static AppointmentException merge(AppointmentException acc, AppointmentException next) {
        Set<AppointmentErrorCodes> mergedErrorCodes = Sets.union(acc.errorCodes, next.errorCodes);
        return new AppointmentException(acc.getMessage(), mergedErrorCodes);
    }

    public boolean hasErrors() {
        return CollectionUtils.isNotEmpty(errorCodes);
    }
}
