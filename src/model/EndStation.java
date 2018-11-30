package model;

import java.util.ArrayList;
import java.util.Collection;

import io.OurStatistic;
import io.Statistics;

import javax.swing.*;

/**
 * Class for the end station. This is the last station where all objects are collected
 *
 * @author Jaeger, Schmidt
 * @version 2017-10-26
 */
public class EndStation extends SimpleStation {

	/** instance of the start station */
	@SuppressWarnings("unused")
	private static EndStation theEndStation;

	/** (private!) Constructor, creates a new end station
	 *
	 * @param label of the station
	 * @param inQueue the incoming queue
	 * @param outQueue the outgoing queue
	 * @param xPos x position of the station
	 * @param yPos y position of the station
	 * @param image image of the station
	 */
	protected EndStation(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image){
		super(label, inQueue, outQueue, xPos, yPos, image);


	}

	/** creates a new end station
	 *
	 * @param label of the station
	 * @param inQueue the incoming queue
	 * @param outQueue the outgoing queue
	 * @param xPos x position of the station
	 * @param yPos y position of the station
	 * @param image image of the station
	 */
	public static void create(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image){

		theEndStation = new EndStation(label, inQueue, outQueue, xPos, yPos, image);

	}

	@Override
	protected boolean work() {

		//let the thread wait only if there are no objects in the incoming queue
		if (numberOfInQueueObjects() == 0 ) return false;

		//If there is an inqueue object found, handle it
		if (numberOfInQueueObjects() > 0) this.handleObject(this.getNextInQueueObject());

		//maybe there is more work to do
		return true;

	}

	@Override
	protected void handleObject(TheObject theObject){


		// the object chooses the outgoing queue and enter it
		theObject.enterOutQueue(this);

		//  this is a just for fun action, the object gets a new location, but CAUTION !!!! magic numbers :-( !!!!!!
		theObject.theView.setLocation((this.getXPos() -100) + 18 * numberOfOutQueueObjects(), this.getYPos() + 120);

		//End the simulation if the condition is met
		endSimulation();


	}


	/** End the simulation if the condition is met
	 *
	 *
	 */
	protected void endSimulation(){

		// Are all objects in the stations outgoing queue, then we are finish
		if(TheObject.getAllObjects().size() == numberOfOutQueueObjects()){

			Statistics.show("\n--- Simulation beendet ----");

			//show some station statistics
			for (ProcessStation station : ProcessStation.getAllProcessStations()) {
				station.printStatistics();
			}

			//show some objects statistics
			for (Object object : this.outGoingQueue){
				((TheObject) object).printStatistics();
			}

			ArrayList<String> op= new ArrayList<String>();
			for (MensaStationen stationen : MensaStationen.getAllMensaStation()){
				op.add(stationen.label);
			}
			for (Student st : Student.getAllStudents()){
				op.add(st.label);
			}
			String[] optionen = op.toArray(new String[op.size()]);
			for (int i = 0; i <optionen.length ; i++) {
				System.out.println(optionen[i]);
			}
			String theOption = (String) JOptionPane.showInputDialog(null,"Wählen sie ein Objekt aus ","Statistik",JOptionPane.QUESTION_MESSAGE,null,optionen,optionen[0]);
			OurStatistic.printEndstatistic(theOption);
			//System.out.println(optionen);
			// end simulation
			// System.exit(0);
		}
	}

	@Override
	protected void handleObjects(Collection<TheObject> theObjects) {

	}

	@Override
	protected Collection<TheObject> getNextInQueueObjects() {
		return null;
	}

	@Override
	protected Collection<TheObject> getNextOutQueueObjects() {
		return null;
	}

	@Override
	protected void increaseIdleTime() {

	}


}
