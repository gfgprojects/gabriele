package sfcabm;

import sfcabm.Firm;
import sfcabm.Workers;

import java.util.ArrayList;
import java.util.Collections;



public class LaborMkt {

	int numWorkers;
	int numFirms;
	int wageSum;
	int totalJobs;
	int applicSum;
	int vacancySum;
	double reservationWageWorkerSum;
	double reservationWageFirmSum;
	
	ArrayList firmList = new ArrayList();
	ArrayList workerList = new ArrayList();
	
public void buildLaborMkt() {
	//super ();
	//name = "Labor Market";
	 //numWorkers = 20;
	 //numFirms = 20;
	
	workerList.clear();
	firmList.clear();
	
	totalJobs = 0;
	wageSum = 0;
	applicSum = 0;
	vacancySum = 0;
	reservationWageWorkerSum = 0;
	reservationWageFirmSum = 0;
	
	
for (int i=0; i < numFirms; i++){
	Firm aFirm = new Firm (i, reservationWageFirmSum, vacancySum, totalJobs);
	firmList.add(aFirm);
}

//CONTROLLARE CHE IN QUESTO MODO SI CREANO DOMANDE INDIVIDUALI PER CIASCUNA IMPRESA: aFirm
	//creare le imprese che postano offerte di lavoro
//num jobs sono le realizzazione di domande - offerte di lavoro andate a buon fine
	System.out.print("firm ID " +firmID+ "reserv wage firm" +reservationWageFirmSum+ "num job demand" +vacancySum+ "num jobs "+totalJobs);


for (int i=0; i < numWorkers; i++){
	Worker aWorker = new Worker (i, reservationWageWorkerSum, applicSum);
	workerList.add(aWorker);
}
System.out.print("worker ID " +workerID+ "reserv wage worker" +reservationWageWorkerSum+ "num applications" +applicSum);

}

//job application
public void application(){
	for (int i=0; i< workerList.size(); i++){
		Workers aWorker = (Workers) workerList.get(i);
		Collections.shuffle(firmList);
		for (int g = 0; g < aWorker.numApplic; g++){
			for(int f = 0; f < firmList.size(); f++){
				Firm aFirm = (Firm) firmList.get(f);
				if (!aFirm.applicationList.contains(aWorker) & aFirm.numVacancy > 0){
					aFirm.applicationList.add(aWorker);
				}
			}
		}
	}
	
	
	
}
}