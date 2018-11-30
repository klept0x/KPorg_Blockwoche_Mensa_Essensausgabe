package model;

import io.Statistics;
import io.XMLStatistics;

import javax.swing.*;

public class MensaExit extends EndStation {

    private static MensaExit theExit;


    /**
     * (private!) Constructor, creates a new end station
     *
     * @param label    of the station
     * @param inQueue  the incoming queue
     * @param outQueue the outgoing queue
     * @param xPos     x position of the station
     * @param yPos     y position of the station
     * @param image    image of the station
     */
    private MensaExit(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image) {
        super(label, inQueue, outQueue, xPos, yPos, image);
        theExit= this;
    }

    public static void create(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image){
        new MensaExit(label,inQueue,outQueue,xPos,yPos,image);
    }

    public static MensaExit getMensaExit() {
        return theExit;
    }

    /**
     * Show's a pop up messages where you can decide if you want to choose XML Files or JSON Files
     */
    public void datenAbfrage(){

        String[] option = {"Warteschlangen", "Gewinnermittlung"};
        try {
            int i = JOptionPane.showOptionDialog(null, "Welche Daten sollen ausgegeben werden?", "Daten", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
            if (option[i].equals("Warteschlangen")) {
                //TO DO;
                System.exit(0);
            } else if (option[i].equals("Gewinnermittlung")) {
                //TO DO;
                System.exit(0);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // e.printStackTrace();
            System.exit(0);
        }

    }


    /** End the simulation if the condition is met
     *
     *
     */
    @Override
    protected void endSimulation(){
        if(TheObject.getAllObjects().size() == numberOfOutQueueObjects()) {
            super.endSimulation();
            //datenAbfrage();
            Statistics.show("hier PopUp");

            XMLStatistics x1 = new XMLStatistics();
            x1.theXMLspeichern();
        }
    }

    public  boolean isEnd(){
        if(TheObject.getAllObjects().size() == numberOfOutQueueObjects()){
            return true;
        }
        return false;
    }
}
