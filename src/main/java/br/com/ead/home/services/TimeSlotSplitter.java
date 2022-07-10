package br.com.ead.home.services;

import br.com.ead.home.models.api.TimeSlot;

import java.util.Set;

public interface TimeSlotSplitter {

    Set<TimeSlot> split(TimeSlot timeSlot);
}
