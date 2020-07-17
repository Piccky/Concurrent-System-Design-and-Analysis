/**
 * The implement of Agenda simulator.
 *
 * It is responsible for:
 *  - add new quests to new agenda 
 *  - assign quests to knights from the new agenda
 *  - add completed quests to complete agenda
 *  - remove released quests from the complete agenda
 *
 * @author Jinxin Hu 963171
 *
 */

import java.util.*;

public class Agenda{
	private String agenda;
	// a map stores <quest, status>, if the status is true,
	// the agenda could operate the quest like assign or remove
	public Map<Quest, Boolean> quests;
	// quest object to store the operated quest
	private Quest quest;

	// create a new agenda
	Agenda(String newAgenda) {
        this.agenda = newAgenda;
        this.quests = new HashMap<Quest, Boolean>();
    }

    // add / update a quest to an agenda
    public synchronized void updateAgenda(Quest newQuest, Boolean isReady){
    	// if the quest is already in the agenda
    	if(quests.get(newQuest)!=null){
    		// update the status of the quest
    		quests.replace(newQuest, isReady);
    	}
    	else{
    		// otherwise, add the new pair to the quests map
    		quests.put(newQuest, isReady);
    	}
    	notifyAll();
    }

    // remove a quest from an agenda
    public synchronized Quest removeQuestFromAgenda(){
    	// while the quests map is empty, wait
		while(quests.isEmpty()){
			try{
				wait();
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		// otherwise, do the while cycle to iterate the map
		while(true){
			for(Quest i : quests.keySet()){
				// if find a record with status true
				if(quests.get(i)==true)
				{
					// remove the record from the quests map
					quest = i;
					quests.remove(i);
					notifyAll();
					// return the quest to knight
					return quest;
				}
			}
			// if there is not any record can be operated, wait
			try{
				wait();
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	// return the name of an agenda
    public String toString(){
    	return agenda;
    }
}