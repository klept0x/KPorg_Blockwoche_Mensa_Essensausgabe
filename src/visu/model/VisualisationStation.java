package visu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class for a visualisation  label
 *
 * @author Team5
 * @version 1
 */
public class VisualisationStation {

    /** name of the visualisation label */
    private String label;

    /** idle time of the visualisation label */
    private int idelTime;

    /** in use time of the visualisation label */
    private int inUseTime;

    /** a list of the visualisation label */
    private static ArrayList<VisualisationStation> allVisualisationStations = new ArrayList<VisualisationStation>();

    /**
     * (private!) Constructor, creates a new visualisation label
     * 
     * @param label    of the visualisation label
     * @param idelTime   the idel time of the visualisation label
     * @param inUseTime  the time that the visualisation label was be used
     */
    private VisualisationStation(String label, int idelTime, int inUseTime) {
        this.label = label;
        this.idelTime = idelTime;
        this.inUseTime = inUseTime;

        allVisualisationStations.add(this);

    }

    /**
     * create a new visualisation label and add it to the visualisationstation list
     *
     * @param label    of the visualisation label
     * @param idelTime   the idel time of the visualisation label
     * @param inUseTime  the time that the visualisation label was be used
     */
    public static void create(String label, int idelTime, int inUseTime) {
        new VisualisationStation(label,idelTime,inUseTime);
    }

    /**
     * Gets the name of the visualisation staion
     * 
     * @return the name of the visualisation staion
     */
    public String getLabel() {
        return label;
    }


    /**
     * Gets the idle time of the visualisation staion
     *
     * @return the idle time of the visualisation staion
     */
    public int getIdelTime() {
        return idelTime;
    }

    /**
     * Get the in use time of the visualisation staion
     *
     * @return the in use time of the visualisation staion
     */
    public int getInUseTime() {
        return inUseTime;
    }

    /**
     * sorts the visualisation staions in the list, so the poitns (poitns equals the visualisation staion) in the
     * graph plotter are shown in descending order
     */
    private static void sortiereVisualisirungStation(){
        Collections.sort(allVisualisationStations, Comparator.comparingInt(VisualisationStation::getInUseTime).reversed());
    }

    /**
     * Gets the visualisation staions in descending order
     *
     * @return the visualisation staions in descending order
     */
    public static ArrayList<VisualisationStation> getAllVisualisationStations() {
        //bad code example
        //getter should just get the information
        sortiereVisualisirungStation();
        return allVisualisationStations;
    }
}
