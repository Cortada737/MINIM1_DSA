# Mínim 1 DSA — Gestió d'una flota de drons


Projecte Java i Maven del Mínim 1 de DSA.

## Estat

| Part | Estat |
|---|---|
| Interfície `DronManager` (9 operacions) | sí |
| Fachada Singleton (`DronManagerImpl`) | sí |
| 4 tests JUnit | sí |
| Servei REST (4 operacions) | sí |
| Log4j (INFO / ERROR / FATAL) | sí |
| JSON sense cicles (`@JsonIgnore`) | sí |

No hi ha cap `TODO` ni cap `System.out.println`.

## Com executar

Requisits: **Java 17** i **Maven**.

```bash
# Tests
JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.0.2.jdk/Contents/Home mvn clean test

# Servidor REST
JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.0.2.jdk/Contents/Home mvn exec:java
```

El servidor queda a `http://localhost:8080/api`. Per aturar-lo, `Ctrl+C`.

## API REST (4 operacions)

| Mètode | Ruta | Què fa | Èxit |
|---|---|---|---|
| POST | `/api/drones` | Afegir un dron | 201 |
| POST | `/api/pilots` | Afegir un pilot | 201 |
| POST | `/api/flightplans` | Crear un pla de vol | 201 |
| GET | `/api/pilots/{id}/flightplans` | Plans d'un pilot | 200 |

Errors: `400` dades incorrectes, `404` recurs inexistent, `409` conflicte
(duplicat, manteniment o col·lisió), `500` error intern.

## Decisions de disseny

- **Fachada + Singleton:** `DronManagerImpl.getInstance()` retorna sempre la
  mateixa instància, i el REST treballa sobre aquest estat en memòria.
- **Estructures:** `HashMap` per a drons i pilots, `ArrayDeque` com a pila
  LIFO del magatzem, `LinkedList` per als plans de `Dron` i `Pilot`, i
  `ArrayList` per a les posicions i les còpies dels llistats.
- **JSON:** es retornen els models directament i els cicles es trenquen amb
  `@JsonIgnore` a `Dron.getPlansDeVol()` i `Pilot.getPlansDeVol()`.
  No s'utilitzen DTOs.
- **Dates:** `ObjectMapperProvider` registra `JavaTimeModule` perquè
  `LocalDateTime` funcioni amb el format `2026-11-10T10:00:00`.
- **Errors:** un sol `GlobalExceptionMapper` converteix les excepcions a
  `400`, `404`, `409` o `500` amb un JSON `{"status":..., "missatge":...}`.
- **Logs:** `INFO` a l'inici i al final correcte, `ERROR` abans de llançar
  una excepció de negoci i `FATAL` per errors no controlats.

## Tests

`src/test/java/edu/upc/eetac/dsa/DronManagerTest.java`

1. Afegir drons i pilots i ordenar-los per hores de vol.
2. Magatzem LIFO i reparació del dron més proper a la porta.
3. Crear un pla de vol i consultar-lo per pilot i per dron.
4. Un dron en manteniment no pot tenir un pla de vol.

Entre els 4 tests es cobreixen les 9 operacions de la Fachada. Cada test
neteja l'estat amb `@BeforeEach` i `@AfterEach`.






# DSA_MINIM1
# DSA_MINIM1
