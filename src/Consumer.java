/**
 * Consumes completed quests from an agenda.
 *
 * @author ngeard@unimelb.edu.au
 * @Jinxin Hu 963171
 *
 */

public class Consumer extends Thread {

    // the agenda from which completd quests are removed
    private Agenda agenda;

    // creates a new consumer for the given agenda
    Consumer(Agenda agendaComplete) {
        this.agenda = agendaComplete;
    }

    // repeatedly collect completed quests from the agenda
    public void run() {
        while (!isInterrupted()) {
            try {
                // remove a quest that is complete
                Quest temp = agenda.removeQuestFromAgenda();
                // let some time pass before the next quest is removed
                System.out.println(temp.toString() + " removed from Complete Agenda.");
                sleep(Params.QUEST_REMOVAL_TIME);
            }
            catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
