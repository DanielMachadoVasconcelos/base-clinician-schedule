package br.com.ead.home.models.responses;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.key.ZonedDateTimeKeyDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AppointmentResponse {

    @JsonProperty("clinician_id")
    ClinicianId clinicianId;

    @JsonProperty("patient_id")
    PatientId patientId;

    @JsonProperty("start_at")
    @JsonDeserialize(keyAs = ZonedDateTimeKeyDeserializer.class)
    ZonedDateTime startAt;

    @JsonProperty("duration")
    @JsonDeserialize(contentAs = DurationDeserializer.class)
    Duration duration;

    public static AppointmentResponse from(Appointment appointment){
        return new AppointmentResponse(
                appointment.clinicianId(),
                appointment.patientId(),
                appointment.timeSlot().start(),
                appointment.timeSlot().length());
    }
}
