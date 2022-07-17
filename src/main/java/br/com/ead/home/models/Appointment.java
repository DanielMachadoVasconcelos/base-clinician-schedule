package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;

import java.util.Comparator;

public record Appointment(ClinicianId clinicianId, PatientId patientId, TimeSlot timeSlot) implements Comparable<Appointment> {

    @Override
    public int compareTo(Appointment other) {
        return Comparator.comparing(Appointment::clinicianId)
                         .thenComparing(Appointment::patientId)
                         .thenComparing(Appointment::timeSlot)
                         .compare(this, other);
    }
}
