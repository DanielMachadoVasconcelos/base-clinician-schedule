package br.com.ead.home;

import br.com.ead.home.actions.PostAppointmentsAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        ObjectMapper mapper = DatabindCodec.mapper();
        mapper.registerModule(new JavaTimeModule());

        ObjectMapper prettyMapper = DatabindCodec.prettyMapper();
        prettyMapper.registerModule(new JavaTimeModule());

        // Create a Router
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        // Mount the handler for all incoming requests at every path and HTTP method
        router.post( "/appointments")
              .handler(new PostAppointmentsAction());

        // Create the HTTP server
        vertx.createHttpServer()
                // Handle every request using the router
                .requestHandler(router)
                // Start listening
                .listen(8888)
                // Print the port
                .onSuccess(server -> log.info("HTTP server started on port " + server.actualPort()));
    }

}
