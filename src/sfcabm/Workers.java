package sfcabm;

//import sfcabm.Bank;
//import sfcabm.LaborMkt; 
//import sfcabm.Firm;

import java.util.ArrayList;

import repast.simphony.random.RandomHelper;
//import repast.simphony.engine.schedule.ScheduledMethod;


public class Workers {
	int identity;
	//generation age distribution per inizializzare i workers: diverse eta, diverse condizioni lavorative e di studio: HETEROG
	//double initialProbabilityToBeEmployed=0.7;
	double reservationWageWorker;
	double workerWage;
	int numApplic;
	int status;
	//DISPOSABLE INCOME yd = yp - iL - T, diverso dal wage!!
	 double disposableIncome;
	 int consumption;
	 //propensioni rivedere calibration
	 double alpha = 0.4;
	 double beta = 0.4;
	 //taxes on consumption, tax rate evolves??...T = \phi c
	 double taxRate = 0.1;
	 double taxConsumTot=taxRate*consumption;
	 //savings = deposits
	 int saving;
	 double wealth=RandomHelper.nextDoubleFromTo(-500.0, 1000.09);
	 double workerLoan;
	 double studentLoan;
	
	//ABILITY
	 double abilityStudent = RandomHelper.nextDoubleFromTo(0,0.5);
	 //double abilityStudThres = 0.25;
	 boolean InvestEducation;
	 int studentSpending;
	 //settare totYearsEdu come somma dei time step. come si fa?!
	 int totYearsEdu=10;
	 int costEdu=300;
	 boolean successEdu;
	 double lambda=0.3;
	 
	 //spostare iL in banca dopo!
	 double iL=0.03;
	double sumAbilityWorker; 
	 //forse e il caso di creare due classi, 
	 //cosi si possono creare array con le identities di workers e students separatamente? 
	 //ArrayList<Students> studentsList = new ArrayList<Students>();
	 ArrayList<Workers> workersList = new ArrayList<Workers>();
	 ArrayList<Double> abilityStudentList = new ArrayList<Double>();

	public Workers(int workerID){
		super();
		identity = workerID;
		//livello iniziale di ricchezza
		wealth=RandomHelper.nextDoubleFromTo(-500.0, 1000.09);
		abilityStudent = RandomHelper.nextDoubleFromTo(0,0.5);
		
		//workersList.clear();
		abilityStudentList.clear();
		
		//check schedule
		 //@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=2.0)
	}	
	public void chooseEdu(){
		if(RandomHelper.nextDouble()>abilityStudent & wealth > 100){
			InvestEducation=true;
			if(wealth >= 300){
				  studentSpending=costEdu;
			 }
		  else{
			  if(wealth < 300)
			  studentLoan=costEdu*iL;
			  }
	  }
  else{
	  if(RandomHelper.nextDouble()<abilityStudent & wealth < 100){
		  InvestEducation=false;
		  for(int i=0; i<abilityStudentList.size(); i++){
			  sumAbilityWorker=abilityStudentList.get(i);
			  //+lambda*totYearsEdu;
		  }
	  }
	  
	  System.out.println("ID " +identity+ " investe in Edu " +InvestEducation+ " ability " +abilityStudent+ " wealth " +wealth);
	
		//next time step?? schedule?!
	if(InvestEducation=true){
		if(RandomHelper.nextDouble()*0.5<abilityStudent){
			successEdu=true;
			abilityStudentList.add(new Double(1.0));
		}
		else{
			successEdu=false;
		}
		
		//System.out.println("successo "+successEdu);
	}

  }
}

	
	public double getStudentSpending(){
		return studentSpending;
	}
	
	public double getWealth(){
		return wealth;
	}
	
	public double getAbilityStudent(){
		return abilityStudent;
	}
	
	public double getStudentLoan(){
		return studentLoan;
	}

	
	
}
