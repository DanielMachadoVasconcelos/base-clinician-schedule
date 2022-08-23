package br.com.ead.home.actions;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HttpRequestLogger implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerRequest httpServerRequest = routingContext.request();
        HttpMethod method = httpServerRequest.method();
        String path = httpServerRequest.path();
        String hostAddress = httpServerRequest.host();
        log.info("Request received. method={}, path={}, from={}", method, path, hostAddress);
        routingContext.next();
    }
}
