package model;

import controller.Simulation;
import io.OurStatistic;
import io.Statistics;
import plotter.src.main.java.model.CustomPoint;
import plotter.src.main.java.view.PlotterPane;
import view.StationView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;

/**
 * Beschreibung der Klasse MensaStationen.
 * Weiter Beschreibung
 *
 * @author Team5
 * @version 1.0
 */

public class MensaStationen extends ProcessStation implements Cloneable{

    private static long startTime;
    
    /**
     * konstant value of maximal Inqueue objects before open another station
     */
    private static final int  maxSalatbarInObjects=3;

    /**
     * konstant value of maximal Inqueue objects before open another station
     */
    private static final int  maxWarmesEssenInObjects=3;

    /**
     * konstant value of maximal Inqueue objects before open another station
     */
    private static final int  maxBurgerInObjects=3;

    /**
     * konstant value of maximal Inqueue objects before open another station
     */
    private static final int maxKasseInObjects=4;

    /**
     * the prize of the station
     */
    double preis;

    /**
     * the kind of the station
     */
    private String gruppierung="";

    /**
     * the instance of maesurement
     */
    Measurement measurement;

    /**
     * list of all MensaStation Objects
     */
    private static ArrayList<MensaStationen> allMensaStation= new ArrayList<MensaStationen>();

    /**
     * list of all Salatbar Objects
     */
    private static ArrayList<MensaStationen> allSalatBar= new ArrayList<MensaStationen>();

    /**
     * list of all WarmesEssen objects
     */
    private static ArrayList<MensaStationen> allWarmesEssen= new ArrayList<MensaStationen>();

    /**
     * list of all Burgerstation objects
     */
    private static ArrayList<MensaStationen> allBurgerEssen= new ArrayList<MensaStationen>();

    /**
     * list of all Kasse objects
     */
    private static ArrayList<MensaStationen> allKassen= new ArrayList<MensaStationen>();

    /**
     * boolean if the station open or not
     */
    private boolean offenZustand;

    /**
     * List of all Diagramms
     */
    private HashMap<String,PlotterPane> datenDias;

    /** The maximal amount of MensaStation with label "Kasse" is 3 (Singelton Pattern)*/
    private static int maximalAmountCashRegister = 3;

    /**
     *  constructor of Mensastation
     * @param label label of the Station
     * @param inQueues Inqueue of the Station
     * @param outQueues Outqueue of the Station
     * @param troughPut the Troughput of the Statiom
     * @param xPos x Position
     * @param yPos y Position
     * @param image the Image
     * @param pPreis the prize
     * @param pOffenZ the status
     * @param pGruppe the kind
     */
    private MensaStationen(String label, ArrayList<SynchronizedQueue> inQueues, ArrayList<SynchronizedQueue> outQueues, double troughPut, int xPos, int yPos, String image, double pPreis,boolean pOffenZ,String pGruppe) {
        super(label, inQueues, outQueues, troughPut, xPos, yPos, image);
        measurement = new Measurement(this);
        this.preis=pPreis;
        this.offenZustand=pOffenZ;
        this.gruppierung= pGruppe;
        System.out.println(this.label+" "+this.gruppierung);
        allMensaStation.add(this);
        trageKategorieListe(this);
        datenDias= new HashMap<String,PlotterPane>();
        System.out.println(this.label+" "+pOffenZ);

        //initDias();
    }

    private void initDias() {
        datenDias.put("inUseTime",new PlotterPane(new ArrayList<CustomPoint>(),200,200,true,"time","InUseTime",this.label+" InUseTime/time"));
        datenDias.put("idleTime",new PlotterPane(new ArrayList<CustomPoint>(),200,200,true,"time","IdleTime",this.label+" IdleTime/time"));
        datenDias.put("numberOVO",new PlotterPane(new ArrayList<CustomPoint>(),200,200,true,"time","numberOfVisitedObjects" ,this.label+" numberOfVisitedObjects/time"));
    }

    /**
     * create the Station
     * @param label label of the Station
     * @param inQueues Inqueue of the Station
     * @param outQueues Outqueue of the Station
     * @param troughPut the Troughput of the Statiom
     * @param xPos x Position
     * @param yPos y Position
     * @param image the Image
     * @param preis the prize
     * @param pOffenZ the status
     * @param pGruppe the kind
     * @throws CashRegisterLimitExceededException
     */
    public static void create(String label, ArrayList<SynchronizedQueue> inQueues, ArrayList<SynchronizedQueue> outQueues, double troughPut, int xPos, int yPos, String image, double preis,boolean pOffenZ,String pGruppe) throws CashRegisterLimitExceededException {
        //Statistics.show("create MensaStation");
        //If label is "Kasse" and if the "maximalAmountCashRegister" amount isn't "0" create MensaStation with label "Kasse"
        if(label == "Kasse" && maximalAmountCashRegister > 0 ){
            //counts down for each MensaStation with label "Kasse"
            maximalAmountCashRegister = maximalAmountCashRegister -1;
            new MensaStationen(label, inQueues, outQueues, troughPut, xPos, yPos, image, preis,pOffenZ,pGruppe);
            //Statistics.show("Kasse erzeugt. Es können noch " + maximalAmountCashRegister + " Kassen erzeugt werden.");
        }
        else if(label == "Kasse" && maximalAmountCashRegister == 0){
            throw new CashRegisterLimitExceededException();
        }
        else{
            new MensaStationen(label, inQueues, outQueues, troughPut, xPos, yPos, image, preis,pOffenZ,pGruppe);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        throw new CloneNotSupportedException("To clone an object of the class 'MensaStation' is not allowed.");
    }


    /**
     * add the station in the specific list
     * @param mensaStationen the Station
     */
    private void trageKategorieListe(MensaStationen mensaStationen) {
        switch (mensaStationen.gruppierung){
            case "Salatebar":
                allSalatBar.add(mensaStationen);
                break;
            case"WarmesEssen":
                allWarmesEssen.add(mensaStationen);
                break;
            case "Burger":
                allBurgerEssen.add(mensaStationen);
                break;
            case"Kasse":
                allKassen.add(mensaStationen);
        }
    }

    /**
     * se the Starttime of the Simulation
     * @param globalTime the global time
     */
    public static void setStartTime(long globalTime) {
        startTime =globalTime;
    }


    /**
     * handle the object
     * @param theObject
     */
    @Override
    protected void handleObject(TheObject theObject) {

        Statistics.show(this.label);
        this.pruefeFreischalten();
        Student s = (Student) theObject;
        System.out.println(s.getLabel()+" "+s.measurement.guthaben);
        s.measurement.aenderGuthaben(preis);
        System.out.println(s.getLabel()+" "+s.measurement.guthaben);
        s.measurement.aenderWarteZeit((int)(Simulation.getGlobalTime()-s.getInqueueStartTime()));
        super.handleObject(theObject);
        this.measurement.aenderNumOV(super.measurement.numbOfVisitedObjects);
        this.measurement.aenderInUseTime(super.measurement.inUseTime);
    }




    /**
     *get the Next Inqueue Object
     * @return The Object
     */
    @Override
    protected TheObject getNextInQueueObject() {
        System.out.println("next Object "+"\n");
        if(!(this.gruppierung.equals("Kasse"))){
            checkObjectWarte();
        }
        return super.getNextInQueueObject();
    }


    /**
     * get all Inqueue objects
     * @return Collection <TheObject>
     */
    @Override
    protected Collection<TheObject> getNextInQueueObjects() {
        ArrayList<TheObject> liste = new ArrayList<TheObject>();
        for(SynchronizedQueue sq : getAllInQueues()){
            Object[] s = sq.toArray();
            for(int i= 0;i<s.length;i++){
                liste.add((TheObject) s[i]);
            }
        }
        return liste;
    }

    /**
     * remove the specific Object from the Inqueue
     * @param o the Object
     */
    private void loescheAusInqueue(TheObject o) {
        System.out.println(o.label+" loesche aus Inqueue");
        String objectlabel= o.label;
        for(SynchronizedQueue sy: getAllInQueues() ){
            Object[] sys= sy.toArray();
            for (int i = 0; i <sys.length ; i++) {
                TheObject ob=(TheObject)sys[i];
                if(ob.label.equals(objectlabel)) {
                    sy.remove(o);
                }
            }
        }
    }

    /**
     * check if the removed Object is the next Object that should be handled
     *
     * @param o the Object
     * @return true if the Object the next and false is not
     */
    private boolean checkIndexHead(TheObject o) {
        ArrayList<TheObject> liste = (ArrayList<TheObject>)getNextInQueueObjects();
        TheObject headElement= liste.get(0);
        if(o.label.equals(headElement.label)){
            return true;
        }
        return false;
    }

    /**
     * check if the waiting time is bigger than the specific max Waiting time for the Object
     */
    private void checkObjectWarte() {
        long actuelTime=Simulation.getGlobalTime();
        for(TheObject o: getNextInQueueObjects()){
            Student student = (Student) o;
            int inQueueWaitTime= (int)(actuelTime-student.getInqueueStartTime());
            if(inQueueWaitTime>=student.getMaxWait()&& checkIndexHead(o)!=true){
                System.out.println("juhu "+student.getMaxWait()+" "+inQueueWaitTime);
                loescheAusInqueue(o);
                o.wakeUp();
            }
        }

    }

    /**
     * get the startTime
     * @return startTime
     */
    public static long getStartTime() {
        return startTime;
    }

    /**
     * get the measurement instance
     * @return the measurement instance
     */
    public Measurement getMeasurement() {
        return this.measurement;
    }


    public static class CashRegisterLimitExceededException extends Exception{
        public CashRegisterLimitExceededException() {
        }
    }

    /**
     * Getter for all existing Mensastations
     *
     * @return returns a ArrayList of the existing Mensastations
     */
    public static ArrayList<MensaStationen> getAllMensaStation() {
        return allMensaStation;
    }

    /**
     * get the list of all diagramms
     * @return
     */
    public HashMap<String,PlotterPane> getDatenDias() {
        return datenDias;
    }


    /*public static void setzeVisible() {
       allMensaStation.get(0).getDatenDias().get(0).setVisible(true);
    }*/

    /**
     * Methode for checking idleTime
     */
    public  void pruefeIdleTime(){
        if (numberOfInQueueObjects() == 0 && numberOfOutQueueObjects() == 0 && (this.offenZustand!=false)){
            if(this.label.equals("Kasse")){
                System.out.println(this.label+" "+this.measurement.idleTime+"---------------------------------");
            }
           this.measurement.aenderIdleTime(this.measurement.idleTime+=1);

        }
    }

    /**
     * checking idleTime every heartbeat
     */
    public static void tacktruf(){
        if(Simulation.isRunning && !(MensaExit.getMensaExit().isEnd())){
            for (MensaStationen ms : getAllMensaStation()){
                ms.pruefeIdleTime();
            }
        }

    }

    /**
     * check wich kind the station is
     */
    private void pruefeFreischalten() {

        switch (this.gruppierung){
            case "Salatebar":
                if(this.numberOfInQueueObjects()>=maxSalatbarInObjects){
                    this.oeffneStation(allSalatBar,"saladGruen.png");
                }
                break;
            case"WarmesEssen":
                if(this.numberOfInQueueObjects()>=maxWarmesEssenInObjects){
                    this.oeffneStation(allWarmesEssen,"fleischGruen.png");
                }
                break;
            case "Burger":
                if(this.numberOfInQueueObjects()>=maxBurgerInObjects){
                    this.oeffneStation(allBurgerEssen,"burgerinoGruen.png");
                }
                break;
            case "Kasse":
                if(this.numberOfInQueueObjects()>=maxKasseInObjects){
                    this.oeffneStation(allKassen,"kasseGruen.png");
                }
                break;
        }
    }

    /**
     *open one Station of the same kind and change the image
     * @param liste list of all Object from a specific kind
     */
    private void oeffneStation(ArrayList<MensaStationen> liste ,String image ) {
        int zaehler=0;
        for (MensaStationen m : liste){
            if(m.offenZustand== false&& zaehler==0){
                m.offenZustand= true;
                m.theView.setIcon(new ImageIcon(image));
                System.out.println(m.label+" wurde geöffnet");
                zaehler=1;
            }
            System.out.println("\n"+"keine weitere Station gefunden");
        }
    }

    /**
     * check wich kind the Station has and close one station till one is still open
     */
    private void pruefeSchliessseStation(){
        switch (this.gruppierung){
            case "Salatebar":
                  this.schliesseStation(allSalatBar,"saladRot.png");
                break;
            case"WarmesEssen":
                    this.schliesseStation(allWarmesEssen,"fleischRot.png");
                break;
            case "Burger":
                   this.schliesseStation(allBurgerEssen,"burgerinoRot.png");
                break;
            case "Kasse":
                    this.schliesseStation(allKassen,"kasseRot.png");
                break;
        }
    }

    /**
     * close one Station till one is still open and change the image
     * @param gruppeList list of the specific Station
     * @param image the image
     */
    private void schliesseStation(ArrayList<MensaStationen> gruppeList,String image ) {
       int zaehlerZu=0;
       for (MensaStationen ms : gruppeList ){
           if(ms.offenZustand==false){
               zaehlerZu++;
           }
       }
       if(zaehlerZu<(gruppeList.size()-1)){
           this.offenZustand=false;
           this.theView.setIcon(new ImageIcon(image));
       }
    }

    /**
     * get the list of all Salatbar Objects
     * @return ArrayList
     */
    public static ArrayList<MensaStationen> getAllSalatBar() {
        return allSalatBar;
    }

    /**
     * get the list of all Warmes Essen Objects
     * @return ArrayList
     */
    public static ArrayList<MensaStationen> getAllWarmesEssen() {
        return allWarmesEssen;
    }

    /**
     * get the list of all Burger station Objects
     * @return ArrayList
     */
    public static ArrayList<MensaStationen> getAllBurgerEssen() {
        return allBurgerEssen;
    }

    /**
     * get the list of all Kassen Objects
     * @return ArrayList
     */
    public static ArrayList<MensaStationen> getAllKassen() {
        return allKassen;
    }

    /**
     * get the status
     * @return boolean
     */
    public boolean isOffenZustand() {
        return offenZustand;
    }

    /**
     * print the statistic
     */
    @Override
    public void printStatistics() {
        super.printStatistics();
        String theString = "\nZeit ohne Betrieb: " + measurement.idleTime;
        Statistics.show(theString);
    }

    /**
     * getter for gruppierung
     * @return gruppierung value
     */
    public String getGruppierung() {
        return gruppierung;
    }



    /**------------------------------------------------------------InnerClASS-------------------------------------------------------------------------------------------*/

    public static class Measurement extends Observable {
        /**
         * List of all Statistic points for inUseTime
         */
        protected ArrayList<CustomPoint>inUseTimeP;
        /**
         * List of all Statistic points for numberOfVisitedObjects
         */
        protected ArrayList<CustomPoint>numberOVP;
        /**
         * List of all Statistic points for idleTime
         */
        protected ArrayList<CustomPoint>idleTimeP;


        /**
         *  the object of the station
         */
        protected MensaStationen theOutObject;
        /**
         * the total time the station is in use
         */
        protected int inUseTime = 0;

        /**
         * the number of all objects that visited this station
         */
        protected int numbOfVisitedObjects = 0;
        /**
         * the total time the station is open but not in use
         */
        protected int idleTime = 0;

        /**
         * counter of idle time for closing the station
         */
        protected int bufferedIdleTimeTick= 0;

        /**
         * constructor from measurement
         * @param outObject the outer Object
         */
        public Measurement( MensaStationen outObject) {
            theOutObject= outObject;
            inUseTimeP= new ArrayList<CustomPoint>();
            idleTimeP= new ArrayList<CustomPoint>();
            numberOVP= new ArrayList<CustomPoint>();
            this.addObserver(OurStatistic.getMensaBeo());

        }

        /**
         * get the station object of this measurement
         * @return the MensaStation Object
         */
        public MensaStationen getOuterClass() {
            return theOutObject;
        }

        /**
         * change the InUse field
         * @param pInUseTime new InUse value
         */
        void aenderInUseTime(int pInUseTime){
            this.inUseTime= pInUseTime;
            this.inUseTimeP.add(new CustomPoint((int)Simulation.getGlobalTime(),this.inUseTime));
            notifyObservers("inUseTime");
        }

        /**
         * notfied all Observers
         * @param arg  the info
         */
        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }

        /**
         * getter for InUse value
         * 
         * @return
         */
        public int getInUseTime() {
            return inUseTime;
        }

        /**
         * getter for numberOfVisitedObject value
         * @return
         */
        public int getNumbOfVisitedObjects() {
            return numbOfVisitedObjects;
        }

        /**
         * getter for IdleTime value
         * @return
         */
        public int getIdleTime() {
            return idleTime;
        }

        /**
         * change the IdleTime field
         * @param idleTime new IdleTime value
         */
        public void aenderIdleTime(int idleTime) {
           if(bufferedIdleTimeTick<6) {
               bufferedIdleTimeTick++;
               this.idleTime = idleTime;
               this.idleTimeP.add(new CustomPoint((int) (Simulation.getGlobalTime()), this.idleTime));
               notifyObservers("idleTime");
           }else{
            theOutObject.pruefeSchliessseStation();
            bufferedIdleTimeTick=0;
           }
        }

        /**
         * change the number of visited Objects
         * @param numbOfVisitedObjects new numberOfVisitedObjects value
         */
        public void aenderNumOV(int numbOfVisitedObjects) {
            this.numbOfVisitedObjects= numbOfVisitedObjects;
            this.numberOVP.add(new CustomPoint((int)Simulation.getGlobalTime(),this.numbOfVisitedObjects));
            this.notifyObservers("numberOVO");
        }

        /**
         * getter for the List of statistic Points
         * @return ArrayList
         */
        public ArrayList<CustomPoint> getInUseTimeP() {
            return inUseTimeP;
        }

        /**
         * getter for the List of statistic Points
         * @return ArrayList
         */
        public ArrayList<CustomPoint> getNumberOVP() {
            return numberOVP;
        }

        /**
         * getter for the List of statistic Points
         * @return ArrayList
         */
        public ArrayList<CustomPoint> getIdleTimeP() {
            return idleTimeP;
        }
    }
}