package model;

import controller.Simulation;
import io.OurStatistic;
import io.Statistics;
import plotter.src.main.java.model.CustomPoint;
import plotter.src.main.java.view.PlotterPane;

import java.util.ArrayList;
import java.util.Observable;

public class Student extends TheObject   {

//    private double guthaben;

    private static ArrayList<Student> allStudents = new ArrayList<Student>();
    private static long startTime;
    private ArrayList<PlotterPane> dataDias;
    private ArrayList<CustomPoint> kostenpunkte= new ArrayList<CustomPoint>();
    private ArrayList<CustomPoint> warteZeitPoints= new ArrayList<CustomPoint>();

    Measurement measurement;
    private int maxWait;
    private long iqueueStartTime;

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

        super(label, stationsToGo, processtime, speed, xPos, yPos, image,pMaxWait);
        this.maxWait=pMaxWait;
        measurement= new Measurement(this);
        allStudents.add(this);
        kostenpunkte.add(new CustomPoint((int)Simulation.getGlobalTime(),this.measurement.guthaben));
        warteZeitPoints.add(new CustomPoint((int)Simulation.getGlobalTime(),this.measurement.gesWarteZeit));
        dataDias= new ArrayList<PlotterPane>();
        initDias();
    }

    public static void setStartTime(long globalTime) {
        startTime= globalTime;
    }

    private void initDias() {
        dataDias.add(new PlotterPane(new ArrayList<CustomPoint>(),600,400,true,"Kosten","Globaltime","GesamtEinnahmen"));
        dataDias.add(new PlotterPane(new ArrayList<CustomPoint>(),600,400,true,"WarteZeit","Globaltime","GesamtWarteZeit"));
    }


    public static void create(String label, ArrayList<String> stationsToGo, int processtime, int speed, int xPos, int yPos, String image,int pMaxWait) {

        new Student(label, stationsToGo, processtime, speed, xPos, yPos, image,pMaxWait);
        Statistics.show("Student create() methode wurde aufgerufen");
    }

    public int getMaxWait() {
        return maxWait;
    }

    public long getInqueueStartTime() {
        return iqueueStartTime;
    }

    @Override
    protected void enterInQueue(Station station) {
        System.out.println("enter inqueue Student");
        super.enterInQueue(station);
        this.iqueueStartTime=Simulation.getGlobalTime();
    }

    public ArrayList<PlotterPane> getDataDias() {
        return dataDias;
    }

    public Measurement getMeasurement() {
        return this.measurement;
    }

    /**
     * A (static) inner class for measurement jobs. The class records specific values of the object during the simulation.
     * These values can be used for statistic evaluation.
     */
    public static class Measurement extends Observable {

       protected ArrayList<CustomPoint> gesWarteP;
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

        private ArrayList<CustomPoint> listInit() {
            ArrayList<CustomPoint> list= new ArrayList<CustomPoint>();
            list.add(new CustomPoint((int)Simulation.getGlobalTime(),0));
            return list;
        }

        public Student getOuterClass(){
            System.out.println();
            return outObject;
        }

        void aenderGuthaben(double preis){
            this.guthaben= this.guthaben+preis;
            notifyObservers(this.guthaben);
        }

        void aenderWarteZeit(int pWarteZeit){
            this.gesWarteZeit= this.gesWarteZeit+pWarteZeit;

            notifyObservers(this.gesWarteZeit);
        }

        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }

        public ArrayList<CustomPoint> getGesWarteP() {
            return gesWarteP;
        }

        public ArrayList<CustomPoint> getGuthabenP() {
            return guthabenP;
        }

        public int getGesWarteZeit() {
            return gesWarteZeit;
        }

        public double getGuthaben() {
            return guthaben;
        }


    }

    /**
     * Print some statistics
     */
    protected void printStatistics() {
        super.printStatistics();
        Statistics.show("Der Student musss " + measurement.guthaben + "€ zahlen.");
        Statistics.show("Der Student hat insgesammt " + measurement.gesWarteZeit + " Ticks an den Stationen gewartet.");
    }

    public static ArrayList<Student> getAllStudents() {
        return allStudents;
    }


    public static long getStartTime() {
        return startTime;
    }

    public static void setzeVisible() {
       allStudents.get(0).getDataDias().get(0).setVisible(true);
    }
}