package edu.upc.eetac.dsa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Dron de la flota: dades bàsiques, hores de vol i plans assignats.
 * La llista de plans s'ignora al JSON amb @JsonIgnore per evitar el cicle
 * Dron -> PlaDeVol -> Dron.
 */
public class Dron {

    private String id;
    private String nom;
    private String fabricant;
    private String model;
    private double horesDeVol;
    private boolean enManteniment;
    private final List<PlaDeVol> plansDeVol;

    public Dron() {
        this.plansDeVol = new LinkedList<>();
    }

    public Dron(String id, String nom, String fabricant, String model, double horesDeVol) {
        this();
        this.id = id;
        this.nom = nom;
        this.fabricant = fabricant;
        this.model = model;
        this.horesDeVol = horesDeVol;
        this.enManteniment = false;
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

    public String getFabricant() {
        return fabricant;
    }

    public void setFabricant(String fabricant) {
        this.fabricant = fabricant;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getHoresDeVol() {
        return horesDeVol;
    }

    public void setHoresDeVol(double horesDeVol) {
        this.horesDeVol = horesDeVol;
    }

    public boolean isEnManteniment() {
        return enManteniment;
    }

    public void setEnManteniment(boolean enManteniment) {
        this.enManteniment = enManteniment;
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
        if (!(o instanceof Dron)) {
            return false;
        }
        Dron dron = (Dron) o;
        return Objects.equals(id, dron.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Dron{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", fabricant='" + fabricant + '\'' +
                ", model='" + model + '\'' +
                ", horesDeVol=" + horesDeVol +
                ", enManteniment=" + enManteniment +
                ", numPlansDeVol=" + plansDeVol.size() +
                '}';
    }
}
