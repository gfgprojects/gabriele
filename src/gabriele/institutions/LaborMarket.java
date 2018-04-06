/*
GABRIELE: the General Agent Based Repast Implemented Extensible Laboratory for Economics
Copyright (C) 2018  Gianfranco Giulioni
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package gabriele.institutions;

import repast.simphony.random.RandomHelper;
import java.util.ArrayList;
import java.util.Collections;

import gabriele.agents.Firm;
import gabriele.agents.Consumer;
import gabriele.bargaining.Curriculum;
import gabriele.Context;
import gabriele.bargaining.LaborOffer;


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
		if(Context.verboseFlag){
			System.out.println("  Labor agency received CV from consumer "+aCV.getSenderID()+" degree "+aCV.getSenderDegree());
		}
	}	

	public void receiveVacancies(LaborOffer aOffer){
		laborOffersList.add(aOffer);
		if(Context.verboseFlag){
			System.out.println("  Labor agency received offer from firm "+aOffer.getSenderID()+" wageOffer "+aOffer.getSenderFirmReservationWage());
		}
	}
	public void match(){

//print situation on the screen

		if(Context.verboseFlag){
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
		if(Context.verboseFlag){
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
		laborOffersList = new ArrayList<LaborOffer>();
		if(Context.verboseFlag){
			System.out.println("     Labor agency jettisoning curricula ");
		}
	}
}


	
	//workerList.clear();
	//firmList.clear();


