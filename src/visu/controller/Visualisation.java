package visu.controller;

import visu.io.VisualisierungFactory;
import visu.view.VisualisationGraph;

/**
 *The main class, initialize the visualisation
 *
 * @author Team5
 * @version 1
 */
public class Visualisation {

    /**
     *
     *
     * @param args
     */
    public static void main(String[] args){
        Visualisation theVisualisation = new Visualisation();
        theVisualisation.init();
    }

    private void init(){
        VisualisierungFactory.createStartScenario();


        VisualisationGraph g1 = new VisualisationGraph();
        g1.createDiagramWithInitialDataPoints();


    }
}
