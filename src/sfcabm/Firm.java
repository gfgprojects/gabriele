package sfcabm;

import java.util.ArrayList;

//import java.util.ArrayList;
//import sfcabm.LaborMkt;
import sfcabm.Curriculum;

public class Firm {

	int FirmID;
	int identity;
	double reservationWageFirmSum;
	int vacancySum;
	int totalJobs;
	double firmWageSum;
	int numVacancy;
	int numJobs;


	ArrayList<Curriculum> applicationList = new ArrayList<Curriculum>();
	ArrayList<Object> jobList = new ArrayList<Object>();



	public Firm(int FirmID) {
		super();
		identity = FirmID;
	}


	public int getID(){
		return identity;
	}
	public void printID(){
		System.out.println("I am firm "+identity);
	}

	public void receiveCurriculum(Curriculum aCV){
		applicationList.add(aCV);
		if(Context.verbousFlag){
			System.out.println("Firm "+identity+" received CV from consumer "+aCV.getSenderID()+" degree "+aCV.getSenderDegree());
		}
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
