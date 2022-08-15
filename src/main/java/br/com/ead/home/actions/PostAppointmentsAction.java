package br.com.ead.home.actions;

import br.com.ead.home.common.context.InMemoryInitialContext;
import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.injectables.Bean;
import br.com.ead.home.common.namespace.InMemoryNamespaceResolver;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.configurations.SystemEnvironmentVariables;
import br.com.ead.home.controllers.AppointmentController;
import br.com.ead.home.controllers.ScheduleAvailabilityController;
import br.com.ead.home.controllers.ShiftController;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.request.AppointmentRequest;
import br.com.ead.home.models.responses.AppointmentResponse;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class PostAppointmentsAction implements Handler<RoutingContext> {

    private static InitialContext initialContext = InMemoryInitialContext.getInstance();
    private static Environment environment = new SystemEnvironmentVariables();
    private static NamespaceResolver resolver = new InMemoryNamespaceResolver(environment);

    @Override
    public void handle(RoutingContext routingContext) {
        AppointmentController appointmentController = getAppointmentController();
        Single.fromSupplier(routingContext::body)
                .map(body -> body.asPojo(AppointmentRequest.class))
                .map(request -> appointmentController.createAppointment(request.getClinicianId(), request.getPatientId(), Slot.from(request.getStartAt(), request.getDuration())))
                .map(appointment -> new AppointmentResponse(appointment.clinicianId(), appointment.patientId(), appointment.timeSlot().start(), appointment.timeSlot().length()))
                .doOnError(error -> log.error("Something went wrong", error))
                .doOnSuccess(response -> log.info("Appointment booked={}", response))
                .subscribe(
                        buffer -> routingContext.response()
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .send(Json.encodePrettily(buffer)),
                        error -> routingContext.response()
                                .setStatusCode(500)
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(new JsonObject()
                                        .put("title", "Something went wrong! 🤔")
                                        .put("message", error.getMessage())
                                        .toBuffer()));
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
        String jndiName = resolver.namespace(environment.getStage(), environment.getPartition(), clazz.getName());
        return (T) initialContext.lookup(jndiName);
    }
}
