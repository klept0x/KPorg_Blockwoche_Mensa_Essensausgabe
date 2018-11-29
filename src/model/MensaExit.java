package model;

public class MensaExit extends EndStation {


    /**
     * (private!) Constructor, creates a new end station
     *
     * @param label    of the station
     * @param inQueue  the incoming queue
     * @param outQueue the outgoing queue
     * @param xPos     x position of the station
     * @param yPos     y position of the station
     * @param image    image of the station
     */
    private MensaExit(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image) {
        super(label, inQueue, outQueue, xPos, yPos, image);
    }

    public static void create(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image){
        new MensaExit(label,inQueue,outQueue,xPos,yPos,image);
    }
}
