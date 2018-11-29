package model;

import controller.Simulation;
import io.OurStatistic;
import io.Statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

/**
 * Beschreibung der Klasse MensaStationen.
 * Weiter Beschreibung
 * <p>
 * Die Klasse wurde am 28.November.2018 um 16:16 Uhr erstellt.
 *
 * @author Team5
 * @version 1.0
 */

public class MensaStationen extends ProcessStation{

    double preis;
    Measurement measurement;

    private static int maximalOfCashRegister = 3;

    private MensaStationen(String label, ArrayList<SynchronizedQueue> inQueues, ArrayList<SynchronizedQueue> outQueues, double troughPut, int xPos, int yPos, String image, double preis) {
        super(label, inQueues, outQueues, troughPut, xPos, yPos, image);
        measurement = new Measurement(this);
    }

    public static void create(String label, ArrayList<SynchronizedQueue> inQueues, ArrayList<SynchronizedQueue> outQueues, double troughPut, int xPos, int yPos, String image, double preis) throws CashRegisterLimitExceededException {

        //If MensaStation is labeled "Kasse" and if the maximal amount doesn't exceeed the limt create MensaStation with label "Kasse"
        if(label == "Kasse" && maximalOfCashRegister > 0){
            maximalOfCashRegister = maximalOfCashRegister -1;
            new MensaStationen(label, inQueues, outQueues, troughPut, xPos, yPos, image, preis);
            Statistics.show("Kasse erzeugt. Es können noch " + maximalOfCashRegister + " Kassen erzeugt werden.");
        }
        else if(label == "Kasse" && maximalOfCashRegister == 0){
            throw new CashRegisterLimitExceededException();
        }
        else{
            new MensaStationen(label, inQueues, outQueues, troughPut, xPos, yPos, image, preis);
        }
    }



    @Override
    protected void handleObject(TheObject theObject) {
        Statistics.show("EssenAusgabe");
        super.handleObject(theObject);
        Student s = (Student) theObject;
        s.measurement.aenderGuthaben();
        s.measurement.aenderWarteZeit((int)(Simulation.getGlobalTime()-s.getInqueueStartTime()));
        this.measurement.idleTime= super.measurement.idleTime;
        this.measurement.numbOfVisitedObjects= super.measurement.numbOfVisitedObjects;
        this.measurement.aenderInUseTime(super.measurement.inUseTime);
    }

    /**
     *
     * @return
     */
    @Override
    protected TheObject getNextInQueueObject() {
        System.out.println("next Object "+"\n");
        if(!(this.label.equals("Kasse"))){
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


    public static class CashRegisterLimitExceededException extends Exception{
        public CashRegisterLimitExceededException() {
        }
    }

    /**------------------------------------------------------------InnerClASS-------------------------------------------------------------------------------------------*/

    public static class Measurement extends Observable {

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
            notifyObservers(this.inUseTime);
        }


        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    }
}