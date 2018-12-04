package model;

/**
 * Beschreibung der Klasse MensaEntrance.
 * Weiter Beschreibung
 * <p>
 * Die Klasse wurde am 28.November.2018 um 11:34 Uhr erstellt.
 *
 * @author Team5
 * @version 1.0
 */

public class MensaEntrance extends StartStation {
    /**
     * instance of MensaEntrance
     */
    private static MensaEntrance starStation;
    /**
     * (private!) Constructor, creates a new start station
     *
     * @param label    of the station
     * @param inQueue  the incoming queue
     * @param outQueue the outgoing queue
     * @param xPos     x position of the station
     * @param yPos     y position of the station
     * @param image    image of the station
     */

    private MensaEntrance(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image) {
        super(label, inQueue, outQueue, xPos, yPos, image);
    }

    /**
     *
     * @param label of the station
     * @param inQueue the incoming queue
     * @param outQueue the outgoing queue
     * @param xPos x position of the station
     * @param yPos y position of the station
     * @param image image of the station
     */
    public static void create(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image) {
        starStation = new MensaEntrance(label, inQueue, outQueue, xPos, yPos, image);
    }

    /**
     * getter for MensaEntrance instance
     * @return MensaEntrance
     */
    public static MensaEntrance getStartStation() {
        return starStation;
    }


}



