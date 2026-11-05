package edu.upc.eetac.dsa.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Pla de vol: associa un dron i un pilot en un interval de temps i guarda
 * la ruta de posicions. És la peça que permet detectar solapaments.
 *
 * Es pot serialitzar directament a JSON perquè Dron i Pilot ignoren les
 * seves llistes de plans, de manera que no es produeixen cicles.
 */
public class PlaDeVol {

    private String id;
    private Dron dron;
    private Pilot pilot;
    private LocalDateTime dataHora;
    private double duradaHores;
    private final List<Posicio> posicions;

    public PlaDeVol() {
        this.posicions = new ArrayList<>();
    }

    public PlaDeVol(String id, Dron dron, Pilot pilot, LocalDateTime dataHora,
                    double duradaHores, List<Posicio> posicions) {
        this();
        this.id = id;
        this.dron = dron;
        this.pilot = pilot;
        this.dataHora = dataHora;
        this.duradaHores = duradaHores;
        if (posicions != null) {
            this.posicions.addAll(posicions);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Dron getDron() {
        return dron;
    }

    public void setDron(Dron dron) {
        this.dron = dron;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public double getDuradaHores() {
        return duradaHores;
    }

    public void setDuradaHores(double duradaHores) {
        this.duradaHores = duradaHores;
    }

    public List<Posicio> getPosicions() {
        return posicions;
    }

    /** Converteix la durada en hores a segons i calcula l'hora de fi. */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime getDataHoraFi() {
        long segons = Math.round(this.duradaHores * 3600.0);
        return this.dataHora.plusSeconds(segons);
    }

    /**
     * Dos intervals es solapen si iniciA < fiB i iniciB < fiA.
     * Els extrems són oberts: un vol pot començar just quan n'acaba un altre.
     */
    public boolean solapaAmb(LocalDateTime iniciProposat, LocalDateTime fiProposat) {
        return iniciProposat.isBefore(this.getDataHoraFi())
                && this.dataHora.isBefore(fiProposat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaDeVol)) {
            return false;
        }
        PlaDeVol plaDeVol = (PlaDeVol) o;
        return Objects.equals(id, plaDeVol.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PlaDeVol{" +
                "id='" + id + '\'' +
                ", dronId='" + (dron != null ? dron.getId() : null) + '\'' +
                ", pilotId='" + (pilot != null ? pilot.getId() : null) + '\'' +
                ", dataHora=" + dataHora +
                ", duradaHores=" + duradaHores +
                ", dataHoraFi=" + (dataHora != null ? getDataHoraFi() : null) +
                ", numPosicions=" + posicions.size() +
                '}';
    }
}
