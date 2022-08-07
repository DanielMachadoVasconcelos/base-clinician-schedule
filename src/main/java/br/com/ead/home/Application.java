package br.com.ead.home;

import io.vertx.core.Launcher;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Application {

    public static void main(String[] args) {
        Launcher.main(new String[]{"run", "br.com.ead.home.MainVerticle"});
    }
}
