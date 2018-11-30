package visu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VisualisirungStation {

    private String station;
    private int idelTime;
    private int inUseTime;

    private static ArrayList<VisualisirungStation> allVisualisirungStations = new ArrayList<VisualisirungStation>();

    private VisualisirungStation(String station, int idelTime, int inUseTime) {
        this.station = station;
        this.idelTime = idelTime;
        this.inUseTime = inUseTime;

        allVisualisirungStations.add(this);

    }

    public static void create(String station, int idelTime, int inUseTime) {
        new VisualisirungStation(station,idelTime,inUseTime);
    }

    public String getStation() {
        return station;
    }

    public int getIdelTime() {
        return idelTime;
    }

    public int getInUseTime() {
        return inUseTime;
    }

    private static void sortiereVisualisirungStation(){
        Collections.sort(allVisualisirungStations, Comparator.comparingInt(VisualisirungStation::getInUseTime).reversed());

    }

    public static ArrayList<VisualisirungStation> getAllVisualisirungStations() {
        sortiereVisualisirungStation();

        return allVisualisirungStations;
    }
}
