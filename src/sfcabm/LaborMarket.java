package sfcabm;

import java.util.ArrayList;
import java.util.Collections;

import sfcabm.Firm;
import sfcabm.Consumer;


public class LaborMarket {
	int wageSum;
	int totalJobs;
	int applicSum;
	int vacancySum;
	double reservationWageSum;
	
	//IMPORTARE NUMERO FIRM E WORKER?!?!
	
	//creare liste firms e Consumer
	ArrayList<Firm> firmList = new ArrayList<Firm>();
	ArrayList<Consumer> workerList = new ArrayList<Consumer>();

}

	
	//workerList.clear();
	//firmList.clear();


//job application
public void application(){
	for (int i=0; i< workerList.size(); i++){
		Consumer aConsumer = workerList.get(i);
		//cosi le application avvengono random. le imprese dovrebbero scegliere in base alle produttivita
		Collections.shuffle(firmList);
		for (int g = 0; g < aConsumer.numApplic; g++){
			for(int f = 0; f < firmList.size(); f++){
				Firm aFirm = firmList.get(f);
				if (!aFirm.applicationList.contains(aConsumer) & aFirm.numVacancy > 0){
					aFirm.applicationList.add(aConsumer);
				}
			}
		}
	}
	
	public void hire(){
		int counter = 0;
		for (int i = 0; i < firmList.size(); i++){
			Firm aFirm = firmList.get(i);
			counter = counter + aFirm.numVacancy;
		}
		
		vacancySum = counter;
		
		for (int i = 0; i < workerList.size(); i++){
			Consumer aConsumer = workerList.get(i);
			applicSum = applicSum + aConsumer.numApplic;
		}
	}
	




