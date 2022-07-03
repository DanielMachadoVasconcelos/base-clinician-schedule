package br.com.ead.home.model;

import com.google.common.base.MoreObjects;

public record Appointment(String clinicianId, TimeSlot timeSlot) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("clinicianId", clinicianId)
                .add("timeSlot", timeSlot)
                .toString();
    }
}
