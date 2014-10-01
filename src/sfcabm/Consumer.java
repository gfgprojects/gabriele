package sfcabm;

//import sfcabm.Bank;
//import sfcabm.LaborMkt; 
//import sfcabm.Firm;
import repast.simphony.random.RandomHelper;
//import repast.simphony.parameter.Parameters;
//import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;


public class Consumer {
	int identity;
	boolean isStudent;
	//DISPOSABLE INCOME yd = yp - iL - T, diverso dal wage!!
	public double disposableIncome,desiredConsumption,consumption,desiredChangeInWealth,desiredWealth;
	 double saving;
	//CAMBIARE QUANDO DIVENTA ENDOGENO
	 double wealth=RandomHelper.nextDoubleFromTo(-500.0, 500000.00);
	 double workerWage=RandomHelper.nextDoubleFromTo(100, 2000);
	 public static double abilityStudent = RandomHelper.nextDoubleFromTo(0,0.5);
	 //double taxConsumTot=taxRate*consumption;

	 //DEBT
	 double workerLoan;
	 double studentLoan;
	 //spostare iL in banca dopo!
	 double iL=0.03;
	 
	//ABILITY
	 //double sumAbilityWorker; 
	 double sumAbilityStudent;
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
	 
	 
	public Consumer(int workerID){
		super();
		identity = workerID;
		
	}
	
	@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=2.0)
	public void step(){
	    if(isStudent){
	        stepStudent();
	        consumptionStudent();
	    }
	    else{
	        stepWorker();
	        consumptionWorker();
	    }
	    
	    disposableIncome=workerWage+saving-iL*workerLoan-iL*studentLoan;
	    //-taxes
	}

	
	
	private void consumptionWorker() {
		
	}

	private void stepWorker() {
		if(isStudent=false){		
		}
	}
		

	private void consumptionStudent() {
	
	  	}
		
	
	@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=1.0)
	private void stepStudent() {
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
		  }
  }
		
		double count = 1.0 ;
		while ( count <= abilityStudent )
		{
		   count++ ;
		}
		
		//sumAbilityStudent=
	/*	if(InvestEducation=true & DECIDERE COME CONTARE ANNI ISTRUZIONE()>15){
		    isStudent=true;
		}
		*/
		
		System.out.println("ID " +identity+ " investe in Edu " +InvestEducation+ " ability " +abilityStudent+ " count " +count+ " wealth " +wealth);
		//" ability " +abilityStudent+
	}

	
	  
	
	  	
/*@ScheduledMethod(start = 2, interval = 1,shuffle=false, priority=2.0)
	public void consume(){
		// wealth=wealth(t-1)+disposable income(YP - IL - T) - consumo
	
	desiredConsumption= alpha*disposableIncome+beta*wealth;
			//+imitation;
	desiredChangeInWealth=disposableIncome-desiredConsumption;
	desiredWealth=wealth+desiredChangeInWealth;
		
	}
	*/
	 

	
	public double getStudentSpending(){
		return studentSpending;
	}
	
	public double getWealth(){
		return wealth;
	}
	
	public double getAbilityStudent(){
		return getAbilityStudent();
	}
	
	public double getStudentLoan(){
		return studentLoan;
	}

	
	
}
