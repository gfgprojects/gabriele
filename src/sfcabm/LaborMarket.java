package sfcabm;

import java.util.ArrayList;
import java.util.Collections;

import sfcabm.Firm;
import sfcabm.Consumer;
import sfcabm.Curriculum;


public class LaborMarket {
	int wageSum;
	int totalJobs;
	int applicSum;
	int vacancySum;
	double reservationWageSum;
	
	//IMPORTARE NUMERO FIRM E WORKER?!?!
	
	//creare liste per domande di lavoro
	ArrayList<Curriculum> laborDemandsList = new ArrayList<Curriculum>();

	public LaborMarket(){
	}
	public void receiveCurriculum(Curriculum aCV){
		laborDemandsList.add(aCV);
		if(Context.verbousFlag){
			System.out.println("  Labor agency received CV from consumer "+aCV.getSenderID()+" degree "+aCV.getSenderDegree());
		}
	}	


	

}

	
	//workerList.clear();
	//firmList.clear();


