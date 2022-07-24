package br.com.ead.home.models.api;

import java.util.Set;

public interface TimeSlotPreferences {

    Set<TimeSlot> slice(TimeSlot timeSlot);
}
