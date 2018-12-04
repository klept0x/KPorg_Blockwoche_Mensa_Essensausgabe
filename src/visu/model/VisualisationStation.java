package visu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VisualisationStation {

    private String station;
    private int idelTime;
    private int inUseTime;

    private static ArrayList<VisualisationStation> allVisualisationStations = new ArrayList<VisualisationStation>();

    private VisualisationStation(String station, int idelTime, int inUseTime) {
        this.station = station;
        this.idelTime = idelTime;
        this.inUseTime = inUseTime;

        allVisualisationStations.add(this);

    }

    public static void create(String station, int idelTime, int inUseTime) {
        new VisualisationStation(station,idelTime,inUseTime);
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
        Collections.sort(allVisualisationStations, Comparator.comparingInt(VisualisationStation::getInUseTime).reversed());

    }

    public static ArrayList<VisualisationStation> getAllVisualisationStations() {
        sortiereVisualisirungStation();

        return allVisualisationStations;
    }
}
