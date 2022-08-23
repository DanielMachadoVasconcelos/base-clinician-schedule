package br.com.ead.home.verticles;

import io.vertx.core.AbstractVerticle;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApplicationVersionVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
       log.info("Current Application Version is: {}", "1.0.0.0");
    }
}
