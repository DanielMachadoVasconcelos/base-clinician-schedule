package br.com.ead.home.services.api;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.Service;

import java.time.ZonedDateTime;
import java.util.Set;

public interface ScheduleAvailabilityService extends Service {

    Set<TimeSlot> getBookableAvailabilities(ClinicianId clinicianId,
                                            ZonedDateTime startAt,
                                            ZonedDateTime entAt);
}
