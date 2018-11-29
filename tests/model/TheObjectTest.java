package model;

import controller.Simulation;
import io.Factory;
import io.FactoryXML;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TheObjectTest {

    ArrayList<String> stationsToGo;
    Student s1;


    TheObjectTest() {


//        stationsToGo = new ArrayList<String>();
//        stationsToGo.add("Station_1");
//        stationsToGo.add("Station_2");
//
//        Student.create("Pascal",stationsToGo,4,6,1,1,"burgerino.png");

        Simulation s1 = new Simulation();

        Factory.setTheObjectDataFile("xml/test/object.xml");
        Factory.setTheStartStationDataFile("xml/test//startstation.xml");
        Factory.setTheStationDataFile("xml/test/station.xml");
        Factory.setTheEndStationDataFile("xml/test/endstation.xml");

        FactoryXML.createStartScenario();

    }

    @Test
    public void wurdenAlleStudentenErzeugt() {

        assertEquals(4, Student.getAllStudents().size());
    }

    @Test
    public void nextStationTestPascal_0(){

        for(Student s: Student.getAllStudents()){
           if(s.getLabel().equals("Pascal_0")){
                assertEquals("Salate_2", s.getNextStation().getLabel());
           }
        }
    }
}