package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import controller.Simulation;
import model.MensaEntrance;
import model.MensaStationen;
import model.Student;


/**
 * A simple JButton class for a start button
 * Modified by Team5:
 * method actionPerformed MensaStation.getStartStation().wakeUp() befor StartStation.getStartStation().wakeUp();
 *
 * @author Jaeger, Schmidt modified by Team5
 * @version 2016-07-07
 */
@SuppressWarnings("serial")
public class StartButton extends JButton implements ActionListener {

    public StartButton() {
        super("START");
        this.addActionListener(this);

    }


    @Override
    public void actionPerformed(ActionEvent event) {

        //set the simulation on
        Simulation.isRunning = true;

        MensaStationen.setStartTime(Simulation.getGlobalTime());
        Student.setStartTime(Simulation.getGlobalTime());
       // setzeVisible();

        //wake up the start station -> lets the simulation run
        MensaEntrance.getStartStation().wakeUp();

    }

    private void setzeVisible(){
       MensaStationen.setzeVisible();
       Student.setzeVisible();
    }


}
