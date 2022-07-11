package br.com.ead.home.models;

import br.com.ead.home.models.api.TimeSlot;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.ZonedDateTime;

public record Slot(ZonedDateTime start, ZonedDateTime end) implements TimeSlot {

  public Slot {
    Preconditions.checkNotNull(start, "Start time is mandatory");
    Preconditions.checkNotNull(start, "End time is mandatory");
    Preconditions.checkState(start.isBefore(end) , "Start time must be before End time");
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Slot)) {
      return false;
    }

    Slot slot = (Slot) o;

    return new EqualsBuilder()
            .append(start, slot.start)
            .append(end, slot.end)
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(start)
            .append(end)
            .toHashCode();
  }
}
