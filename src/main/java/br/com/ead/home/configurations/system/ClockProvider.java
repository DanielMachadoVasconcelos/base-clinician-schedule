package br.com.ead.home.configurations.system;

import br.com.ead.home.common.injectables.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@FunctionalInterface
public interface ClockProvider extends Component {

    Clock currentSystemClock();

    default ZonedDateTime currentDay(){
        return ZonedDateTime.now(currentSystemClock());
    }

    default ZonedDateTime time(LocalDate date, String time) {
        LocalTime now = LocalTime.parse(time);
        return ZonedDateTime.of(date, now, ZoneOffset.UTC);
    }

    default ZonedDateTime time(String time) {
        return time(LocalDate.now(currentSystemClock()), time);
    }
}
