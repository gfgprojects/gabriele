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

	 double sumAbilityStudent=0;
	 //structure degree
	 int degree;
	 int elementary;
	 int intermediate;
	 int college;
	 int bachelor;
	 int master;
	 int phd;
	 double productivity;
	 
	 int numberOfSuccessfulPeriodsOfEducation=0;
	 int numberOfFailedPeriodsOfEducation=0;
	 
	 boolean InvestEducation;
	 double studentSpending;
	 int costEdu=300;
	 
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
		if(Context.verbousFlag){
			System.out.println("Consumer "+identity+" isStudent "+isStudent+" I am a worker");
		}	
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
	
	private void stepStudent() {
		
	//se questo metodo viene eseguito significa che il soggetto ha deciso di studiare
	//se ho un successo nello studio
		if(RandomHelper.nextDouble()<(abilityStudent/0.5)){
			numberOfSuccessfulPeriodsOfEducation++;
			numberOfFailedPeriodsOfEducation=0;
			if(numberOfSuccessfulPeriodsOfEducation>9){
				isStudent=false;
				//calcolare qui titolo di studio e produttivit da lavoratore
			}
	  }
	  else{
		  numberOfFailedPeriodsOfEducation++;
	  }

	//ora decide se continuare diventare worker
		//pensare la condizione per terminare gli studi. Adesso ho messo che dopo tre bocciature si va a lavorare  
	  if(numberOfFailedPeriodsOfEducation>2 || numberOfSuccessfulPeriodsOfEducation>21){
		  isStudent=false;
		  //calcolare qui titolo di studio e produttivit da lavoratore
		  sumAbilityStudent=numberOfSuccessfulPeriodsOfEducation;
			if(sumAbilityStudent<=5){
				degree=elementary;
				productivity=0.05;
			} 
			if(sumAbilityStudent<=8){
				degree=intermediate;
				productivity=0.09;
			}
			if(sumAbilityStudent<=13){
				degree=college;
				productivity=0.12;
			}
			if(sumAbilityStudent<=16){
				degree=bachelor;
				productivity=0.2;
			}
			if(sumAbilityStudent<=18){
				degree=master;
				productivity=0.24;
			}
			if(sumAbilityStudent<=21){
				degree=phd;
				productivity=0.3;
			}
	  }

		
	if(Context.verbousFlag){
		System.out.println("Consumer "+identity+" isStudent "+isStudent+" promozioni "+numberOfSuccessfulPeriodsOfEducation);
		//		System.out.println("ID " +identity+ " ability " +abilityStudent+  " investe in Edu " +InvestEducation+ " updateAbility " +sumAbilityStudent+  " wealth " +wealth);

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
