package sfcabm;

//import sfcabm.Bank;
//import sfcabm.LaborMkt; 
import sfcabm.Firm;


import repast.simphony.random.RandomHelper;
//import repast.simphony.parameter.Parameters;
//import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
//import repast.simphony.context.Context;
import repast.simphony.util.collections.IndexedIterable;

public class Consumer {
	int identity;
	boolean isStudent;
	boolean isWorking;
	//public double disposableIncome,desiredConsumption,consumption,desiredChangeInWealth,desiredWealth;
	// double saving;
	 double wealth=RandomHelper.nextDoubleFromTo(0.0, 1000.00);
	 //double workerWage=RandomHelper.nextDoubleFromTo(100, 2000);
	  double abilityStudent = RandomHelper.nextDoubleFromTo(0.0,0.5);
	 //double taxConsumTot=taxRate*consumption;

	 //DEBT
	 //double workerLoan;
	 double studentLoan;
	 //spostare iL in banca dopo!
	double iL=0.03;
	double iD=0.01;

	 double sumAbilityStudent=0;
	 //structure degree
	 int degree;
/*
	 int elementary;
	 int intermediate;
	 int college;
	 int bachelor;
	 int master;
	 int phd;
	 */
	 double productivity;
	 
	 int numberOfSuccessfulPeriodsOfEducation=0;
	 int numberOfFailedPeriodsOfEducation=0;
	 int numberOfConsecutiveFailedPeriodsOfEducation=0;
	 
	 boolean InvestEducation;
	 double studentSpending;
	 int costEdu=300;

	 double consumption;
	 repast.simphony.context.Context<Object> myContext;
	 IndexedIterable<Object> firmsList;


//	 Context<Object> myContext;
	 //DEFINIRE LAMBDA double lambda=0.3;
	 //private ArrayList jobApplicationList= new ArrayList();
	 
	public Consumer(int consumerID){
		super();
		identity = consumerID;
		isStudent=true;
		isWorking=false;
		
	}
		public Consumer(int consumerID, repast.simphony.context.Context<Object> con){
//		public Consumer(int consumerID, repast.simphony.context.Context<Object> con){
		super();
		identity = consumerID;
		isStudent=true;
		isWorking=false;
		myContext=con;
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
		if(isWorking){
			if(Context.verbousFlag){
				System.out.println("Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" titolo "+degree+" produttivita "+productivity+" abilityStud "+abilityStudent);
			}
		}
		else{
			if(Context.verbousFlag){
				System.out.println("Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" titolo "+degree+" produttivita "+productivity+" abilityStud "+abilityStudent);
			}
			try{
				firmsList=myContext.getObjects(Class.forName("sfcabm.Firm"));
			}
			catch(ClassNotFoundException e){
				System.out.println("Class not found");
			}
			System.out.println("sending application to firm "+((Firm)firmsList.get(RandomHelper.nextIntFromTo(0,(firmsList.size()-1)))).getID());

		}



	}


	private void consumptionStudent() {
		if(Context.verbousFlag){
			System.out.print("Consumer "+identity+" isStudent "+isStudent+" initial wealth "+wealth);
		}	
		consumption=costEdu;
		if(wealth >= 0){
			  wealth=wealth*(1+iD)-consumption;
		 }
	  else{
			  wealth=wealth*(1+iL)-consumption;
	  	}
		if(Context.verbousFlag){
			System.out.println(" final wealth "+wealth);
		}	
	}
	
	private void stepStudent() {
		
	//se questo metodo viene eseguito significa che il soggetto ha deciso di studiare
	//se ho un successo nello studio
		if(RandomHelper.nextDouble()<(abilityStudent/0.5)){
			numberOfSuccessfulPeriodsOfEducation++;
			numberOfConsecutiveFailedPeriodsOfEducation=0;
	  }
	  else{
		  numberOfFailedPeriodsOfEducation++;
		  numberOfConsecutiveFailedPeriodsOfEducation++;
	  }

	//ora decide se continuare diventare worker
		//pensare la condizione per terminare gli studi. Adesso ho messo che dopo tre bocciature si va a lavorare  
//	  if(numberOfConsecutiveFailedPeriodsOfEducation>2 || numberOfSuccessfulPeriodsOfEducation>21){
	  if(numberOfFailedPeriodsOfEducation>2 || numberOfSuccessfulPeriodsOfEducation>21){
		  isStudent=false;
		  //calcolare qui titolo di studio e produttivit da lavoratore
//		  sumAbilityStudent=numberOfSuccessfulPeriodsOfEducation;
	  }
			degree=6;
			if(numberOfSuccessfulPeriodsOfEducation<=21){
				degree=5;
				productivity=0.3;
			}
			if(numberOfSuccessfulPeriodsOfEducation<=18){
				degree=4;
				productivity=0.24;
			}
			if(numberOfSuccessfulPeriodsOfEducation<=16){
				degree=3;
				productivity=0.2;
			}
			if(numberOfSuccessfulPeriodsOfEducation<=13){
				degree=2;
				productivity=0.12;
			}
			if(numberOfSuccessfulPeriodsOfEducation<=8){
				degree=1;
				productivity=0.09;
			}
			if(numberOfSuccessfulPeriodsOfEducation<=5){
				degree=0;
				productivity=0.05;
			} 
			productivity=abilityStudent+(numberOfSuccessfulPeriodsOfEducation-1)*0.5/21;

		
	if(Context.verbousFlag){
		System.out.println("Consumer "+identity+" isStudent "+isStudent+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" bocciature "+numberOfFailedPeriodsOfEducation+" cons "+numberOfConsecutiveFailedPeriodsOfEducation+" titolo "+degree+" productivity "+productivity);
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
