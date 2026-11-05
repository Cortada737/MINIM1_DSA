package edu.upc.eetac.dsa.services;

import edu.upc.eetac.dsa.exceptions.ConflictException;
import edu.upc.eetac.dsa.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * Tradueix totes les excepcions a una resposta JSON amb el codi HTTP adequat:
 *  - IllegalArgumentException -> 400
 *  - NotFoundException        -> 404
 *  - ConflictException        -> 409
 *  - qualsevol altra          -> 500
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof NotFoundException) {
            LOGGER.error("Recurs no trobat: {}", exception.getMessage());
            return resposta(404, exception.getMessage());
        }
        if (exception instanceof ConflictException) {
            LOGGER.error("Conflicte: {}", exception.getMessage());
            return resposta(409, exception.getMessage());
        }
        if (exception instanceof IllegalArgumentException) {
            LOGGER.error("Petició incorrecta: {}", exception.getMessage());
            return resposta(400, exception.getMessage());
        }
        // Respectem les respostes que Jersey ja ha generat (p. ex. un 404 o un 405).
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        LOGGER.fatal("Error intern no controlat: {}", exception.getMessage(), exception);
        return resposta(500, "S'ha produït un error intern al servidor.");
    }

    private Response resposta(int status, String missatge) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("status", status, "missatge", missatge))
                .build();
    }
}
