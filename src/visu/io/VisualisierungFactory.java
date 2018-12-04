package visu.io;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import visu.model.VisualisationStation;

import java.io.IOException;
import java.util.List;

/**
 * This is an abstract factory that creates instances
 * of actor types like objects, stations and their queues
 * Modified by Team 5:
 * Data-fields and createStartStation(), createObjects(), createProcessStations(), createEndStation() were set to protected,
 * because FactoryXML inherits from VisualisierungFactory.
 * Setter-Methods for following data-f
 * ields : theObjectDataFile, theStationDataFile, theStartStationDataFile, theEndStationDataFile
 *
 * @author Jaeger, Schmidt modified by Team 5
 * @version 2017-10-29
 */
public class VisualisierungFactory {

    private static String theObjectDataFile = "daten/Station Kennzahlauswertung Burgertag.xml";



    /**
     * create the actors for the starting scenario
     */
    public static void createStartScenario() {

        /*NOTE: The start station must be created first,
         * because the objects constructor puts the objects into the start stations outgoing queue
         */

        createObjects();
    }


    /**
     * create some objects out of the XML file
     *
     */
    private static void createObjects(){

        //Statistics.show("createObjects");

        try {

            //Statistics.show("try");

            //read the information from the XML file into a JDOM Document
            Document theXMLDoc = new SAXBuilder().build(theObjectDataFile);

            //the <daten> ... </daten> node, this is the files root Element
            Element root = theXMLDoc.getRootElement();

            //get all the objects into a List object
            List <Element> allObjects = root.getChildren("kennzahlen");

            //Statistics.show("Vor der Schleife");

            //separate every JDOM "object" Element from the list and create Java TheObject objects
            //Statistics.show("" + allObjects.size());

            for (Element theStations : allObjects) {

                //Statistics.show("In der Schleife");

                // data variables:
                String station = "";
                int idelTime = 0;
                int inUseTime = 0;

                // read data
                station = theStations.getChildText("stationen");
                idelTime = Integer.parseInt(theStations.getChildText("idelTime"));
                inUseTime = Integer.parseInt(theStations.getChildText("inUseTime"));

                //creating a new TheObject object
                VisualisationStation.create(station, idelTime, inUseTime);
                //Statistics.show("Station erstellt!");

            }



        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
