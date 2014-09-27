package sfcabm;

import java.util.ArrayList;

public class Firm {

	int FirmID;
	double reservationWageFirmSum;
	int vacancySum;
	int totalJobs;
	double firmWageSum;
	int numVacancy;
	int numJobs;
	
	ArrayList applicationList = new ArrayList();
	ArrayList jobList = new ArrayList();
	
	
	public Firm(int FirmID, double reservationWageFirmSum, int vacancySum, int totalJobs){
		super();
		//identity = FirmID
	}
	
	public void reset(){
		numJobs=0;
		firmWageSum=0;
		applicationList.clear();
		jobList.clear();
	}

}
