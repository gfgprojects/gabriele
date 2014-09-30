package sfcabm;

//import sfcabm.Bank;
//import sfcabm.LaborMkt; 
//import sfcabm.Firm;

import java.util.ArrayList;
import repast.simphony.random.RandomHelper;
import repast.simphony.engine.schedule.ScheduledMethod;
/*import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
*/


public class Workers {
	int identity;
	//double initialProbabilityToBeEmployed=0.7;
	//VARIABILI USATE IN LAB MKT
	double reservationWageWorker;
	double workerWage;
	int numApplic;
	//EMPLOYMENT STATUS: DECIDERE SE TENERE O RIMUOVERE IN BASE A LAB MKT
	//int status;
	
	//DISPOSABLE INCOME yd = yp - iL - T, diverso dal wage!!
	public double disposableIncome,desiredConsumption,consumption,desiredChangeInWealth,desiredWealth;
	//savings = deposits
	 int saving;
	//CAMBIARE QUANDO DIVENTA ENDOGENO
	 double wealth=RandomHelper.nextDoubleFromTo(-500.0, 1000.00);
	 
	 //propensioni rivedere calibration
	 double alpha = 0.4;
	 double beta = 0.4;
	 
	 //taxes on consumption, tax rate evolves??...T = \phi c
	 double taxRate = 0.1;
	 double taxConsumTot=taxRate*consumption;
	 
	 //DEBT
	 double workerLoan;
	 double studentLoan;
	 //spostare iL in banca dopo!
	 double iL=0.03;
	 
	 
	//ABILITY
	 double abilityStudent = RandomHelper.nextDoubleFromTo(0,0.5);
	 double sumAbilityWorker; 
	 boolean InvestEducation;
	 double studentSpending;
	 
	
	 int yearsEdu=RandomHelper.nextIntFromTo(1,15);
	 int maxYearsEdu=15;
	 //settare totYearsEdu come somma dei time step. come si fa?!
	 int totYearsEdu;
	 int costEdu=300;
	 boolean successEdu;
	 //DEFINIRE LAMBDA
	 double lambda=0.3;
	 
	
	 //ArrayList<Students> studentsList = new ArrayList<Students>();
	 ArrayList<Double> workersList = new ArrayList<Double>();
	 ArrayList<Double> abilityStudentList = new ArrayList<Double>();

	
	 
	public Workers(int workerID){
		super();
		identity = workerID;
		//livello iniziale di ricchezza
		wealth=RandomHelper.nextDoubleFromTo(-500.0, 1000.00);
		abilityStudent=RandomHelper.nextDoubleFromTo(0,0.5);
		
		workersList.clear();
		abilityStudentList.clear();
		
		//check schedule
		 //@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=2.0)
	}	
	
	@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=1.0)
	//@ScheduledMethod(start = 1, interval = 1,priority=0)
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
		  if(RandomHelper.nextInt()>maxYearsEdu){
		  workersList.add(new Double(1.0));
		  }
		  
	  	}
	  }
	  
	  System.out.println("ID " +identity+ " investe in Edu " +InvestEducation+ " ability " +abilityStudent+ " wealth " +wealth);
	
	  	//workerWage da Labor mkt
		  disposableIncome=workerWage+saving-iL*workerLoan-iL*studentLoan-taxConsumTot;	  
	}
	
@ScheduledMethod(start = 3, interval = 1, shuffle=false, priority=2.0)
public void updateAbility(){
if(InvestEducation=true){
	if(RandomHelper.nextDouble()*0.5<abilityStudent){
		successEdu=true;
		abilityStudentList.add(new Double(1.0));
	}
	else{
		successEdu=false;
	}
	
	System.out.println(" successo anno istruzione "+successEdu);
	}
}
 
@ScheduledMethod(start = 2, interval = 1,shuffle=false, priority=2.0)
	public void consume(){
		//prendere wage da Labor Mkt
		//aggiornare wealth=wealth(t-1)+disposable income(YP - IL - T) - consumo
	
	desiredConsumption= alpha*disposableIncome+beta*wealth;
			//+imitation;
	desiredChangeInWealth=disposableIncome-desiredConsumption;
	desiredWealth=wealth+desiredChangeInWealth;
		
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
