package edu.upc.eetac.dsa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Pilot de la flota: dades bàsiques, hores de vol i plans assignats.
 * Igual que Dron, la llista de plans s'ignora al JSON per evitar cicles.
 */
public class Pilot {

    private String id;
    private String nom;
    private String cognoms;
    private double horesDeVol;
    private final List<PlaDeVol> plansDeVol;

    public Pilot() {
        this.plansDeVol = new LinkedList<>();
    }

    public Pilot(String id, String nom, String cognoms, double horesDeVol) {
        this();
        this.id = id;
        this.nom = nom;
        this.cognoms = cognoms;
        this.horesDeVol = horesDeVol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognoms() {
        return cognoms;
    }

    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
    }

    public double getHoresDeVol() {
        return horesDeVol;
    }

    public void setHoresDeVol(double horesDeVol) {
        this.horesDeVol = horesDeVol;
    }

    public void addPlaDeVol(PlaDeVol plaDeVol) {
        this.plansDeVol.add(plaDeVol);
    }

    @JsonIgnore
    public List<PlaDeVol> getPlansDeVol() {
        return plansDeVol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pilot)) {
            return false;
        }
        Pilot pilot = (Pilot) o;
        return Objects.equals(id, pilot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pilot{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", cognoms='" + cognoms + '\'' +
                ", horesDeVol=" + horesDeVol +
                ", numPlansDeVol=" + plansDeVol.size() +
                '}';
    }
}
