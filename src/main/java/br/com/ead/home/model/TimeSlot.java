package br.com.ead.home.model;

import com.google.common.base.MoreObjects;

import java.time.Duration;
import java.time.ZonedDateTime;

public record TimeSlot(ZonedDateTime start, Duration duration) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("start", start)
                .add("duration", duration)
                .toString();
    }
}
