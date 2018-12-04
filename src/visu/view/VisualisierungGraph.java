package visu.view;

import io.Statistics;
import plotter.src.main.java.model.CustomPoint;
import plotter.src.main.java.view.PlotterPane;
import visu.model.VisualisationStation;

import java.util.ArrayList;

public class VisualisierungGraph {

    public void createDiagramWithInitialDataPoints() {
        ArrayList<CustomPoint> points = new ArrayList<>();

        int x = 1;

        for(VisualisationStation s: VisualisationStation.getAllVisualisirungStations()) {
            points.add(new CustomPoint(x, s.getInUseTime()));
            x++;
            Statistics.show("Punkt in Liste");
        }

        PlotterPane p1 = new PlotterPane(points,
                800,
                600,
                false,
                "Stationen",
                "In Use Time",
                "Idle Time / In Use Time");

        p1.setVisible(true);


        //is not needed. Just to see the result.
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
