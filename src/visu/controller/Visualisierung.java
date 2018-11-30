package visu.controller;

import visu.io.VisualisierungFactory;
import visu.view.VisualisierungGraph;

public class Visualisierung {

    /**
     *
     *
     * @param args
     */
    public static void main(String[] args){
        Visualisierung theVisualisierung = new Visualisierung();
        theVisualisierung.init();
    }

    private void init(){
        VisualisierungFactory.createStartScenario();


        VisualisierungGraph g1 = new VisualisierungGraph();
        g1.createDiagramWithInitialDataPoints();


    }
}
