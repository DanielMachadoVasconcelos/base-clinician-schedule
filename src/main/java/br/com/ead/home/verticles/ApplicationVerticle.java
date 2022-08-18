package br.com.ead.home.verticles;

import io.vertx.core.AbstractVerticle;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApplicationVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(new ApplicationVersion())
             .compose(parameters -> vertx.deployVerticle(new BeanFactoryVerticle()))
             .compose(parameters -> vertx.deployVerticle(new JacksonMapperVerticle()))
             .compose(parameters -> vertx.deployVerticle(new HttpServerVerticle()))
        ;
    }
}
