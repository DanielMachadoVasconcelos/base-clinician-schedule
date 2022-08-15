package br.com.ead.home.models.responses;

import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class AppointmentResponse extends JsonObject {

    @JsonProperty("clinician_id")
    private ClinicianId clinicianId;

    @JsonProperty("patient_id")
    private PatientId patientId;

    @JsonProperty("start_at")
    private ZonedDateTime startAt;

    @JsonProperty("duration")
    private Duration duration;
}
