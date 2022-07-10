package br.com.ead.home.services;

import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Set;

import static helpers.TimeSlotHelper.time;

class DoctorScheduleConfigurationTest {

    @Test
    void shouldReturnTheDoctorConfigurationSchedule() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DoctorScheduleConfiguration configuration = new DoctorScheduleConfiguration(
                Duration.ofHours(1),
                Duration.ofMinutes(15),
                Duration.ofHours(4),
                6L,
                ZoneOffset.UTC);

        Slot shift = new Slot(time(tomorrow, "08:00"), time(tomorrow, "18:00"));
        Set<TimeSlot> availableSlots = configuration.split(shift);

        Assertions.assertNotNull(availableSlots, "Must not return null");
        Assertions.assertEquals(6, availableSlots.size(), "Must return at maximum 6 slots");
    }
}