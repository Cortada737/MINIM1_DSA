package edu.upc.eetac.dsa.services;

import edu.upc.eetac.dsa.DronManager;
import edu.upc.eetac.dsa.DronManagerImpl;
import edu.upc.eetac.dsa.models.Dron;
import edu.upc.eetac.dsa.models.Pilot;
import edu.upc.eetac.dsa.models.PlaDeVol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Servei REST de la flota de drons.
 *
 * Implementa les 4 operacions:
 *  1. Afegir un dron            -> POST /api/drones
 *  2. Afegir un pilot           -> POST /api/pilots
 *  3. Crear un pla de vol       -> POST /api/flightplans
 *  4. Plans de vol d'un pilot   -> GET  /api/pilots/{id}/flightplans
 *
 * Retorna directament els models de negoci. Els cicles JSON s'eviten amb
 * @JsonIgnore a les llistes de plans de Dron i Pilot.
 * Codis HTTP: 200, 201, 400, 404, 409 i 500 (via GlobalExceptionMapper).
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class DronService {

    private static final Logger LOGGER = LogManager.getLogger(DronService.class);

    private final DronManager manager;

    public DronService() {
        this(DronManagerImpl.getInstance());
    }

    public DronService(DronManager manager) {
        this.manager = manager;
    }

    /** POST /api/drones: afegeix un dron (201). */
    @POST
    @Path("/drones")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDron(Dron dron) {
        LOGGER.info("POST /api/drones -Inici- dades={}", dron);

        if (dron == null) {
            throw new IllegalArgumentException("El cos de la petició no pot ser buit.");
        }

        Dron creat = manager.addDron(
                dron.getId(), dron.getNom(), dron.getFabricant(),
                dron.getModel(), dron.getHoresDeVol());

        LOGGER.info("POST /api/drones -Fi- dron creat id={}", creat.getId());
        return Response.status(Response.Status.CREATED).entity(creat).build();
    }

    /** POST /api/flightplans: crea un pla de vol (201/400/404/409). */
    @POST
    @Path("/flightplans")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFlightPlan(PlaDeVol plaDeVol) {
        LOGGER.info("POST /api/flightplans -Inici- dades={}", plaDeVol);

        if (plaDeVol == null) {
            throw new IllegalArgumentException("El cos de la petició no pot ser buit.");
        }
        if (plaDeVol.getDron() == null || plaDeVol.getDron().getId() == null) {
            throw new IllegalArgumentException("El camp 'dron.id' és obligatori.");
        }
        if (plaDeVol.getPilot() == null || plaDeVol.getPilot().getId() == null) {
            throw new IllegalArgumentException("El camp 'pilot.id' és obligatori.");
        }

        PlaDeVol creat = manager.createFlightPlan(
                plaDeVol.getDron().getId(),
                plaDeVol.getPilot().getId(),
                plaDeVol.getDataHora(),
                plaDeVol.getDuradaHores(),
                plaDeVol.getPosicions());

        LOGGER.info("POST /api/flightplans -Fi- pla creat id={}", creat.getId());
        return Response.status(Response.Status.CREATED).entity(creat).build();
    }

    /** GET /api/pilots/{id}/flightplans: plans d'un pilot (200/404). */
    @GET
    @Path("/pilots/{id}/flightplans")
    public Response getFlightPlansByPilot(@PathParam("id") String id) {
        LOGGER.info("GET /api/pilots/{}/flightplans -Inici-", id);

        List<PlaDeVol> plans = manager.getFlightPlansByPilot(id);

        LOGGER.info("GET /api/pilots/{}/flightplans -Fi- {} plans", id, plans.size());
        return Response.ok(plans).build();
    }

    /** POST /api/pilots: afegeix un pilot (201). */
    @POST
    @Path("/pilots")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPilot(Pilot pilot) {
        LOGGER.info("POST /api/pilots -Inici- dades={}", pilot);

        if (pilot == null) {
            throw new IllegalArgumentException("El cos de la petició no pot ser buit.");
        }

        Pilot creat = manager.addPilot(
                pilot.getId(), pilot.getNom(),
                pilot.getCognoms(), pilot.getHoresDeVol());

        LOGGER.info("POST /api/pilots -Fi- pilot creat id={}", creat.getId());
        return Response.status(Response.Status.CREATED).entity(creat).build();
    }
}
