package helpers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeSlotHelper {

    public static ZonedDateTime time(String time) {
        return time(LocalDate.now(), time);
    }

    public static ZonedDateTime time(LocalDate date, String time) {
        LocalTime now = LocalTime.parse(time);
        return ZonedDateTime.of(date, now, ZoneOffset.UTC);
    }
}
