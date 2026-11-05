package edu.upc.eetac.dsa.models;

import java.util.Objects;

/**
 * Punt geogràfic (latitud/longitud) d'un pla de vol.
 */
public class Posicio {

    private double latitud;
    private double longitud;

    public Posicio() {
    }

    public Posicio(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Posicio)) {
            return false;
        }
        Posicio posicio = (Posicio) o;
        return Double.compare(posicio.latitud, latitud) == 0
                && Double.compare(posicio.longitud, longitud) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitud, longitud);
    }

    @Override
    public String toString() {
        return "Posicio{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
