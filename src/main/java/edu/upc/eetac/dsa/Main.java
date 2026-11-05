package edu.upc.eetac.dsa;

import edu.upc.eetac.dsa.services.DronService;
import edu.upc.eetac.dsa.services.GlobalExceptionMapper;
import edu.upc.eetac.dsa.services.ObjectMapperProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

/**
 * Arrenca el servidor Grizzly amb el servei REST de Jersey a
 * http://localhost:8080/api. Per aturar-lo, Ctrl+C.
 */
public final class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static final String BASE_URI = "http://0.0.0.0:8080/";

    private Main() {
    }

    public static void main(String[] args) {
        try {
            ResourceConfig config = new ResourceConfig();
            config.register(DronService.class);
            config.register(GlobalExceptionMapper.class);
            config.register(ObjectMapperProvider.class);
            config.register(JacksonFeature.class);

            final HttpServer server = GrizzlyHttpServerFactory
                    .createHttpServer(URI.create(BASE_URI), config);

            LOGGER.info("Servidor REST arrencat a {}api", BASE_URI);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Aturant el servidor REST...");
                server.shutdownNow();
                LOGGER.info("Servidor REST aturat.");
            }));

            // Manté el procés viu mentre Grizzly escolta.
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            LOGGER.error("El fil principal s'ha interromput", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.fatal("No s'ha pogut arrencar el servidor REST", e);
            throw new IllegalStateException("Error arrencant el servidor REST", e);
        }
    }
}
