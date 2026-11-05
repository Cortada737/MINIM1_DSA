package edu.upc.eetac.dsa;

import edu.upc.eetac.dsa.models.Dron;
import edu.upc.eetac.dsa.models.Pilot;
import edu.upc.eetac.dsa.models.PlaDeVol;
import edu.upc.eetac.dsa.models.Posicio;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfície de negoci (Fachada) amb les 9 operacions del sistema de drons.
 * La implementació és DronManagerImpl (Singleton).
 */
public interface DronManager {

    /** Afegeix un dron i el retorna. */
    Dron addDron(String id, String nom, String fabricant, String model, double horesDeVol);

    /** Afegeix un pilot i el retorna. */
    Pilot addPilot(String id, String nom, String cognoms, double horesDeVol);

    /** Retorna els drons ordenats descendentment per hores de vol. */
    List<Dron> getDronesByHours();

    /** Retorna els pilots ordenats descendentment per hores de vol. */
    List<Pilot> getPilotsByHours();

    /** Guarda un dron al magatzem de manteniment (pila LIFO). */
    Dron saveDronInWarehouse(String dronId);

    /** Repara i retira el dron més proper a la porta del magatzem. */
    Dron repairDron();

    /** Crea un pla de vol validant manteniment i solapaments de dron i pilot. */
    PlaDeVol createFlightPlan(String dronId, String pilotId, LocalDateTime dataHora,
                              double duradaHores, List<Posicio> posicions);

    /** Retorna els plans de vol assignats a un pilot. */
    List<PlaDeVol> getFlightPlansByPilot(String pilotId);

    /** Retorna els plans de vol assignats a un dron. */
    List<PlaDeVol> getFlightPlansByDron(String dronId);
}
