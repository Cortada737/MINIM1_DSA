package edu.upc.eetac.dsa;

import edu.upc.eetac.dsa.exceptions.ConflictException;
import edu.upc.eetac.dsa.exceptions.NotFoundException;
import edu.upc.eetac.dsa.models.Dron;
import edu.upc.eetac.dsa.models.Pilot;
import edu.upc.eetac.dsa.models.PlaDeVol;
import edu.upc.eetac.dsa.models.Posicio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementació Singleton de la Fachada.
 *
 * HashMap per a drons i pilots, ArrayDeque com a pila del magatzem i una
 * llista de plans a cada dron i pilot per detectar solapaments.
 */
public class DronManagerImpl implements DronManager {

    private static final Logger LOGGER = LogManager.getLogger(DronManagerImpl.class);
    private static final DronManagerImpl INSTANCE = new DronManagerImpl();

    private final Map<String, Dron> drones = new HashMap<>();
    private final Map<String, Pilot> pilots = new HashMap<>();
    private final Deque<Dron> magatzem = new ArrayDeque<>();

    private DronManagerImpl() {
    }

    /** Retorna la instància única del Singleton. */
    public static DronManagerImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Dron addDron(String id, String nom, String fabricant,
                                     String model, double horesDeVol) {
        LOGGER.info("addDron: id={}", id);

        if (id == null || id.isBlank() || nom == null || nom.isBlank()
                || fabricant == null || fabricant.isBlank()
                || model == null || model.isBlank()) {
            LOGGER.error("Falten camps obligatoris del dron");
            throw new IllegalArgumentException("Tots els camps del dron són obligatoris.");
        }
        if (horesDeVol < 0) {
            LOGGER.error("Hores de vol negatives: {}", horesDeVol);
            throw new IllegalArgumentException("Les hores de vol no poden ser negatives.");
        }
        if (drones.containsKey(id)) {
            LOGGER.error("Dron duplicat: {}", id);
            throw new ConflictException("Ja existeix un dron amb l'id '" + id + "'.");
        }

        Dron dron = new Dron(id, nom, fabricant, model, horesDeVol);
        drones.put(id, dron);

        LOGGER.info("addDron correcte: {}", id);
        return dron;
    }

    @Override
    public Pilot addPilot(String id, String nom, String cognoms,
                                       double horesDeVol) {
        LOGGER.info("addPilot: id={}", id);

        if (id == null || id.isBlank() || nom == null || nom.isBlank()
                || cognoms == null || cognoms.isBlank()) {
            LOGGER.error("Falten camps obligatoris del pilot");
            throw new IllegalArgumentException("Tots els camps del pilot són obligatoris.");
        }
        if (horesDeVol < 0) {
            LOGGER.error("Hores de vol negatives: {}", horesDeVol);
            throw new IllegalArgumentException("Les hores de vol no poden ser negatives.");
        }
        if (pilots.containsKey(id)) {
            LOGGER.error("Pilot duplicat: {}", id);
            throw new ConflictException("Ja existeix un pilot amb l'id '" + id + "'.");
        }

        Pilot pilot = new Pilot(id, nom, cognoms, horesDeVol);
        pilots.put(id, pilot);

        LOGGER.info("addPilot correcte: {}", id);
        return pilot;
    }

    @Override
    public List<Dron> getDronesByHours() {
        LOGGER.info("getDronesByHours");

        List<Dron> resultat = new ArrayList<>(drones.values());
        resultat.sort((d1, d2) -> Double.compare(d2.getHoresDeVol(), d1.getHoresDeVol()));
        return resultat;
    }

    @Override
    public List<Pilot> getPilotsByHours() {
        LOGGER.info("getPilotsByHours");

        List<Pilot> resultat = new ArrayList<>(pilots.values());
        resultat.sort((p1, p2) -> Double.compare(p2.getHoresDeVol(), p1.getHoresDeVol()));
        return resultat;
    }

    @Override
    public Dron saveDronInWarehouse(String dronId) {
        LOGGER.info("saveDronInWarehouse: dronId={}", dronId);

        Dron dron = drones.get(dronId);
        if (dron == null) {
            LOGGER.error("Dron no trobat: {}", dronId);
            throw new NotFoundException("No existeix cap dron amb l'id '" + dronId + "'.");
        }
        if (dron.isEnManteniment()) {
            LOGGER.error("El dron {} ja està en manteniment", dronId);
            throw new ConflictException("El dron '" + dronId + "' ja està en manteniment.");
        }

        dron.setEnManteniment(true);
        magatzem.push(dron);

        LOGGER.info("saveDronInWarehouse correcte: {}", dronId);
        return dron;
    }

    @Override
    public Dron repairDron() {
        LOGGER.info("repairDron");

        if (magatzem.isEmpty()) {
            LOGGER.error("El magatzem és buit");
            throw new ConflictException("El magatzem és buit.");
        }

        Dron dron = magatzem.pop();
        dron.setEnManteniment(false);

        LOGGER.info("repairDron correcte: {}", dron.getId());
        return dron;
    }

    @Override
    public PlaDeVol createFlightPlan(String dronId, String pilotId,
                                                  LocalDateTime dataHora, double duradaHores,
                                                  List<Posicio> posicions) {
        LOGGER.info("createFlightPlan: dronId={}, pilotId={}", dronId, pilotId);

        // Validacions bàsiques de les dades rebudes.
        if (dronId == null || pilotId == null || dataHora == null) {
            LOGGER.error("Falten dades del pla de vol");
            throw new IllegalArgumentException("Falten dades del pla de vol.");
        }
        if (duradaHores <= 0) {
            LOGGER.error("Durada no vàlida: {}", duradaHores);
            throw new IllegalArgumentException("La durada ha de ser positiva.");
        }
        if (posicions == null || posicions.isEmpty()) {
            LOGGER.error("El pla no té posicions");
            throw new IllegalArgumentException("El pla ha de tenir almenys una posició.");
        }
        for (Posicio p : posicions) {
            if (p == null || p.getLatitud() < -90 || p.getLatitud() > 90
                    || p.getLongitud() < -180 || p.getLongitud() > 180) {
                LOGGER.error("Posició no vàlida al pla de vol");
                throw new IllegalArgumentException("Hi ha una posició no vàlida.");
            }
        }

        Dron dron = drones.get(dronId);
        if (dron == null) {
            LOGGER.error("Dron no trobat: {}", dronId);
            throw new NotFoundException("No existeix cap dron amb l'id '" + dronId + "'.");
        }

        Pilot pilot = pilots.get(pilotId);
        if (pilot == null) {
            LOGGER.error("Pilot no trobat: {}", pilotId);
            throw new NotFoundException("No existeix cap pilot amb l'id '" + pilotId + "'.");
        }

        if (dron.isEnManteniment()) {
            LOGGER.error("El dron {} està en manteniment", dronId);
            throw new ConflictException("El dron '" + dronId + "' està en manteniment.");
        }

        // Comprovem que ni el dron ni el pilot tinguin un vol solapat.
        LocalDateTime fi = dataHora.plusSeconds(Math.round(duradaHores * 3600));
        for (PlaDeVol pla : dron.getPlansDeVol()) {
            if (pla.solapaAmb(dataHora, fi)) {
                LOGGER.error("Solapament de dron: {}", dronId);
                throw new ConflictException("El dron '" + dronId + "' ja té un vol en aquest horari.");
            }
        }
        for (PlaDeVol pla : pilot.getPlansDeVol()) {
            if (pla.solapaAmb(dataHora, fi)) {
                LOGGER.error("Solapament de pilot: {}", pilotId);
                throw new ConflictException("El pilot '" + pilotId + "' ja té un vol en aquest horari.");
            }
        }

        PlaDeVol pla = new PlaDeVol(UUID.randomUUID().toString(), dron, pilot,
                dataHora, duradaHores, posicions);
        dron.addPlaDeVol(pla);
        pilot.addPlaDeVol(pla);

        LOGGER.info("createFlightPlan correcte: {}", pla.getId());
        return pla;
    }

    @Override
    public List<PlaDeVol> getFlightPlansByPilot(String pilotId) {
        LOGGER.info("getFlightPlansByPilot: pilotId={}", pilotId);

        Pilot pilot = pilots.get(pilotId);
        if (pilot == null) {
            LOGGER.error("Pilot no trobat: {}", pilotId);
            throw new NotFoundException("No existeix cap pilot amb l'id '" + pilotId + "'.");
        }
        return new ArrayList<>(pilot.getPlansDeVol());
    }

    @Override
    public List<PlaDeVol> getFlightPlansByDron(String dronId) {
        LOGGER.info("getFlightPlansByDron: dronId={}", dronId);

        Dron dron = drones.get(dronId);
        if (dron == null) {
            LOGGER.error("Dron no trobat: {}", dronId);
            throw new NotFoundException("No existeix cap dron amb l'id '" + dronId + "'.");
        }
        return new ArrayList<>(dron.getPlansDeVol());
    }

    /** Buidar l'estat. S'utilitza als tests. */
    public void clear() {
        drones.clear();
        pilots.clear();
        magatzem.clear();
    }
}
