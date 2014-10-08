package sfcabm;

//import java.util.ArrayList;
//import sfcabm.LaborMkt;

public class Firm {

	int FirmID;
	int identity;
	double reservationWageFirmSum;
	int vacancySum;
	int totalJobs;
	double firmWageSum;
	int numVacancy;
	int numJobs;
	
	//ArrayList applicationList = new ArrayList();
	//ArrayList jobList = new ArrayList();
	
	
	
	public Firm(int FirmID) {
		super();
		identity = FirmID;
	}
	

	
	


	//reset the firm each time step 
	/*public void reset(){
		numJobs=0;
		firmWageSum=0;
		applicationList.clear();
		jobList.clear();
	}
*/
}
