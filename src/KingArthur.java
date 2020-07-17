/**
 * The implement of KingArthur simulator.
 *
 * It is responsible for:
 *  - king Arthur enters the great hall
 *  - king Arthur starts a meeting
 *  - king Arthur ends a meeting
 *  - king Arthur exits the great hall
 *
 * @author Jinxin Hu 963171
 *
 */

public class KingArthur extends Thread {
    private Hall greatHall;
	// create a new kingArthur
	KingArthur(Hall newGreatHall) {
        this.greatHall = newGreatHall;
    }

    /* king Arthur thread runs in the sequence:
    * enter -> start meeting -> end meeting -> exit -> enter.
    */
    public void run() {
        while(!isInterrupted()) {
            try {
                // set king waiting time, half the waiting time for his first enter
            	sleep(Params.getKingWaitingTime()/2);
                // king Arthur enters the great hall with "-1" flag 
                // to let the great hall know he is the king Arthur
                greatHall.enterHall(-1);
                // king Arthur starts a meeting, also with the "-1" flag
                greatHall.beginMeeting(-1);
                // king Arthur ends a meeting, also with the "-1" flag
                greatHall.endMeeting(-1);
                // king Arthur exits the great hall, also with the "-1" flag
                greatHall.exitHall(-1);
                // set the other half waiting time for king Arthur to enter
                sleep(Params.getKingWaitingTime()/2);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}