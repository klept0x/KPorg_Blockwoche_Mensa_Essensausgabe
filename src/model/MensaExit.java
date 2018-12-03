package model;

import io.OurStatistic;
import io.Statistics;
import io.XMLStatistics;

import javax.swing.*;
import java.util.ArrayList;

public class MensaExit extends EndStation {
    /**
     * the instance of the EndStation
     */
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
    /** creates a new MensaExit
     *
     * @param label of the station
     * @param inQueue the incoming queue
     * @param outQueue the outgoing queue
     * @param xPos x position of the station
     * @param yPos y position of the station
     * @param image image of the station
     */
    public static void create(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image){
        new MensaExit(label,inQueue,outQueue,xPos,yPos,image);
    }

    /**
     * getter for the EndStation
     * @return
     */
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

            ArrayList<String> op= new ArrayList<String>();
            for (MensaStationen stationen : MensaStationen.getAllMensaStation()){
                op.add(stationen.label);
            }
            for (Student st : Student.getAllStudents()){
                op.add(st.label);
            }
            String[] optionen = op.toArray(new String[op.size()]);
            for (int i = 0; i <optionen.length ; i++) {
                System.out.println(optionen[i]);
            }
            String theOption = (String) JOptionPane.showInputDialog(null,"Wählen sie ein Objekt aus ","Statistik",JOptionPane.QUESTION_MESSAGE,null,optionen,optionen[0]);
            OurStatistic.printEndstatistic(theOption);
            XMLStatistics x1 = new XMLStatistics();
            x1.theXMLspeichern();

        }
    }

    /**
     * check if all Objects had arrived the EndStation
     * @return boolean
     */
    public  boolean isEnd(){
        if(TheObject.getAllObjects().size() == numberOfOutQueueObjects()){
            return true;
        }
        return false;
    }

}
