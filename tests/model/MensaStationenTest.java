package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MensaStationenTest {

    @BeforeEach
    void setUp() throws MensaStationen.CashRegisterLimitExceededException {
        for(int testCounter = 0; testCounter < 3; testCounter++){
            MensaStationen.create("Kasse",null,null,2.2,1,1,"Kasse.jpg",0,true,"Kasse");
        }
    }

    @Test
    void testExcpectedException() {
        // wirft Exception weil probiert wird noch eine Kasse zu erzeugen
        Assertions.assertThrows(MensaStationen.CashRegisterLimitExceededException.class, () -> {
            MensaStationen.create("Kasse",null,null,2.2,1,1,"Kasse.jpg",0,true,"Kasse");
        });

    }
}