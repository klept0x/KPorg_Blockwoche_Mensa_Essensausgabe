package visu.view;

import plotter.src.main.java.model.CustomPoint;
import plotter.src.main.java.view.PlotterPane;
import visu.model.VisualisationStation;

import java.util.ArrayList;

/**
 * A class that displays the average in use time of MensaStation.
 *
 * @author Team5
 * @version 1
 */
public class VisualisationGraph {


    /**
     * Creates diagram with points. The points are the average in use time. And the number of points are the Station in total.
     */
    public void createDiagramWithInitialDataPoints() {
        ArrayList<CustomPoint> points = new ArrayList<>();

        int averageInUseTime = 0;
        int stationsCounter = 1;

        for(VisualisationStation s: VisualisationStation.getAllVisualisationStations()) {
            averageInUseTime += s.getInUseTime();
        }

        averageInUseTime = averageInUseTime/VisualisationStation.getAllVisualisationStations().size();

        for(VisualisationStation s: VisualisationStation.getAllVisualisationStations()) {
            points.add(new CustomPoint(stationsCounter, averageInUseTime));
            stationsCounter++;
            //Statistics.show("Punkt in Liste");
        }



        PlotterPane p1 = new PlotterPane(points,
                800,
                600,
                true,
                "Stations",
                "Average in use time",
                "Stations / Average in use time");

        p1.setVisible(true);


        //is not needed. Just to see the result.
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
