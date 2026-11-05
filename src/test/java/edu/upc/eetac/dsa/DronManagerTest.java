package edu.upc.eetac.dsa;

import edu.upc.eetac.dsa.exceptions.ConflictException;
import edu.upc.eetac.dsa.models.Dron;
import edu.upc.eetac.dsa.models.Pilot;
import edu.upc.eetac.dsa.models.PlaDeVol;
import edu.upc.eetac.dsa.models.Posicio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de la Fachada. Amb aquests 4 tests es cobreixen totes les operacions:
 * altes i llistats, magatzem LIFO i reparació, creació i consulta de plans,
 * i la validació de dron en manteniment.
 */
class DronManagerTest {

    private DronManagerImpl manager;

    @BeforeEach
    void setUp() {
        manager = DronManagerImpl.getInstance();
        manager.clear();
    }

    @AfterEach
    void tearDown() {
        manager.clear();
    }

    @Test
    @DisplayName("Els drons i pilots s'ordenen descendentment per hores de vol")
    void testAddDronAndPilotOrderedByHours() {
        manager.addDron("d1", "Falcon", "DJI", "Mavic 3", 10.0);
        manager.addDron("d2", "Eagle", "Parrot", "Anafi", 25.5);
        manager.addDron("d3", "Swift", "Autel", "Evo Lite", 3.0);
        manager.addPilot("p1", "Anna", "Serra", 30.0);
        manager.addPilot("p2", "Marc", "Vila", 12.5);

        List<String> idsDrons = manager.getDronesByHours().stream()
                .map(Dron::getId)
                .collect(Collectors.toList());
        List<String> idsPilots = manager.getPilotsByHours().stream()
                .map(Pilot::getId)
                .collect(Collectors.toList());

        assertEquals(List.of("d2", "d1", "d3"), idsDrons);
        assertEquals(List.of("p1", "p2"), idsPilots);
        assertEquals(25.5, manager.getDronesByHours().get(0).getHoresDeVol(), 0.0001);
        assertEquals(30.0, manager.getPilotsByHours().get(0).getHoresDeVol(), 0.0001);
    }

    @Test
    @DisplayName("El magatzem és LIFO: es repara el dron més proper a la porta")
    void testWarehouseIsLifoAndRepairReturnsClosestDrone() {
        manager.addDron("d1", "Falcon", "DJI", "Mavic 3", 10.0);
        manager.addDron("d2", "Eagle", "Parrot", "Anafi", 20.0);
        manager.addDron("d3", "Swift", "Autel", "Evo Lite", 30.0);

        manager.saveDronInWarehouse("d1");
        manager.saveDronInWarehouse("d2");
        manager.saveDronInWarehouse("d3");

        assertTrue(manager.getDronesByHours().stream().allMatch(Dron::isEnManteniment));

        // L'últim guardat és el més proper a la porta i surt primer.
        Dron primerReparat = manager.repairDron();
        assertEquals("d3", primerReparat.getId());
        assertFalse(primerReparat.isEnManteniment());

        assertEquals("d2", manager.repairDron().getId());
        assertEquals("d1", manager.repairDron().getId());
        assertThrows(ConflictException.class, () -> manager.repairDron());
    }

    @Test
    @DisplayName("Es crea un pla de vol correcte i s'assigna a dron i pilot")
    void testCreateFlightPlanCorrectly() {
        manager.addDron("d1", "Falcon", "DJI", "Mavic 3", 10.0);
        manager.addPilot("p1", "Anna", "Serra", 30.0);

        LocalDateTime inici = LocalDateTime.of(2026, 11, 10, 10, 0);
        List<Posicio> recorregut = List.of(
                new Posicio(41.275, 1.988),
                new Posicio(41.300, 2.100),
                new Posicio(41.320, 2.150)
        );

        PlaDeVol pla = manager.createFlightPlan("d1", "p1", inici, 2.0, recorregut);

        assertNotNull(pla.getId());
        assertEquals("d1", pla.getDron().getId());
        assertEquals("p1", pla.getPilot().getId());
        assertEquals(inici, pla.getDataHora());
        assertEquals(inici.plusHours(2), pla.getDataHoraFi());
        assertEquals(3, pla.getPosicions().size());
        assertEquals(1, manager.getFlightPlansByPilot("p1").size());
        assertEquals(1, manager.getFlightPlansByDron("d1").size());
    }

    @Test
    @DisplayName("Un dron en manteniment no pot tenir un pla de vol")
    void testDroneInMaintenanceCannotFly() {
        manager.addDron("d1", "Falcon", "DJI", "Mavic 3", 10.0);
        manager.addPilot("p1", "Anna", "Serra", 30.0);
        manager.saveDronInWarehouse("d1");

        LocalDateTime inici = LocalDateTime.of(2026, 11, 10, 10, 0);
        List<Posicio> recorregut = List.of(new Posicio(41.0, 2.0));

        assertThrows(ConflictException.class, () ->
                manager.createFlightPlan("d1", "p1", inici, 2.0, recorregut));

        manager.repairDron();
        assertNotNull(manager.createFlightPlan("d1", "p1", inici, 2.0, recorregut).getId());
    }
}
