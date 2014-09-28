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
	 int saving;
	 double wealth=RandomHelper.nextDoubleFromTo(100.07, 10000.09);
	 double deposits, workerLoan;
	//ABILITY
	 double abilityStudent = RandomHelper.nextDoubleFromTo(0,0.5);
	 double abilityStudThres = 0.25;
	 boolean InvestEducation;
	 double invest;
	 //double probabilityInvestEdu;
	 //condizionare a ability e wealth
	 int yearEdu=1;
	 int totYearsEdu;
	 int costEdu=300;
	 double lambda;
	 //spostare iL in banca dopo!
	 double iL=0.03;
	 double abilityWorker=abilityStudent+lambda*totYearsEdu;
	 
	 //forse e il caso di creare due classi, 
	 //cosi si possono creare array con le identities di workers e students separatamente? 
	 //ArrayList<Students> studentsList = new ArrayList<Students>();
	 ArrayList<Workers> workersList = new ArrayList<Workers>();
	

	public Workers(int workerID){
		super();
		identity = workerID;
		//livello iniziale di ricchezza
		//wealth=0;
		
		
		//********define Bank.il in bank!
		//disposableIncome=workerWage+deposits-Bank.iL*workerLoan-taxConsumTot;
		

		//check schedule
		 //@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=2.0)
		
		//rivedere threshold
			if(RandomHelper.nextDouble()>abilityStudThres && wealth > 100){
				InvestEducation=true;
				/*if(wealth > 0){
					  invest=costEdu;
				 }
			  else{
				  if(wealth < 0)
				  invest=costEdu*iL;
				  }
				  */
		  }
	  else{
		  if(RandomHelper.nextDouble()<abilityStudThres && wealth < 100){
			  InvestEducation=false;
		  }
		  
		  System.out.println("ID " +workerID+ " investe in Edu " +InvestEducation+ " threshold " +abilityStudThres+ " wealth " +wealth);
	  }
	}
	
	public double getInvest(){
		return invest;
	}
	
	public double getWealth(){
		return wealth;
	}

	 	  	  
		/* stato di occupazione/disoccupazione iniziale --->useful per inizializzare labor mkt
		if(RandomHelper.nextDouble()<initialProbabilityToBeEmployed){
		status=0;
	}
	else{
		status=1;
	}
	*/
	
	
	



	/* if(Context.verbouseFlag){
		System.out.println("Creato con l'ID "+workerID+" wealth="+wealth+" occupato="+employed+" wage="+workerWage);
	}
	*/
	 
	 /*per labor market
		public void reset(){
			workerWage=0;
			status=0;
		}
*/
	
	
	
}