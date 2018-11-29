package model;

import controller.Simulation;
import io.Statistics;

import java.util.ArrayList;
import java.util.Collection;

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

    private MensaStationen(String label, ArrayList<SynchronizedQueue> inQueues, ArrayList<SynchronizedQueue> outQueues, double troughPut, int xPos, int yPos, String image, double preis) {
        super(label, inQueues, outQueues, troughPut, xPos, yPos, image);

       // this.preis = preis;
    }

    public static void create(String label, ArrayList<SynchronizedQueue> inQueues, ArrayList<SynchronizedQueue> outQueues, double troughPut, int xPos, int yPos, String image, double preis){
        new MensaStationen(label, inQueues, outQueues, troughPut, xPos, yPos, image, preis);
    }

    @Override
    protected void handleObject(TheObject theObject) {
        Statistics.show("EssenAusgabe");
        super.handleObject(theObject);
        Student s = (Student) theObject;
        s.measurement.guthaben++;
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


}
