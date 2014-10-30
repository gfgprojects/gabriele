package sfcabm;

import repast.simphony.random.RandomHelper;
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
	ArrayList<LaborOffer> laborOffersList = new ArrayList<LaborOffer>();
	LaborOffer aLaborOffer;
	Curriculum aLaborDemand;
	Consumer aConsumer;
	Firm aFirm;

	public LaborMarket(){
	}
	
	public void receiveCurriculum(Curriculum aCV){
		laborDemandsList.add(aCV);
		if(Context.verbousFlag){
			System.out.println("  Labor agency received CV from consumer "+aCV.getSenderID()+" degree "+aCV.getSenderDegree());
		}
	}	

	public void receiveVacancies(LaborOffer aOffer){
		laborOffersList.add(aOffer);
		if(Context.verbousFlag){
			System.out.println("  Labor agency received offer from firm "+aOffer.getSenderID()+" wageOffer "+aOffer.getSenderFirmReservationWage());
		}
	}
	public void match(){

//print situation on the screen

		if(Context.verbousFlag){
			System.out.println("Trying to match");
			if(laborOffersList.size()>0){
				System.out.println("   offer:");
				for(int i=0;i<laborOffersList.size();i++){
					aLaborOffer=laborOffersList.get(i);
					System.out.println("      firm "+aLaborOffer.getSenderID()+" needed "+aLaborOffer.getNeededProductionCapacity());

				}
			}
			else{
				System.out.println("   no offers from firms");
			}
			if(laborDemandsList.size()>0){
				System.out.println("   demand:");
				for(int i=0;i<laborDemandsList.size();i++){
					aLaborDemand=laborDemandsList.get(i);
					System.out.println("      unemplyed "+aLaborDemand.getSenderID()+" degree "+aLaborDemand.getSenderDegree()+" production "+aLaborDemand.getSenderProduction());

				}
			}
			else{
				System.out.println("   no unemployed");
			}
		}
//matching
		while(laborOffersList.size()>0 && laborDemandsList.size()>0){
			aLaborOffer=laborOffersList.get(RandomHelper.nextIntFromTo(0,laborOffersList.size()-1));
			aFirm=aLaborOffer.getSender();
			aLaborDemand=laborDemandsList.get(RandomHelper.nextIntFromTo(0,laborDemandsList.size()-1));
			double aLaborDemandProduction=aLaborDemand.getSenderProduction();
			aConsumer=aLaborDemand.getSender();
			aFirm.hire(aConsumer);
			aLaborOffer.decreaseNeededProductionCapacityAfterHiring(aLaborDemandProduction);
			if(aLaborOffer.getNeededProductionCapacity()<0){
				laborOffersList.remove(aLaborOffer);
			}
			laborDemandsList.remove(aLaborDemand);
		}

//print situation on the screen
		if(Context.verbousFlag){
			if(laborOffersList.size()>0){
				System.out.println("Unsatisfied firms");
				for(int i=0;i<laborOffersList.size();i++){
					aLaborOffer=laborOffersList.get(i);
					System.out.println("      firm "+aLaborOffer.getSenderID()+" needed "+aLaborOffer.getNeededProductionCapacity());
				}
			}
			if(laborDemandsList.size()>0){
				System.out.println("   Unsatified workers:");
				for(int i=0;i<laborDemandsList.size();i++){
					aLaborDemand=laborDemandsList.get(i);
					System.out.println("      unemplyed "+aLaborDemand.getSenderID()+" degree "+aLaborDemand.getSenderDegree()+" production "+aLaborDemand.getSenderProduction());

				}
			}

		}


	}

	public void jettisoningCurricula(){
		laborDemandsList = new ArrayList<Curriculum>();
		if(Context.verbousFlag){
			System.out.println("  Labor agency jettisoning curricula ");
		}
	}
}


	
	//workerList.clear();
	//firmList.clear();


