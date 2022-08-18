package br.com.ead.home.verticles;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.jackson.DatabindCodec;

import java.time.Duration;

public class JacksonMapperVerticle extends AbstractVerticle {


    @Override
    public void start() throws Exception {
        configureObjectMapper(DatabindCodec.mapper());
        configureObjectMapper(DatabindCodec.prettyMapper());
    }

    public static void configureObjectMapper(ObjectMapper mapper) {
        // jackson java time module
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(Duration.class, new DurationDeserializer());

        // simple module to register deserializers
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Duration.class, new DurationDeserializer());

        // Creating the parameters name modules
        ParameterNamesModule parameterNamesModule = new ParameterNamesModule();

        // register modules
        mapper.findAndRegisterModules();
        mapper.registerModule(parameterNamesModule);
        mapper.registerModule(javaTimeModule);
        mapper.registerModule(module);

        // enable and disable features
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);

        // set special configurations
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
