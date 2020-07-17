/**
 * The implement of knights simulator.
 *
 * It is responsible for:
 *  - knights enter the great hall
 *  - knights sit down around the round table
 *  - knights release a quest
 *  - knights acquire a quest
 *  - knights stand from the round table
 *  - knights exit the great hall
 *  - knights set off a quest
 *  - knights complete a quest
 *
 * @author Jinxin Hu 963171
 *
 */

public class Knight extends Thread {
    // numKnight stores the knight id
	public int numKnight;
	private Agenda agendaNew;
	private Agenda agendaComplete;
	protected Hall greatHall;
    // quest stores the quest that a knight acquires
	public Quest quest;

	// create a new knight
	Knight(int newNumKnight, Agenda newAgendaNew,
		Agenda newAgendaComplete, Hall newGreatHall) {
		this.numKnight = newNumKnight;
		this.agendaNew = newAgendaNew;
		this.agendaComplete = newAgendaComplete;
        this.greatHall = newGreatHall;
    }

    // knights sit down
    public void sitDown(){
        // update the seated status to true
        greatHall.knights.replace(numKnight, true);
        System.out.println("Knight " + numKnight + " sits at the Round Table.");
    }

    // knights release a quest
    public void releaseQuest(){
		if(quest!=null) {
            System.out.println("Knight " + numKnight 
                + " releases " + quest.toString() + ".");
            // remove the released quest from the complete agenda
			agendaComplete.updateAgenda(quest, true);
			quest = null;
		}
	}

    // knights acquire a quest
    public void acquireQuest(){
        // get the aquired quest from the new agenda
		quest = agendaNew.removeQuestFromAgenda();
		System.out.println("Knight " + numKnight 
            + " acquires " + quest.toString() + ".");
    }

    // knights stand up
    public void standUp(){
        // update the seated status to true
        greatHall.knights.replace(numKnight, false);
        System.out.println("Knight " + numKnight + " stands from the Round Table.");
    }


    // knights set off to complete a quest
    public void setoffQuest() throws InterruptedException{
		if(quest!=null) {
			System.out.println("Knight " + numKnight 
                + " sets off to complete " + quest.toString() + ".");
            // knight sleep for the questing time to complete the quest
            sleep(Params.getQuestingTime());
		}
    }

    // knights complete a quest
    public void completeQuest(){
    	if(quest!=null){
			System.out.println("Knight " + numKnight 
                + " completes " + quest.toString() + ".");
            // add the completed quest to the complete agenda with the flag "false"
            // , which means the quest cannot be updated at the moment
            agendaComplete.updateAgenda(quest, false);
    	}
    }


    /* knight thread runs in the sequence:
    * enter -> sit -> release -> acquire -> stand -> exit 
    * -> set off -> complete quest -> enter.
    */
    public void run() {
        while(!isInterrupted()) {
            try {
                // knight enters the great hall
                greatHall.enterHall(numKnight);
                // knight minglings before sit down
                sleep(Params.getMinglingTime());
                // knight sits down around the round table
                sitDown();
                // knight starts the meeting when all knights seated
                greatHall.beginMeeting(numKnight);
                // knight releases a quest
            	releaseQuest();
                // knight acquires a quest
            	acquireQuest();
                // knight stands up from the round table
                standUp();
                // knight ends the meeting when all knights standing
                greatHall.endMeeting(numKnight);
                // knight minglings after standing up
                sleep(Params.getMinglingTime());
                // knight exits the great hall
                greatHall.exitHall(numKnight);
                // knight sets off a quest
           	    setoffQuest();
                // knight completes a quest
            	completeQuest();
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}