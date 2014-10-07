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
	//public double disposableIncome,desiredConsumption,consumption,desiredChangeInWealth,desiredWealth;
	// double saving;
	 double wealth=RandomHelper.nextDoubleFromTo(0.0, 1000.00);
	 //double workerWage=RandomHelper.nextDoubleFromTo(100, 2000);
	 public static double abilityStudent = RandomHelper.nextDoubleFromTo(0.0,0.5);
	 //double taxConsumTot=taxRate*consumption;

	 //DEBT
	 //double workerLoan;
	 double studentLoan;
	 //spostare iL in banca dopo!
	double iL=0.03;

	 double sumAbilityStudent;
	 boolean InvestEducation;
	 double studentSpending;
	
	// int yearsEdu=RandomHelper.nextIntFromTo(1,15);
	 int maxYearsEdu=15;
	 int totYearsEdu;
	 int costEdu=300;
	 //boolean successEdu;
	 //DEFINIRE LAMBDA double lambda=0.3;
	 //private ArrayList jobApplicationList= new ArrayList();
	 
	public Consumer(int consumerID){
		super();
		identity = consumerID;
		isStudent=true;
		
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
	    
	//    disposableIncome=workerWage+saving-iL*workerLoan-iL*studentLoan;
	    //-taxes
	}

	
	
	private void consumptionWorker() {
		
	}

	private void stepWorker() {
	}


	private void consumptionStudent() {
		if(wealth >= 300){
			  studentSpending=costEdu;
		 }
	  else{
		  if(wealth < 300)
		  studentLoan=costEdu*iL;
		  }
	  	}
	
	@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=1.0)
	private void stepStudent() {
		sumAbilityStudent=0;
		
	if(Context.verbousFlag){
		System.out.println("Stepping Student "+identity);
	}	

		if(RandomHelper.nextDouble()>abilityStudent & wealth > 100){
			InvestEducation=true;
	  }
  else{
	  if(RandomHelper.nextDouble()<abilityStudent & wealth < 100){
		  InvestEducation=false;
		  }
  }
		
		while (isStudent=true) {
		if(RandomHelper.nextDouble()>abilityStudent){
			double count = 0.0;
			if ( count <= abilityStudent ){
				count++;
			}
			sumAbilityStudent = count++;
	}
		}
		
	if(Context.verbousFlag){

		System.out.println("ID " +identity+ " ability " +abilityStudent+  " investe in Edu " +InvestEducation+ " updateAbility " +sumAbilityStudent+  " wealth " +wealth);

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
	
	public double getSumAbility(){
		return sumAbilityStudent;
	}
	
	public double getStudentLoan(){
		return studentLoan;
	}
	
	
}
