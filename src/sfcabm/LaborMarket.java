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
	ArrayList<LaborOffer> laborOfferList = new ArrayList<LaborOffer>();

	public LaborMarket(){
	}
	
	public void receiveCurriculum(Curriculum aCV){
		laborDemandsList.add(aCV);
		if(Context.verbousFlag){
			System.out.println("  Labor agency received CV from consumer "+aCV.getSenderID()+" degree "+aCV.getSenderDegree());
		}
	}	

	public void receiveLaborDemand(LaborOffer aOffer){
		laborOfferList.add(aOffer);
		if(Context.verbousFlag){
			System.out.println("  Labor agency received offer from firm "+aOffer.getSenderID()+" wageOffer "+aOffer.senderFirmReservationWage());
		}
	}
}


	
	//workerList.clear();
	//firmList.clear();


