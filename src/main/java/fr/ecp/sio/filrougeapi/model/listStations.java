package fr.ecp.sio.filrougeapi.model;

import java.util.List;

/**
 * A POJO to represent a listStations.
 */
public class listStations {

    private List<Station> stations;

    private int totAvailableBikes = 0;
    private int totAvailableBikeStands =0;

    public listStations(List<Station> stations) {
        this.stations=stations;
        this.totAvailableBikes=getTotAvailableBikes();
        this.totAvailableBikeStands=getTotAvailableBikeStands();
    }

    public void setStations(List<Station> stations) {
        this.stations =stations;
    }
    public List<Station> getStations() {
        return this.stations;
    }

    public int getTotAvailableBikeStands() {

        for (Station station : this.stations) {
            this.totAvailableBikeStands = this.totAvailableBikeStands + station.getAvailableBikeStands();
        }
        return this.totAvailableBikeStands;
    }

    public int getTotAvailableBikes() {

        for (Station station : this.stations) {
            this.totAvailableBikes = this.totAvailableBikes + station.getAvailableBikes();
        }
        return this.totAvailableBikes;
    }

}
