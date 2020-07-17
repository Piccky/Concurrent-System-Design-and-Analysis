/**
 * Produces new quests for the knights to complete.
 *
 * @author ngeard@unimelb.edu.au
 * @Jinxin Hu 963171
 *
 */

public class Producer extends Thread {

	private Agenda agenda;
	
    // create a new producer
    Producer(Agenda newAgenda) {
        this.agenda = newAgenda;
    }

    // quests 
    public void run() {
        while(!isInterrupted()) {
            // System.out.println("Producer Ready");
            try {
                // create a new quest and send it to the agenda.
                Quest quest = Quest.getNewQuest();
                agenda.updateAgenda(quest, true);
                // print the message that new quest has been added to the agenda
                System.out.println(quest.toString() + " added to " + agenda.toString() + ".");
                // let some time pass before the next quest arrives
                sleep(Params.QUEST_ADDITION_TIME);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}