package br.com.ead.home.services.api;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;

import java.time.ZonedDateTime;
import java.util.Set;

public interface ScheduleAvailabilityService {

    Set<TimeSlot> getBookableAvailabilities(ClinicianId clinicianId,
                                            ZonedDateTime startAt,
                                            ZonedDateTime entAt);
}
