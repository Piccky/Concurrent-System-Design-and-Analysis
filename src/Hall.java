/**
 * The implement of Hall simulator.
 *
 * It is responsible for:
 *  - Lock for knights and king Arthur to enter the great hall
 *  - Lock for king Arthur to start the meeting
 *  - Lock for king Arthur to end the meeting
 *  - Lock for knights and king Arthur to exit from the great hall
 *
 * @author Jinxin Hu 963171
 *
 */

import java.util.*;

public class Hall{
	private String hall;
	private Agenda agendaNew;
	private Agenda agendaComplete;
	// the map stores the <knight id, seated status>, 
	// if a knight is seated, his seated status is true
	public Map<Integer, Boolean> knights;
	// if king Arthur in the great hall, kingIn is true
	public boolean kingIn;
	// if the meeting is ongoing, the isMeeting is true
	public boolean isMeeting;
	// if all knights are seated, the allKnightsSeated is true
	public boolean allKnightsSeated;
	// if all knights stands, the allKnightsStand is true
	public boolean allKnightsStand;
	// if knights and king Arthur haven't exited from a meeting,
	// the isMeetingNotOver is true
	public boolean isMeetingNotOver;

	// create a new hall
	Hall(String newHall, Agenda newAgendaNew, Agenda newAgendaComplete){
		this.hall = newHall;
		this.agendaNew = newAgendaNew;
		this.agendaComplete = newAgendaComplete;
		this.knights = new HashMap<Integer, Boolean>();
		this.kingIn = false;
		this.isMeeting = false;
		this.allKnightsSeated = false;
		this.allKnightsStand = true;
		this.isMeetingNotOver = false;
	}

	/*
	enter great hall lock
	lock:	kingIn
			isMeetingNotOver
	 */
	public synchronized void enterHall(int id){
		// for king Arthur
		if(id == -1){
			// while there are knights in the hall with uncompleted quests, wait
			while(isMeetingNotOver){
				try{
					wait();
				}catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			// otherwise, king enters the great hall
			kingIn = true;
			System.out.println("King Arthur enters the Great Hall.");
			notifyAll();
		}
		// for knights
		else{
			// while there are knights in the hall with uncompleted quests 
			// or king is in the hall, wait
			while(kingIn || isMeetingNotOver){
				try{
					wait();
				}catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			// otherwise, knight enter the hall with seated status "false"
			knights.put(id, false);
			System.out.println("Knight " + id + " enters Great Hall.");
			notifyAll();
		}
	}

	/*
	begin meeting lock
	lock: 	allKnightsSeated
	 		knights
	 		kingIn
	*/
	public synchronized void beginMeeting(int id){
		// reset the allKnightsSeated value for checking
		allKnightsSeated = false;
		// while not all knights are seated and there is at least one knight in the hall
		while(!allKnightsSeated && (!knights.isEmpty())){
			allKnightsSeated = true;
			for(Integer i : knights.keySet()){
				// if there is one knight stand, the allKnightsSeated will be false
				allKnightsSeated = allKnightsSeated && knights.get(i);
			}
			// if king Arthur not in the hall or some knights not seated, wait
			if(!(kingIn && allKnightsSeated)){
				try{
					// reset the allKnightsSeated value for checking
					allKnightsSeated = false;
					wait();
				}catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		// if not in the meeting
		if(!isMeeting){
			// start meeting
			isMeeting = true;
			isMeetingNotOver = true;
			System.out.println("Meeting begin!");
		}
		notifyAll();
	}

	/*
	end meeting lock
	lock:	knights
			allKnightsStand
	*/
	public synchronized void endMeeting(int id){
		// reset allKnightsStand value for checking
		allKnightsStand = false;
		// while not all knights stands and there is at least one knight in the hall
		while(!allKnightsStand && !knights.isEmpty()){
			allKnightsStand = true;
			for(Integer i : knights.keySet()){
				// if there is one knight seated, the allKnightsStand will be false
				allKnightsStand = allKnightsStand && (!knights.get(i));
			}
			// if not all knights stand, wait
			if(!allKnightsStand){
				try{
					// reset allKnightsStand for checking
					allKnightsStand = false;
					wait();
				}catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}else{
				break;
			}
		}
		// if in the meeting
		if(isMeeting){
			// end meeting
			isMeeting = false;
			System.out.println("Meeting end!");
		}
		notifyAll();
	}

	/*
	exithall lock
	lock :	kingIn
	*/
	public synchronized void exitHall(int id){
		// for knights
		if(id != -1){
			// while king Arthur in the hall, wait
			while(kingIn){
				try{
					wait();
				}catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			// otherwise, knight exits from great hall
			knights.remove(id);
			System.out.println("Knight " + id + " exits from Great Hall.");
		}
		// for king Arthur
		else {
			// king Arthur exits the great hall
			kingIn = false;
			System.out.println("King Arthur exits the Great Hall.");

		}
		// if there is no knight in the great hall
		if(knights.isEmpty()){
			// set meeting over
			isMeetingNotOver = false;
		}
		notifyAll();
	}
}