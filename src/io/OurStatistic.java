package io;


import model.MensaStationen;
import model.Student;
import plotter.src.main.java.view.PlotterPane;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Team 5
 * @version 2018-11-30
 */
public class OurStatistic extends Statistics {

    /**
     * instance of Objectbeobachter
     */
    static Objectbeobachter obectb= new Objectbeobachter();
    /**
     * instance of MensaStationbeobachter
     */
    static MensaStationenBeobachter mensaBeo= new MensaStationenBeobachter();




    /**
     *  getter for the observer instance for Student
     * @return observer instance
     */
    public static Objectbeobachter getObjectBeobachter() {
        return obectb;
    }
    /**
     *  getter for the observer instance for MernsaStation
     * @return observer instance
     */
    public static MensaStationenBeobachter getMensaBeo() {
        return mensaBeo;
    }

    /**
     * print the Endstatistic of the choosen object
     * @param label the label of the chossen object
     */
    public static void printEndstatistic(String label){
        for(MensaStationen ms : MensaStationen.getAllMensaStation()){
            if (ms.getLabel().equals(label)){
                MensaStationen.Measurement m = ms.getMeasurement();
                PlotterPane p1 = new PlotterPane(m.getIdleTimeP(),200,200,true,"time","IdleTime",ms.getLabel()+" IdleTime/time");
                p1.setVisible(true);
                PlotterPane p2 = new PlotterPane(m.getInUseTimeP(),200,200,true,"time","InUseTime",ms.getLabel()+" InUseTime/time");
                 p2.setVisible(true);
                PlotterPane p3 = new PlotterPane(m.getNumberOVP(),200,200,true,"time","numberOfVisitedObjects" ,ms.getLabel()+" numberOfVisitedObjects/time");
                p3.setVisible(true);
            }
        }

        for(Student st : Student.getAllStudents()){
            if (st.getLabel().equals(label)){
                Student.Measurement m = st.getMeasurement();
                PlotterPane p1 = new PlotterPane(m.getGesWarteP(),200,200,true,"time","GesammtWartezeit",st.getLabel()+" GesammtWartezeit/time");
                p1.setVisible(true);
                PlotterPane p2 = new PlotterPane(m.getGuthabenP(),200,200,true,"time","Rechnungsbetrag",st.getLabel()+" Rechnungsbetrag/time");
                p2.setVisible(true);

            }
        }

    }



/**----------------------------------------------------------------------INNER CLASSES-------------------------------------------------------------------------------------*/



        static class Objectbeobachter implements Observer {

    /**
     * update the statistic
     * @param o the measuremnet instance
     * @param arg the info
     */
    @Override
        public void update(Observable o, Object arg) {
        System.out.println("\n"+" Student update");
        /*Student.Measurement s = (Student.Measurement)o;
        Student theStudent = s.getOuterClass();
        trageDatenEin(s);
        System.out.println(theStudent.getLabel());*/

        }

    /**
     * trage daten ein für Live daten sameln
     * @param measurement
     */
   /* private void trageDatenEin(Student.Measurement measurement) {
        for (Student s1 : Student.getAllStudents()){
            if(measurement.getOuterClass().getLabel().equals(s1.getLabel())) {
                for (PlotterPane p : s1.getDataDias()) {
                    String title = p.getTheTitle();
                    switch (title) {
                        case "GesamtEinnahmen":
                            System.out.println(s1.getLabel() + " GesamtEinnahmen");
                            p.addPoint(new CustomPoint( (int) (Simulation.getGlobalTime()-Student.getStartTime()),(int)measurement.getGuthaben()));
                            break;
                        case "GesamtWarteZeit":
                            System.out.println(s1.getLabel() + " Gesamtkosten");
                            p.addPoint(new CustomPoint(((int)(Simulation.getGlobalTime())),measurement.getGesWarteZeit()));
                            break;


                    }
                }
            }
        }
    }*/
    }

    static class MensaStationenBeobachter implements Observer{



        /**
         * update the statistic
         * @param o the measuremnet instance
         * @param arg the info
         */
        @Override
        public void update(Observable o, Object arg) {


            System.out.println("\n"+" MensaStation update");
            MensaStationen.Measurement measurement = (MensaStationen.Measurement)o;
            MensaStationen theStation = measurement.getOuterClass();
            //trageDatenEin(measurement);
            System.out.println("\n"+theStation.getLabel());


            }

       /* private void trageDatenEin(MensaStationen.Measurement measurement) {
            for (MensaStationen m : MensaStationen.getAllMensaStation()){
                if(measurement.getOuterClass().getLabel().equals(m.getLabel())) {
                    for (PlotterPane p : m.getDatenDias()) {
                        String title = p.getTheTitle();
                        switch (title) {
                            case "InUseTime":
                                System.out.println(m.getLabel() + " InUse");
                                System.out.println();
                                p.addPoint(new CustomPoint((int) (Simulation.getGlobalTime()-MensaStationen.getStartTime()),measurement.getInUseTime()));
                                break;
                            case "IdleTime":
                                System.out.println(m.getLabel() + " IdleTime");
                                p.addPoint(new CustomPoint((int) (Simulation.getGlobalTime()-MensaStationen.getStartTime()),measurement.getIdleTime()));
                                break;
                            case "numberOfVisitedObject":
                                System.out.println(m.getLabel() + " number");
                                p.addPoint(new CustomPoint( (int) (Simulation.getGlobalTime()-MensaStationen.getStartTime()),measurement.getNumbOfVisitedObjects()));

                        }
                    }
                }
            }
        }*/
    }


}
