package br.com.ead.home;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.injectables.Bean;
import br.com.ead.home.common.loader.InMemoryBeanLoader;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.SystemEnvironmentVariables;
import br.com.ead.home.configurations.system.MockSystemClockProvider;
import br.com.ead.home.controllers.AppointmentController;
import br.com.ead.home.controllers.ScheduleAvailabilityController;
import br.com.ead.home.controllers.ShiftController;
import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class MainVerticle extends AbstractVerticle {

    private static final ZonedDateTime today = new MockSystemClockProvider().currentDay();
    private static final ZonedDateTime tomorrow = today.plusDays(1);

    private static InitialContext initialContext = InMemoryInitialContext.getInstance();
    private static Environment environment = new SystemEnvironmentVariables();
    private static NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);
    private static InMemoryBeanLoader loader = new InMemoryBeanLoader(environment, initialContext, resolver);

    static {
        loader.init();
    }

    @Override
    public void start() throws Exception {
        // Create a Router
        Router router = Router.router(vertx);

        router.route()
                .handler(BodyHandler.create())
                .failureHandler(handleFailure());

        // Mount the handler for all incoming requests at every path and HTTP method
        router.post( "/appointments").handler(context -> {
            AppointmentController appointmentController = getAppointmentController();
            Single.fromSupplier(context::getBodyAsJson)
                          .map(json -> {
                              JsonObject bodyAsJson = context.getBodyAsJson();
                              ClinicianId clinicianId = new ClinicianId(bodyAsJson.getString("clinician_id"));
                              PatientId patientId = new PatientId(bodyAsJson.getString("patient_id"));
                              ZonedDateTime start = ZonedDateTime.ofInstant(Instant.ofEpochMilli(bodyAsJson.getInteger("start_at")), ZoneOffset.UTC);
                              Duration duration = Duration.parse(bodyAsJson.getString("duration"));
                              TimeSlot timeSlot = Slot.from(start, duration);
                              Appointment appointment = appointmentController.createAppointment(clinicianId, patientId, timeSlot);
                              return new JsonObject()
                                      .put("clinician_id", appointment.clinicianId())
                                      .put("patient_id", appointment.patientId())
                                      .put("start_at", appointment.timeSlot().start().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                                      .put("duration", appointment.timeSlot().length().toString())
                                      .toBuffer();

                          })
                          .doOnError(error -> log.error("Something went wrong", error))
                          .subscribe(buffer -> context.response().send(buffer),
                                     error -> context.response().send("Error, %s".formatted(error)));
        });

        // Create the HTTP server
        vertx.createHttpServer()
                // Handle every request using the router
                .requestHandler(router)
                // Start listening
                .listen(8888)
                // Print the port
                .onSuccess(server -> log.info("HTTP server started on port " + server.actualPort()));
    }

    private Handler<RoutingContext> handleFailure() {
        return errorContext -> {

            if (errorContext.response().ended()) {
                return;
            }

            log.error("Route Error:", errorContext.failure());
            errorContext.response()
                    .setStatusCode(500)
                    .end(new JsonObject().put("message", "Something went wrong! :(").toBuffer());
        };
    }

    private static AppointmentController getAppointmentController() {
        AppointmentController controller = getBean(AppointmentController.class);
        log.debug("AppointmentController successfully recovery from initial context");
        return controller;
    }

    private static ShiftController getShiftController() {
        ShiftController controller = getBean(ShiftController.class);
        log.debug("ShiftController successfully recovery from initial context");
        return controller;
    }

    private static ScheduleAvailabilityController getScheduleAvailabilityController() {
        ScheduleAvailabilityController controller = getBean(ScheduleAvailabilityController.class);
        log.debug("ScheduleAvailabilityController successfully recovery from initial context");
        return controller;
    }

    private static <T extends Bean> T getBean(Class<T> clazz) {
        String jndiName = resolver.namespace(StageType.UNIT_TEST, PartitionType.SWEDEN, clazz.getName());
        return (T) initialContext.lookup(jndiName);
    }
}
