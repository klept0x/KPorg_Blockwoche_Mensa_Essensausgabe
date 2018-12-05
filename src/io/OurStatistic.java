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

/*for(String key : st.getDataDias().keySet()){
        st.getDataDias().get(key).setVisible(true);
    }*/
/*for(String key : ms.getDatenDias().keySet()){
        ms.getDatenDias().get(key).setVisible(true);
    }*/

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
            String info= (String) arg;
            Student.Measurement s = (Student.Measurement)o;
            Student theStudent = s.getOuterClass();
            //trageDatenEin(s,info);


        }

        /**
         * trage daten ein für Live daten sameln
         * @param measurement
         */
        private void trageDatenEin(Student.Measurement measurement,String info) {
            for (Student s1 : Student.getAllStudents()){
                if(measurement.getOuterClass().getLabel().equals(s1.getLabel())) {

                    for(String key : s1.getDataDias().keySet()){
                        if(key.equals(info)){
                            switch (info){
                                case "gesWarteZeit":s1.getDataDias().get(key).addPoint(measurement.getGesWarteP().get(measurement.getGesWarteP().size()-1));
                                    break;
                                case"guthaben":s1.getDataDias().get(key).addPoint(measurement.getGuthabenP().get(measurement.getGuthabenP().size()-1));
                            }
                        }
                    }
                }
            }
        }
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
            String info = (String) arg;
            //trageDatenEin(measurement,info);
            System.out.println("\n"+theStation.getLabel());


        }

        private void trageDatenEin(MensaStationen.Measurement measurement,String info) {
            for (MensaStationen ms : MensaStationen.getAllMensaStation()){
                if(measurement.getOuterClass().getLabel().equals(ms.getLabel())) {

                    for(String key : ms.getDatenDias().keySet()){
                        if(key.equals(info)){
                            switch (info){
                                case "idleTime":ms.getDatenDias().get(key).addPoint(measurement.getIdleTimeP().get(measurement.getIdleTimeP().size()-1));
                                    break;
                                case"numberOVO":ms.getDatenDias().get(key).addPoint(measurement.getNumberOVP().get(measurement.getNumberOVP().size()-1));
                                    break;
                                case"inUseTime":ms.getDatenDias().get(key).addPoint(measurement.getInUseTimeP().get(measurement.getInUseTimeP().size()-1));

                            }
                        }
                    }
                }
            }
        }
    }
}
