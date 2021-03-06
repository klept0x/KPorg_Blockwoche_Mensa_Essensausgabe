package io;

import model.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import view.QueueViewJPanel;
import view.QueueViewText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The class FactoryXML extends the class Factory.
 *
 * @author Team5
 * @version 1
 */

public class FactoryXML extends Factory {


    /**
     * create the actors for the starting scenario
     */
    public static void createStartScenario() {

        /*NOTE: The start station must be created first,
         * because the objects constructor puts the objects into the start stations outgoing queue
         */
        createStartStation();
        createObjects();
        createProcessStations();
        createEndStation();
    }

    /**
     * create some objects out of the XML file
     */
    protected static void createObjects() {

        try {

            //read the information from the XML file into a JDOM Document
            Document theXMLDoc = new SAXBuilder().build(Factory.theObjectDataFile);

            //the <settings> ... </settings> node, this is the files root Element
            Element root = theXMLDoc.getRootElement();


            //get all the objects into a List object
            List<Element> allObjects = root.getChildren("object");

            Statistics.show("--------------------Studenten werden jetzt erzeugt-------------------------\n");

            //separate every JDOM "object" Element from the list and create Java TheObject objects
            for (Element theObject : allObjects) {


                // data variables:
                String label = null;
                int schleife = 1;
                int processtime = 0;
                int speed = 0;
                String image = null;
                int maxWait=0;

                // read data
                label = theObject.getChildText("label");
                //the <schleife> ... </schleife> node, this is the objects quantity
                schleife = Integer.parseInt(theObject.getChildText("schleife"));
                //

                processtime = Integer.parseInt(theObject.getChildText("processtime"));

                processtime = processtime + 5;

                speed = Integer.parseInt(theObject.getChildText("speed"));
                maxWait= Integer.parseInt(theObject.getChildText("maxWait"));

                //the <view> ... </view> node
                Element viewGroup = theObject.getChild("view");
                // read data
                image = viewGroup.getChildText("image");

                //get all the stations, where the object wants to go to
                //the <sequence> ... </sequence> node
                Element sequenceGroup = theObject.getChild("sequence");

                List<Element> allStations = sequenceGroup.getChildren("station");

                //get the elements into a list
                ArrayList<String> stationsToGo = new ArrayList<String>();

                for (Element theStation : allStations) {

                    stationsToGo.add(theStation.getText());

                }




                //creates multiple objects
                //in the XML-file is an extra tag <schleife></schleife>, so that one object in the XML-file can be multiplied
                for (int i = 0; i < schleife; i++) {
                    //creating a new TheObject object
                    stationsToGo=shuffelStationen(stationsToGo);
                    Student.create(label + "_" + i, stationsToGo, processtime, speed, XPOS_STARTSTATION, YPOS_STARTSTATION, image,maxWait);
                    Statistics.show(label +" Student wurde erzeugt" + i);
                }


            }

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> shuffelStationen(ArrayList<String> allStations) {
        String start= "Start_Station";
        String end="End_Station";
        String kasse= "Kasse";
        ArrayList<String>shuffelliste = new ArrayList<>();
        ArrayList<String>stationToGoNeu = new ArrayList<>();
        for(String s : allStations){
            if(s.equals(start)||s.equals(end)||s.equals(kasse)){

            }else{
             shuffelliste.add(s);
            }
        }
        //System.out.println(shuffelliste);
        Collections.shuffle(shuffelliste);
        //System.out.println(shuffelliste);
        stationToGoNeu.add(start);
        for(String s: shuffelliste){
            stationToGoNeu.add(s);
        }
        stationToGoNeu.add(kasse);
        stationToGoNeu.add(end);
        return stationToGoNeu;
    }

    /**
     * create some process stations out of the XML file
     */
    protected static void createProcessStations() {

        try {

            //read the information from the XML file into a JDOM Document
            Document theXMLDoc = new SAXBuilder().build(theStationDataFile);

            //the <settings> ... </settings> node
            Element root = theXMLDoc.getRootElement();

            //get all the stations into a List object
            List<Element> stations = root.getChildren("station");

            //separate every JDOM "station" Element from the list and create Java Station objects
            for (Element station : stations) {

                // data variables:
                String label = null;
                double troughPut = 0;
                int xPos = 0;
                int yPos = 0;
                String image = null;
                double preis = 0.0;
                boolean pOffenZ;
                String gruppierung= null;

                // read data
                label = station.getChildText("label");
                troughPut = Double.parseDouble(station.getChildText("troughput"));

                xPos = Integer.parseInt(station.getChildText("x_position"));
                yPos = Integer.parseInt(station.getChildText("y_position"));
                // the price
                preis = Double.parseDouble(station.getChildText("preis"));
                pOffenZ= Boolean.valueOf(station.getChildText("initZustand"));
                gruppierung= station.getChildText("gruppierung");
                //the <view> ... </view> node
                Element viewGroup = station.getChild("view");
                // read data
                image = viewGroup.getChildText("image");

                //CREATE THE INQUEUES

                //get all the inqueues into a List object
                List<Element> inqueues = station.getChildren("inqueue");

                //create a list of the stations inqueues
                ArrayList<SynchronizedQueue> theInqueues = new ArrayList<SynchronizedQueue>(); //ArrayList for the created inqueues

                for (Element inqueue : inqueues) {

                    int xPosInQueue = Integer.parseInt(inqueue.getChildText("x_position"));
                    int yPosInQueue = Integer.parseInt(inqueue.getChildText("y_position"));

                    //create the actual inqueue an add it to the list
                    theInqueues.add(SynchronizedQueue.createQueue(QueueViewJPanel.class, xPosInQueue, yPosInQueue));
                }

                //CREATE THE OUTQUEUES

                //get all the outqueues into a List object
                List<Element> outqueues = station.getChildren("outqueue");

                //create a list of the stations outqueues
                ArrayList<SynchronizedQueue> theOutqueues = new ArrayList<SynchronizedQueue>(); //ArrayList for the created outqueues

                for (Element outqueue : outqueues) {

                    int xPosOutQueue = Integer.parseInt(outqueue.getChildText("x_position"));
                    int yPosOutQueue = Integer.parseInt(outqueue.getChildText("y_position"));

                    //create the actual outqueue an add it to the list
                    theOutqueues.add(SynchronizedQueue.createQueue(QueueViewText.class, xPosOutQueue, yPosOutQueue));
                }

                //creating a new Station object
                MensaStationen.create(label, theInqueues, theOutqueues, troughPut, xPos, yPos, image, preis,pOffenZ,gruppierung);

            }


        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MensaStationen.CashRegisterLimitExceededException e) {
            e.printStackTrace();
        }

    }


    /**
     * create the start station
     */
    protected static void createStartStation() {

        try {

            //read the information from the XML file into a JDOM Document
            Document theXMLDoc = new SAXBuilder().build(theStartStationDataFile);

            //the <settings> ... </settings> node
            Element root = theXMLDoc.getRootElement();

            //get the start_station into a List object
            Element startStation = root.getChild("start_station");

            //get the label
            String label = startStation.getChildText("label");

            //get the position
            XPOS_STARTSTATION = Integer.parseInt(startStation.getChildText("x_position"));
            YPOS_STARTSTATION = Integer.parseInt(startStation.getChildText("y_position"));

            //the <view> ... </view> node
            Element viewGroup = startStation.getChild("view");

            // the image
            String image = viewGroup.getChildText("image");


            //CREATE THE INQUEUE
            //the <inqueue> ... </inqueue> node
            Element inqueueGroup = startStation.getChild("inqueue");

            // the positions
            int xPosInQueue = Integer.parseInt(inqueueGroup.getChildText("x_position"));
            int yPosInQueue = Integer.parseInt(inqueueGroup.getChildText("y_position"));

            //create the inqueue
            SynchronizedQueue theInQueue = SynchronizedQueue.createQueue(QueueViewText.class, xPosInQueue, yPosInQueue);

            //CREATE THE OUTQUEUE
            //the <outqueue> ... </outqueue> node
            Element outqueueGroup = startStation.getChild("outqueue");

            // the positions
            int xPosOutQueue = Integer.parseInt(outqueueGroup.getChildText("x_position"));
            int yPosOutQueue = Integer.parseInt(outqueueGroup.getChildText("y_position"));

            //create the outqueue
            SynchronizedQueue theOutQueue = SynchronizedQueue.createQueue(QueueViewText.class, xPosOutQueue, yPosOutQueue);

            //creating a new StartStation object
            MensaEntrance.create(label, theInQueue, theOutQueue, XPOS_STARTSTATION, YPOS_STARTSTATION, image);


        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * create the end station
     */
    protected static void createEndStation() {

        try {

            //read the information from the XML file into a JDOM Document
            Document theXMLDoc = new SAXBuilder().build(theEndStationDataFile);

            //the <settings> ... </settings> node
            Element root = theXMLDoc.getRootElement();

            //get the end_station into a List object
            Element endStation = root.getChild("end_station");

            //get label
            String label = endStation.getChildText("label");

            //position
            int xPos = Integer.parseInt(endStation.getChildText("x_position"));
            int yPos = Integer.parseInt(endStation.getChildText("y_position"));

            //the <view> ... </view> node
            Element viewGroup = endStation.getChild("view");
            // the image
            String image = viewGroup.getChildText("image");

            //CREATE THE INQUEUE
            //the <inqueue> ... </inqueue> node
            Element inqueueGroup = endStation.getChild("inqueue");

            // the positions
            int xPosInQueue = Integer.parseInt(inqueueGroup.getChildText("x_position"));
            int yPosInQueue = Integer.parseInt(inqueueGroup.getChildText("y_position"));

            //create the inqueue
            SynchronizedQueue theInQueue = SynchronizedQueue.createQueue(QueueViewText.class, xPosInQueue, yPosInQueue);

            //CREATE THE OUTQUEUE
            //the <outqueue> ... </outqueue> node
            Element outqueueGroup = endStation.getChild("outqueue");

            // the positions
            int xPosOutQueue = Integer.parseInt(outqueueGroup.getChildText("x_position"));
            int yPosOutQueue = Integer.parseInt(outqueueGroup.getChildText("y_position"));

            //create the outqueue
            SynchronizedQueue theOutQueue = SynchronizedQueue.createQueue(QueueViewText.class, xPosOutQueue, yPosOutQueue);

            //creating a new EndStation object
            MensaExit.create(label, theInQueue, theOutQueue, xPos, yPos, image);


        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setter for theObjectDataFile
     *
     * @param theObjectDataFile the objects XML data file
     */
    public static void setTheObjectDataFile(String theObjectDataFile) {
        Factory.theObjectDataFile = theObjectDataFile;
    }

    /**
     * setter for theStationDataFile
     *
     * @param theStationDataFile the station XML data file
     */
    public static void setTheStationDataFile(String theStationDataFile) {
        Factory.theStationDataFile = theStationDataFile;
    }

    /**
     * setter for theStartStationDataFile
     *
     * @param theStartStationDataFile the start-station XML data file
     */
    public static void setTheStartStationDataFile(String theStartStationDataFile) {
        Factory.theStartStationDataFile = theStartStationDataFile;
    }

    /**
     * setter for theEndStationDataFile
     *
     * @param theEndStationDataFile the end-station XML data file
     */
    public static void setTheEndStationDataFile(String theEndStationDataFile) {
        Factory.theEndStationDataFile = theEndStationDataFile;
    }

}