package model;

import controller.Simulation;
import io.OurStatistic;
import io.Statistics;
import plotter.src.main.java.model.CustomPoint;
import plotter.src.main.java.view.PlotterPane;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * @author Team 5
 * @version 2018-12-03
 */
public class Student extends TheObject   {

    /**
     * static List of all Student Objects
     */
    private static ArrayList<Student> allStudents = new ArrayList<Student>();
    /**
     * start time of the Simulation
     */
    private static long startTime;
    /**
     * list of all diagramms
     */
    private ArrayList<PlotterPane> dataDias;
    /**
     * instance of measurement
     */
    Measurement measurement;
    /**
     * konstant value of max waiting time
     */
    private final int  maxWait;
    /**
     * the the when the object enter the inqueue
     */
    private long iqueueStartTime;
    /**
     * the actual Label
     */
    private String actualStaionLabel;

    /**
     * (private!) Constructor, creates a new object model and send it to the start station
     *
     * @param label        of the object
     * @param stationsToGo the stations to go
     * @param processtime  the processing time of the object, affects treatment by a station
     * @param speed        the moving speed of the object
     * @param xPos         x position of the object
     * @param yPos         y position of the object
     * @param image        image of the object
     */
    private Student(String label, ArrayList<String> stationsToGo, int processtime, int speed, int xPos, int yPos, String image,int pMaxWait) {

        super(label, stationsToGo, processtime, speed, xPos, yPos, image);
        this.maxWait = pMaxWait;
        measurement = new Measurement(this);
        allStudents.add(this);
        dataDias = new ArrayList<PlotterPane>();
        //initDias();
    }
    /** Create a new Student model
     *
     * @param label of the object
     * @param stationsToGo the stations to go
     * @param processtime the processing time of the object, affects treatment by a station
     * @param speed the moving speed of the object
     * @param xPos x position of the object
     * @param yPos y position of the object
     * @param image image of the object
     */
    public static void create(String label, ArrayList<String> stationsToGo, int processtime, int speed, int xPos, int yPos, String image,int pMaxWait) {

        new Student(label, stationsToGo, processtime, speed, xPos, yPos, image,pMaxWait);
        Statistics.show("Student create() methode wurde aufgerufen");
    }
/*
    private void initDias() {
        dataDias.add(new PlotterPane(new ArrayList<CustomPoint>(),600,400,true,"Kosten","Globaltime","GesamtEinnahmen"));
        dataDias.add(new PlotterPane(new ArrayList<CustomPoint>(),600,400,true,"WarteZeit","Globaltime","GesamtWarteZeit"));
    }*/

    /**
     * set the Start time
     * @param globalTime the time value
     */
    public static void setStartTime(long globalTime) {
        startTime= globalTime;
    }

    /**
     * getter the max waiting time
     * @return maxWait value
     */
    public int getMaxWait() {
        return maxWait;
    }

    /**
     * getter for InqueueStartTime
     * @return inqueueStartTime value
     */
    public long getInqueueStartTime() {
        return iqueueStartTime;
    }

    /**
     * enter the Inqueue
     * @param station the station from where the queue should be chosen
     */
    @Override
    protected void enterInQueue(Station station) {
        System.out.println("enter inqueue Student");
        super.enterInQueue(station);
        this.iqueueStartTime=Simulation.getGlobalTime();
        this.actualStaionLabel=station.getLabel();
    }

    /**
     * getter for the list of all diagrams
     * @return
     */
    public ArrayList<PlotterPane> getDataDias() {

        return dataDias;
    }

    /**
     * getter for the measurement
     * @return the measurement instance
     */
    public Measurement getMeasurement() {
        return this.measurement;
    }

    /**
     * get the next Station Object
     * @return
     */
    @Override
    protected Station getNextStation() {
        Station st;
        //we are at the end of the list
        if(this.stationsToGo.size() < stationListPointer) return null;

        //get the label of the next station from the list and increase the list pointer
        String stationLabel = this.stationsToGo.get(stationListPointer++);


        if(stationLabel.equals("End_Station")||stationLabel.equals("Start_Station")) {
            //looking for the matching station and return it
            for (Station station : Station.getAllStations()) {

                if (stationLabel.equals(station.getLabel())) {
                    return station;
                }


            }
        }
        //set the next Station to go after finding the one with the shortest Ingueue
        st=findeBesteMensaStation(stationLabel);


        return st; //the matching station isn't found
    }

    /**
     *
     * @param gruppierung the gruppierung of the next Station to go
     * @return the matching station
     */
    private Station findeBesteMensaStation(String gruppierung) {
        System.out.println("");
        switch(gruppierung){
            case "Salatebar":
                   return findeKuerzesteWarteSchlange(MensaStationen.getAllSalatBar());
            case"WarmesEssen":
                return findeKuerzesteWarteSchlange(MensaStationen.getAllWarmesEssen());

            case "Burger":
                return findeKuerzesteWarteSchlange(MensaStationen.getAllBurgerEssen());

            case "Kasse":
                return findeKuerzesteWarteSchlange(MensaStationen.getAllKassen());

        }
        return null;
    }

    /**
     *
     * @param groupList the list with all station of the specific kind
     * @return the matching station
     */
    private MensaStationen findeKuerzesteWarteSchlange(ArrayList<MensaStationen> groupList) {
        MensaStationen bufferStation= groupList.get(0);
        for(MensaStationen ms : groupList){
            if(ms.numberOfInQueueObjects()<bufferStation.numberOfInQueueObjects()){
                if(ms.isOffenZustand()==true){
                    bufferStation= ms;
                }

            }
        }
        if(bufferStation != null){
            return bufferStation;
        }
        return null;
    }


    /**
     * Print some statistics
     */
    protected void printStatistics() {
        super.printStatistics();
        Statistics.show("Der Student musss " + measurement.guthaben + "€ zahlen.");
        Statistics.show("Der Student hat insgesammt " + measurement.gesWarteZeit + " Ticks an den Stationen gewartet.");
    }

    /**
     * getter for allStudents list
     * @return ArrayList
     */
    public static ArrayList<Student> getAllStudents() {
        return allStudents;
    }

    @Override
    protected boolean work() {
        //the object is leaving the station -> set actual station to null
        this.actualStation = null;

        //choose the next station to go to
        Station station =  this.getNextStation();

        //only move if there is a next station found
        if(station == null) return false;

        //let the object move to the chosen station

        Statistics.show(this.getLabel() + " geht zur " + station.getLabel());

        //while target is not achieved
        while (!(station.getXPos() == this.xPos && station.getYPos() == this.yPos)) {
            //check is the station still open
            if(station instanceof MensaStationen){
                MensaStationen st = (MensaStationen)station;
                if (!(st.isOffenZustand())){
                    station=this.findeBesteMensaStation(st.getGruppierung());
                }
            }


            //move to the station
            if(station.getXPos() > this.xPos) this.xPos++;
            if(station.getYPos() > this.yPos) this.yPos++;

            if(station.getXPos() < this.xPos) this.xPos--;
            if(station.getYPos() < this.yPos) this.yPos--;

            //set our view to the new position
            ((Component) theView).setLocation(this.xPos, this.yPos);

            //let the thread sleep for the sequence time
            try {
                Thread.sleep(Simulation.SPEEDFACTOR *mySpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        Statistics.show(this.getLabel() + " erreicht " + station.getLabel());

        //the object has reached the station, now the object chooses an incoming queue and enter it
        this.enterInQueue(station);

        //wake up the station
        station.wakeUp();

        //work is done
        return false;

    }


    /**-------------------------------------------INNER--CLASS-------------------------------------------------*/
    /**
     * A (static) inner class for measurement jobs. The class records specific values of the object during the simulation.
     * These values can be used for statistic evaluation.
     */
    public static class Measurement extends Observable {
        /**
         * list of all Statistic points for gesWarteZeit
         */
       protected ArrayList<CustomPoint> gesWarteP;
        /**
         * list of all statistic points for guthaben
         */
       protected ArrayList<CustomPoint> guthabenP;



        private Student outObject;

        int gesWarteZeit=0;
        /**
         * the treated time by all processing stations, in seconds
         */
        int myTreatmentTime = 0;
        /**
         * number of payment
         */
        double guthaben = 0.0;

        /**
         * constructor
         */
        public Measurement(Student pOutObject) {

            this.outObject= pOutObject;
            gesWarteP= this.listInit();
            guthabenP= this.listInit();
            this.addObserver(OurStatistic.getObjectBeobachter());
        }

        /**
         * initilize the Statistic point List with the first point
         * @return
         */
        private ArrayList<CustomPoint> listInit() {
            ArrayList<CustomPoint> list= new ArrayList<CustomPoint>();
            list.add(new CustomPoint((int)Simulation.getGlobalTime(),0));
            return list;
        }

        /**
         * getter for the outer object
         * @return
         */
        public Student getOuterClass(){
            return outObject;
        }

        /**
         * change the guthaben value
         * @param preis the new guthaben value
         */
        void aenderGuthaben(double preis){
            this.guthaben= this.guthaben+preis;
            guthabenP.add(new CustomPoint((int)Simulation.getGlobalTime(),this.guthaben));
            notifyObservers(this.guthaben);
        }

        /**
         * cahnge the gesWarteZeit valueu
         * @param pWarteZeit the new warteZeit value
         */
        void aenderWarteZeit(int pWarteZeit){
            this.gesWarteZeit= this.gesWarteZeit+pWarteZeit;
            gesWarteP.add(new CustomPoint((int)Simulation.getGlobalTime(),this.gesWarteZeit));
            notifyObservers(this.gesWarteZeit);
        }

        /**
         * notified all Observer
         * @param arg the info
         */
        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }

        /**
         * getter for the list of all statistic points of gesWarteZeit
         * @return ArrayList
         */
        public ArrayList<CustomPoint> getGesWarteP() {
            return gesWarteP;
        }

        /**
         * getter  for the list of all statistic points of guthaben
         * @return ArrayList
         */
        public ArrayList<CustomPoint> getGuthabenP() {
            return guthabenP;
        }

        /**
         * getter for gesWarteZeit
         * @return gesWarteZeit value
         */
        public int getGesWarteZeit() {
            return gesWarteZeit;
        }

        /**
         * getter for guthaben
         * @return guthaben value
         */
        public double getGuthaben() {
            return guthaben;
        }


    }



}