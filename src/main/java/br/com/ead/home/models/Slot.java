package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Duration;
import java.time.ZonedDateTime;

public record Slot(ZonedDateTime start, ZonedDateTime end) implements TimeSlot {

  public Slot {
    Preconditions.checkNotNull(start, "Start time is mandatory");
    Preconditions.checkNotNull(start, "End time is mandatory");
    Preconditions.checkState(start.isBefore(end) , "Start time must be before End time");
  }

  public static TimeSlot from(ZonedDateTime start, Duration duration) {
    Preconditions.checkNotNull(start, "Start time is mandatory");
    Preconditions.checkNotNull(duration, "Duration is mandatory");
    Preconditions.checkState(!duration.isNegative(), "Duration must be positive");
    return new Slot(start, start.plus(duration));
  }

  public Slot of(ZonedDateTime start, ZonedDateTime end) {
    return new Slot(start, end);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
            .append("start", start)
            .append("end", end)
            .toString();
  }
}
