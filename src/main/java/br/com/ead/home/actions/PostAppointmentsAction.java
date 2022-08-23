package br.com.ead.home.actions;

import br.com.ead.home.common.factories.ApplicationBeanFactory;
import br.com.ead.home.controllers.AppointmentController;
import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.request.AppointmentRequest;
import br.com.ead.home.models.responses.AppointmentResponse;
import br.com.ead.home.services.exceptions.AppointmentException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;

@Log4j2
public class PostAppointmentsAction implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        AppointmentController appointmentController = getAppointmentController();
        Single.fromSupplier(routingContext::body)
                .map(body -> body.asPojo(AppointmentRequest.class))
                .map(request -> new Appointment(request.getClinicianId(), request.getPatientId(), Slot.from(request.getStartAt(), request.getDuration())))
                .map(appointment -> appointmentController.createAppointment(appointment))
                .map(AppointmentResponse::from)
                .doOnError(error -> log.error("There was an error processing the request. The appointment was not booked.", error))
                .doOnSuccess(response -> log.info("Appointment successfully booked. Appointment={}", response))
                .map(Json::encodePrettily)
                .subscribe(response -> onSuccess(routingContext, response),
                           error -> onError(routingContext, error));
    }

    private void onError(RoutingContext routingContext, Throwable error) {

        int statusCode = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
        Buffer body = new JsonObject()
                .put("title", "The appointment was not booked")
                .put("message", "There was an unknown error while processing the request")
                .put("message", error.getMessage())
                .toBuffer();

        if (error instanceof AppointmentException exception){
            statusCode = HttpResponseStatus.BAD_REQUEST.code();
            body = new JsonObject()
                    .put("title", "The appointment was not booked")
                    .put("message", exception.getMessage())
                    .put("validations", new JsonArray(exception.getErrorCodes().stream()
                                                 .map(code -> new JsonObject()
                                                                .put("field", code.getField())
                                                                .put("message", code.getMessage()))
                                                 .collect(Collectors.toList())))
                    .toBuffer();
        }

        routingContext.response()
                        .setStatusCode(statusCode)
                        .putHeader(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8")
                        .send(body);
    }

    public static void onSuccess(RoutingContext routingContext, String response) {
       routingContext.response()
                     .setStatusCode(HttpResponseStatus.CREATED.code())
                     .putHeader(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8")
                     .send(response);
    }

    private static AppointmentController getAppointmentController() {
        AppointmentController controller = ApplicationBeanFactory.getBean(AppointmentController.class);
        log.debug("AppointmentController successfully recovery from initial context");
        return controller;
    }
}