package br.com.ead.home;

import br.com.ead.home.model.Schedule;
import br.com.ead.home.model.Shift;
import br.com.ead.home.model.TimeSlot;
import io.reactivex.rxjava3.core.Flowable;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {

    public static void main(String[] args) {
        Duration duration = Duration.ofHours(1);
        Flowable.fromStream(generator(duration))
                .map(partition -> new TimeSlot(partition, duration))
                .map(timeSlot -> new Shift(generateClinicianId(), timeSlot))
                .groupBy(Shift::clinicianId, shift -> new Schedule(shift.clinicianId(), Set.of(shift.timeSlot()), Set.of()))
                .flatMapSingle(groupByClinician -> groupByClinician.collect(Collectors.reducing(Schedule::merge)))
                .flatMap(Flowable::fromOptional)
                .subscribe(item -> System.out.println(String.format("Item: %s", item)),
                           error -> System.err.println(String.format("Error: %s", error)),
                           () -> System.out.println("Completed!"));
    }

    private static String generateClinicianId() {
        return new Random().nextBoolean() ? "Thomas" : "Jakob";
    }

    private static Stream<ZonedDateTime> generator(Duration duration) {
        ZoneId zone = ZoneId.of("Europe/Stockholm");
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.of(12, 00);

        ZonedDateTime startAt = ZonedDateTime.of(today, now, zone).truncatedTo(ChronoUnit.MINUTES);
        ZonedDateTime endsAt = startAt.plusDays(1);

        return Stream.iterate(startAt,
                        slot -> slot.isBefore(endsAt),
                        slot -> slot.plus(duration))
                ;
    }
}
