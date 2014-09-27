package sfcabm;

import sfcabm.Bank;
import sfcabm.LaborMkt; 

import repast.simphony.random.RandomHelper;
import repast.simphony.engine.schedule.ScheduledMethod;


public class Workers {
	int workerID;
	//generation age distribution per inizializzare i workers: diverse eta, diverse condizioni lavorative e di studio: HETEROG
	double initialProbabilityToBeEmployed=0.7;
	public boolean employed;
	double reservationWageWorker;
	double workerWage;
	int numApplic;
	
	
	//DISPOSABLE INCOME yd = yp - iL - T
	 double disposableIncome;
	 int consumption;
	 int saving;
	 double wealth;
	 double deposits, workerLoan;
	//taxes on consumption, tax rate evolves??...T = \phi c
	 double taxRate = 0.1;
	 double taxConsumTot=taxRate*consumption;
	 
	//propensioni rivedere calibration
	 double alpha = 0.4;
	 double beta = 0.4;
	
	//ABILITIES STRUCTURE
	// int hCollege = 2;
	// int hBachelor = 4;
	 // int hMaster = 6;
	 // int hPhd = 8;
	//totAbility = sigma nel modello teorico
	// int totAbility;
	//EXPENDITURE for attaining higher degree
	// int expCollege = 100;
	 // int expBachelor = 300;
	 // int expMaster = 500;
	 // int expPhd = 800;
	//probability to invest in higher Edu
	boolean InvestEducation;
	double probabilityInvestEdu = 0.5;
	

	public Workers(int ID){
		super();
		//identity = workerID;
		//int initialAge = RandomHelper.nextIntFromTo(15,30);
		//livello iniziale di ricchezza
		wealth=0;
		
		//stato di occupazione iniziale
		if(RandomHelper.nextDouble()<initialProbabilityToBeEmployed){
		employed=true;
	}
	else{
		employed=false;
	}

	//definire Bank.il in bank!
//disposableIncome=workerWage+deposits-Bank.iL*workerLoan-taxConsumTot;


	/*decidere se tenere qui o spostare in labor mkt
	 * if(Context.verbouseFlag){
		System.out.println("Creato con l'ID "+workerID+" wealth="+wealth+" occupato="+employed+" wage="+workerWage);
	}
	*/
	}

	@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=2.0)
	public void desiredC(){
	//wage
	
	//*********definire bank.iL
	//**definire workerLoan, condizionata a investimento in edu
	 if(RandomHelper.nextDouble()>probabilityInvestEdu){
		 InvestEducation=true;
		 //workerloan = ??;
	 }
 else{
	 if(RandomHelper.nextDouble()<probabilityInvestEdu){
		 InvestEducation=false;
		 //workerloan = 0;
	 }
	 
		public void reset(){
			workerWage=0;
			
		}

		//if(Context.verbouseFlag){
		//decidere cosa stampare nei dati qui
			//System.out.println("Creato con l'ID "+identity+" wealth="+wealth+" occupato="+employed+" wage="+wage);
		//}
	
	
}