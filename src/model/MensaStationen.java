package model;

import controller.Simulation;
import io.OurStatistic;
import io.Statistics;
import plotter.src.main.java.model.CustomPoint;
import plotter.src.main.java.view.PlotterPane;

import java.util.ArrayList;
import java.util.Collection;
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
    private static final int  maxSalatbarInObjects=6;
    private static final int  maxWarmesEssenInObjects=6;
    private static final int  maxBurgerInObjects=6;
    double preis;
    private String gruppierung="";
    Measurement measurement;
    private static ArrayList<MensaStationen> allMensaStation= new ArrayList<MensaStationen>();
    private static ArrayList<MensaStationen> allSalatBar= new ArrayList<MensaStationen>();
    private static ArrayList<MensaStationen> allWarmesEssen= new ArrayList<MensaStationen>();
    private static ArrayList<MensaStationen> allBurgerEssen= new ArrayList<MensaStationen>();
    private static ArrayList<MensaStationen> allKassen= new ArrayList<MensaStationen>();
    private boolean offenZustand;
    private ArrayList<PlotterPane> datenDias;

    /** The maximal amount of MensaStation with label "Kasse" is 3 (Singelton Pattern)*/
    private static int maximalAmountCashRegister = 3;


    private static final int maxKasseInObjects = 4;

    /**
     *
     * @param label
     * @param inQueues
     * @param outQueues
     * @param troughPut
     * @param xPos
     * @param yPos
     * @param image
     * @param pPreis
     * @param pOffenZ
     * @param pGruppe
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
        datenDias= new ArrayList<PlotterPane>();
        System.out.println(this.label+" "+pOffenZ);

        //initDias();
    }

    /*private void initDias() {
        datenDias.add(new PlotterPane(new ArrayList<CustomPoint>(),800,600,true,"Benutzungszeit","Globaltime","InUseTime"));
        datenDias.add(new PlotterPane(new ArrayList<CustomPoint>(),800,600,true,"Zeit ohne Object","Globaltime","IdleTime"));
        datenDias.add(new PlotterPane(new ArrayList<CustomPoint>(),800,600,true,"Anzahl der Visitors","Globaltime","numberOfVisitedObject"));
    }*/

    /**
     *
     * @param label
     * @param inQueues
     * @param outQueues
     * @param troughPut
     * @param xPos
     * @param yPos
     * @param image
     * @param preis
     * @param pOffenZ
     * @param pGruppe
     * @throws CashRegisterLimitExceededException is thrown when there are already 3 MensaStation with label "Kasse"
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


    public static void setStartTime(long globalTime) {
        startTime =globalTime;
    }



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
     *
     * @return
     */
    @Override
    protected TheObject getNextInQueueObject() {
        System.out.println("next Object "+"\n");
        if(!(this.gruppierung.equals("Kasse"))){
            checkObjectWarte();
        }
        return super.getNextInQueueObject();
    }




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
     *
     * @param o
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
     *
     * @param o
     * @return
     */
    private boolean checkIndexHead(TheObject o) {
        ArrayList<TheObject> liste = (ArrayList<TheObject>)getNextInQueueObjects();
        TheObject headElement= liste.get(0);
        if(o.label.equals(headElement.label)){
            return true;
        }
        return false;
    }


    private void checkObjectWarte() {
        long actuelTime=Simulation.getGlobalTime();
        for(TheObject o: getNextInQueueObjects()){

            int inQueueWaitTime= (int)(actuelTime-o.getInqueueStartTime());
            if(inQueueWaitTime>=o.getMaxWait()&& checkIndexHead(o)!=true){
                System.out.println("juhu "+o.getMaxWait()+" "+inQueueWaitTime);
                loescheAusInqueue(o);
                o.wakeUp();
            }
        }

    }

    public static long getStartTime() {
        return startTime;
    }

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

    public ArrayList<PlotterPane> getDatenDias() {
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
     * check if we need to open another station
     */
    private void pruefeFreischalten() {

        switch (this.gruppierung){
            case "Salatebar":
                if(this.numberOfInQueueObjects()>=maxSalatbarInObjects){
                    this.oeffneStation(allSalatBar);
                }
                break;
            case"WarmesEssen":
                if(this.numberOfInQueueObjects()>=maxWarmesEssenInObjects){
                    this.oeffneStation(allWarmesEssen);
                }
                break;
            case "Burger":
                if(this.numberOfInQueueObjects()>=maxBurgerInObjects){
                    this.oeffneStation(allBurgerEssen);
                }
                break;
            case "Kasse":
                if(this.numberOfInQueueObjects()>=maxKasseInObjects){
                    this.oeffneStation(allKassen);
                }
                break;
        }
    }

    /**
     *
     * @param liste list of all Objects from a specific kind
     */
    private void oeffneStation(ArrayList<MensaStationen> liste ) {
        int zaehler=0;
        for (MensaStationen m : liste){
            if(m.offenZustand== false&& zaehler==0){
                m.offenZustand= true;

                System.out.println(m.label+" wurde geöffnet");
                zaehler=1;
            }
            System.out.println("\n"+"keine weitere Station gefunden");
        }
    }

    public static ArrayList<MensaStationen> getAllSalatBar() {
        return allSalatBar;
    }

    public static ArrayList<MensaStationen> getAllWarmesEssen() {
        return allWarmesEssen;
    }

    public static ArrayList<MensaStationen> getAllBurgerEssen() {
        return allBurgerEssen;
    }

    public static ArrayList<MensaStationen> getAllKassen() {
        return allKassen;
    }

    public boolean isOffenZustand() {
        return offenZustand;
    }

    /**------------------------------------------------------------InnerClASS-------------------------------------------------------------------------------------------*/

    public static class Measurement extends Observable {

        protected ArrayList<CustomPoint>inUseTimeP;
        protected ArrayList<CustomPoint>numberOVP;
        protected ArrayList<CustomPoint>idleTimeP;



        protected MensaStationen theOutObject;
        /**
         * the total time the station is in use
         */
        protected int inUseTime = 0;

        /**
         * the number of all objects that visited this station
         */
        protected int numbOfVisitedObjects = 0;

        protected int idleTime = 0;

        public Measurement( MensaStationen outObject) {
            theOutObject= outObject;
            inUseTimeP= new ArrayList<CustomPoint>();
            idleTimeP= new ArrayList<CustomPoint>();
            numberOVP= new ArrayList<CustomPoint>();
            this.addObserver(OurStatistic.getMensaBeo());

        }

        /**
         * Get the average time for treatment
         *
         * @return the average time for treatment
         */
       /* protected int avgTreatmentTime() {

            if (numbOfVisitedObjects == 0) return 0; //in case that a station wasn't visited
            else
                return inUseTime / numbOfVisitedObjects;

        }*/


        public MensaStationen getOuterClass() {
            return theOutObject;
        }

        void aenderInUseTime(int pInUseTime){
            this.inUseTime= pInUseTime;
            this.inUseTimeP.add(new CustomPoint((int)Simulation.getGlobalTime(),this.inUseTime));
            notifyObservers(this.inUseTime);
        }

        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }

        public int getInUseTime() {
            return inUseTime;
        }

        public int getNumbOfVisitedObjects() {
            return numbOfVisitedObjects;
        }

        public int getIdleTime() {
            return idleTime;
        }

        public void aenderIdleTime(int idleTime) {

            this.idleTime= idleTime;
            this.idleTimeP.add(new CustomPoint((int)(Simulation.getGlobalTime()),this.idleTime));
            notifyObservers(this.idleTime);
        }

        public void aenderNumOV(int numbOfVisitedObjects) {
            this.numbOfVisitedObjects= numbOfVisitedObjects;
            this.numberOVP.add(new CustomPoint((int)Simulation.getGlobalTime(),this.numbOfVisitedObjects));
            this.notifyObservers(this.numbOfVisitedObjects);
        }

        public ArrayList<CustomPoint> getInUseTimeP() {
            return inUseTimeP;
        }

        public ArrayList<CustomPoint> getNumberOVP() {
            return numberOVP;
        }

        public ArrayList<CustomPoint> getIdleTimeP() {
            return idleTimeP;
        }

    }
}