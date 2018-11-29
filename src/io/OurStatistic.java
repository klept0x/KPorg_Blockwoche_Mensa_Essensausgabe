package io;


import controller.Simulation;
import model.MensaStationen;
import model.Student;
import plotter.src.main.java.model.CustomPoint;
import plotter.src.main.java.view.PlotterPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class OurStatistic extends Statistics {

    private static OurStatistic statistic;
    static Objectbeobachter obectb= new Objectbeobachter();
    static MensaStationenBeobachter mensaBeo= new MensaStationenBeobachter();
    PlotterPane p;


    private OurStatistic(){
        p = new PlotterPane(new ArrayList<CustomPoint>(),800,600,true,"Kosten in €","Zeiteinheit","Kosten pro Station");
    }

    public static OurStatistic createStatistic(){
        statistic = new OurStatistic();
        return  statistic;
    }

    public static Objectbeobachter getObjectBeobachter() {
        return obectb;
    }

    public static MensaStationenBeobachter getMensaBeo() {
        return mensaBeo;
    }



/**----------------------------------------------------------------------INNER CLASSES-------------------------------------------------------------------------------------*/



        static class Objectbeobachter implements Observer {


    @Override
        public void update(Observable o, Object arg) {
        System.out.println("\n"+" Student update");
        Student.Measurement s = (Student.Measurement)o;
        Student theStudent = s.getOuterClass();
        System.out.println(theStudent.getLabel());
        Statistics.show(String.valueOf(arg.getClass()));
        if(arg instanceof  Integer){
                int daten = (int) arg;
            }
        }
    }

    static class MensaStationenBeobachter implements Observer{



        public MensaStationenBeobachter() {

        }

        @Override
        public void update(Observable o, Object arg) {


            System.out.println("\n"+" MensaStation update");
            MensaStationen.Measurement measurement = (MensaStationen.Measurement)o;
            MensaStationen theStation = measurement.getOuterClass();
            trageDatenEin(measurement);
            System.out.println("\n"+theStation.getLabel());


            }

        private void trageDatenEin(MensaStationen.Measurement measurement) {
            for (MensaStationen m : MensaStationen.getAllMensaStation()){
                for(PlotterPane p : m.getDatenDias()){
                    String title= p.getTheTitle();
                    switch(title){
                        case "InUseTime":
                            System.out.println(m.getLabel()+" InUse");
                           break;
                        case "IdleTime":
                            System.out.println(m.getLabel()+" IdleTime");
                            break;
                        case "numberOfVisitedObject":
                            System.out.println(m.getLabel()+" number");

                    }
                }
            }
        }
    }


}
