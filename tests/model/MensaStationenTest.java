package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for MensaStaions.
 *
 * @author Team 5
 * @version 1
 */
class MensaStationenTest {

    /** a list of the mensa stations */
    private static ArrayList<MensaStationen> testMensaStationListe;

    /** Starts befor all tests. Sets up the test environment.
     *
     * @throws MensaStationen.CashRegisterLimitExceededException when more than 3 "MensaStation" with the label "Kasse" were created.
     */
    @BeforeAll
    static void beforeAll() throws MensaStationen.CashRegisterLimitExceededException {
        for(int testCounter = 0; testCounter < 3; testCounter++){
            MensaStationen.create("Kasse",null,null,2.2,1,1,"Kasse.jpg",0,true,"Kasse");
        }
        testMensaStationListe = MensaStationen.getAllMensaStation();
    }

    /**
     * Should throw the exception "CashRegisterLimitExceededException", because we want to create a fourth "MensaStation" with the label "Kasse".
     */
    @Test
    void testShouldThrowExcpectedCashRegisterLimitExceededException() {
        // wirft Exception weil probiert wird noch eine Kasse zu erzeugen
        assertThrows(MensaStationen.CashRegisterLimitExceededException.class, () -> {
            MensaStationen.create("Kasse",null,null,2.2,1,1,"Kasse.jpg",0,true,"Kasse");
        });
    }

    /**
     * Should be true, because we only have create 3 "MensaStation" with the label "Kasse".
     */
    @Test
    void testShouldShowThatThreeMensaStationsExist(){
        assertEquals(3, MensaStationen.getAllKassen().size());
    }

    /**
     * Should throw "CloneNotSupportedException", because we want to clone a "MensaStation".
     */
    @Test
    void testShouldThrowExcpectedCloneNotSupportedException() {
        assertThrows(CloneNotSupportedException.class, () -> testMensaStationListe.get(0).clone());
    }
}