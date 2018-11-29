package io;


import model.MensaStationen;
import model.Student;
import plotter.src.main.java.model.CustomPoint;
import plotter.src.main.java.view.PlotterPane;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class OurStatistic extends Statistics {

    private static OurStatistic statistic;
    static Objectbeobachter obectb= new Objectbeobachter();
    static MensaStationenBeobachter mensaBeo= new MensaStationenBeobachter();
    PlotterPane p;
    private OurStatistic(){
        p= new PlotterPane(new ArrayList<CustomPoint>(),800,600,true,"Kosten in €","Zeiteinheit","Kosten pro Station");
    }
    public static OurStatistic createStatistic(){
        statistic= new OurStatistic();
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
        }

    }

    static class MensaStationenBeobachter implements Observer{

        @Override
        public void update(Observable o, Object arg) {
            System.out.println("\n"+" MensaStation update");
            MensaStationen.Measurement measurement = (MensaStationen.Measurement)o;
            MensaStationen theStation = measurement.getOuterClass();
            System.out.println("\n"+theStation.getLabel());

        }
    }


}
