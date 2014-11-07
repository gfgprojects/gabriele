package sfcabm;

//import sfcabm.Bank;
//import sfcabm.LaborMkt; 
import java.util.ArrayList;
import java.util.Iterator;

import sfcabm.Firm;
import sfcabm.Curriculum;
import sfcabm.AProductDemand;
import repast.simphony.random.RandomHelper;
//import repast.simphony.parameter.Parameters;
//import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
//import repast.simphony.context.Context;
import repast.simphony.util.collections.IndexedIterable;

public class Consumer {
	int identity,age;
	boolean isStudent;
	boolean isWorking;
	Firm myEmployer=null;
	//public double disposableIncome,desiredConsumption,consumption,desiredChangeInWealth,desiredWealth;
	// double saving;
	 double wealth=RandomHelper.nextDoubleFromTo(0.0, 1000.00);
	 //double workerWage=RandomHelper.nextDoubleFromTo(100, 2000);
	  double abilityStudent = RandomHelper.nextDoubleFromTo(0.35,0.5);
	 //double taxConsumTot=taxRate*consumption;
	 double wage=0;
	 //DEBT
	 //double workerLoan;
	 double studentLoan;
	 //spostare iL in banca dopo!
	double iL=0.03;
	double iD=0.01;

	 double sumAbilityStudent=0;
	 //structure degree
	 int degree=0;
/*
	 int elementary;
	 int intermediate;
	 int college;
	 int bachelor;
	 int master;
	 int phd;
	 */
	 double productivity=0;
	 
	 int numberOfSuccessfulPeriodsOfEducation=0;
	 int numberOfFailedPeriodsOfEducation=0;
	 int numberOfConsecutiveFailedPeriodsOfEducation=0;
	 
	 boolean InvestEducation;
	 double studentSpending;

	 double consumption;
	 
	repast.simphony.context.Context<Object> myContext;
	IndexedIterable<Object> firmsList,banksList;
	ArrayList<LaborOffer> laborOfferList = new ArrayList<LaborOffer>();
	ArrayList<AProductDemand> demandsList = new ArrayList<AProductDemand>();
	ArrayList<BankAccount> bankAccountsList = new ArrayList<BankAccount>();
	AProductDemand aProductDemand;
	Curriculum myCurriculum;
	Firm aFirm;	
	LaborMarket myLaborMarket;
	LaborOffer myOffer;

	Iterator<Industry> industriesListIterator;
	Industry anIndustry;

//	 Context<Object> myContext;
	 //DEFINIRE LAMBDA double lambda=0.3;
	 //private ArrayList jobApplicationList= new ArrayList();
	 BankAccount aBankAccount;
	 Bank aBank;
	 
	public Consumer(int consumerID){
		super();
		identity = consumerID;
		isStudent=true;
		isWorking=false;
		
	}
	public Consumer(int consumerID, repast.simphony.context.Context<Object> con){
		super();
		identity = consumerID;
		isStudent=true;
		isWorking=false;
		myContext=con;
	}

	public Consumer(int consumerID, repast.simphony.context.Context<Object> con,double W){
		super();
		identity = consumerID;
		isStudent=true;
		isWorking=false;
		myContext=con;
		wealth=W;
		age=0;
	}

	public void initialize(){
//		age=RandomHelper.nextIntFromTo(1,Context.consumerExitAge);
		age=RandomHelper.nextIntFromTo(40,60);
		int iterations=0;
		int maxIterations=Math.min(age,22);
		while(numberOfFailedPeriodsOfEducation<3 && iterations<maxIterations){
			if(RandomHelper.nextDouble()<(abilityStudent/0.5)){
				numberOfSuccessfulPeriodsOfEducation++;
				numberOfConsecutiveFailedPeriodsOfEducation=0;
			}
			else{
				numberOfFailedPeriodsOfEducation++;
				numberOfConsecutiveFailedPeriodsOfEducation++;
			}
			iterations++;
		}
		if(numberOfFailedPeriodsOfEducation>2 || numberOfSuccessfulPeriodsOfEducation==21){
			isStudent=false;
			//calcolare qui titolo di studio e produttivit da lavoratore
			//		  sumAbilityStudent=numberOfSuccessfulPeriodsOfEducation;
		}
		degree=6;
		if(numberOfSuccessfulPeriodsOfEducation<21){
			degree=5;
		}
		if(numberOfSuccessfulPeriodsOfEducation<18){
			degree=4;
		}
		if(numberOfSuccessfulPeriodsOfEducation<16){
			degree=3;
		}
		if(numberOfSuccessfulPeriodsOfEducation<13){
			degree=2;
		}
		if(numberOfSuccessfulPeriodsOfEducation<8){
			degree=1;
		}
		if(numberOfSuccessfulPeriodsOfEducation<5){
			degree=0;
		} 
		productivity=abilityStudent+(numberOfSuccessfulPeriodsOfEducation)*0.5/21;


		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" age "+(age+5)+" isStudent "+isStudent+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" bocciature "+numberOfFailedPeriodsOfEducation+" cons "+numberOfConsecutiveFailedPeriodsOfEducation+" titolo "+degree+" productivity "+productivity+" abilityStudent "+abilityStudent);
			//		System.out.println("ID " +identity+ " ability " +abilityStudent+  " investe in Edu " +InvestEducation+ " updateAbility " +sumAbilityStudent+  " wealth " +wealth);

		}


	}

public void setupBankAccount(){
	try{
		banksList=myContext.getObjects(Class.forName("sfcabm.Bank"));
	}
	catch(ClassNotFoundException e){
		System.out.println("Class not found");
	}
	ArrayList<Integer> banksPositions=new ArrayList<Integer>();
	int numberOfBanksToBeCustomerOf=Math.min(Context.numberOfBanksAConsumerCanBeCustumerOf,banksList.size());
	for(int i=0;i<banksList.size();i++){
		banksPositions.add(new Integer(i));
	}
	for(int i=0;i<numberOfBanksToBeCustomerOf;i++){
		int position=banksPositions.remove(RandomHelper.nextIntFromTo(0,(banksPositions.size()-1)));
	aBank=(Bank)banksList.get(position);
		if(Context.verboseFlag){
			System.out.println("       open account");
		}
	aBankAccount=new BankAccount(RandomHelper.nextIntFromTo(-500,500),this);
	bankAccountsList.add(aBankAccount);
	aBank.addAccount(aBankAccount);

	}
}



//	@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=2.0)
	public void stepState(){
	    if(isStudent){
	        stepStudentState();
	    }
	    else{
	        stepWorkerState();
	    }
	    
	//    disposableIncome=workerWage+saving-iL*workerLoan-iL*studentLoan;
	    //-taxes
	}

	public void stepConsumption(){
	    if(isStudent){
	        stepStudentConsumption();
	    }
	    else{
	        stepWorkerConsumption();
	    }
	   age++; 
	//    disposableIncome=workerWage+saving-iL*workerLoan-iL*studentLoan;
	    //-taxes
	}


	

	
	
	private void stepWorkerConsumption() {
		double preferenceParameter=RandomHelper.nextDoubleFromTo(0.5,1.5);
		demandsList = new ArrayList<AProductDemand>();

		if(!isWorking){
			wage=(double)Context.unemploymentDole;
		}
		
		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" wage "+wage+ " wealth "+wealth);
		}

			industriesListIterator=OfficeForStatistics.industriesList.iterator();
			while(industriesListIterator.hasNext()){
				anIndustry=industriesListIterator.next();
				int tmpDemand=(int)Math.round(preferenceParameter*wage*anIndustry.getProductAttractiveness());
				aProductDemand=new AProductDemand(anIndustry.getAbsoluteRank(),anIndustry.getRelativeRank(),tmpDemand);
				aProductDemand.inform(identity);
				demandsList.add(aProductDemand);
			}

	}
	
	
	public void receiveLaborDemand(LaborOffer aOffer){
		laborOfferList.add(aOffer);
		if(Context.verboseFlag){
			System.out.println("  Consumer "+identity+" received offer from firm "+aOffer.getSenderID());
		}
	}

	private void stepWorkerState() {
		if(isWorking){
			if(Context.verboseFlag){
				System.out.println("Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" titolo "+degree+" produttivita "+productivity+" abilityStud "+abilityStudent);
			}
		}
		else{
			if(Context.verboseFlag){
				System.out.println("Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" titolo "+degree+" produttivita "+productivity+" abilityStud "+abilityStudent);
			}
			myCurriculum=new Curriculum(this,degree,identity,10,productivity*Context.parameterOfProductivityInProductionFuncion,10.5);
			try{
				firmsList=myContext.getObjects(Class.forName("sfcabm.Firm"));
				myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("sfcabm.LaborMarket"))).get(0);
			}
			catch(ClassNotFoundException e){
				System.out.println("Class not found");
			}

			aFirm=(Firm)firmsList.get(RandomHelper.nextIntFromTo(0,(firmsList.size()-1)));
			if(Context.verboseFlag){
				System.out.println("  sending application to firm "+aFirm.getID());
			}
			aFirm.receiveCurriculum(myCurriculum);
			if(Context.verboseFlag){
				System.out.println("  sending application to labor agency");
			}
			myLaborMarket.receiveCurriculum(myCurriculum);

		}
		
	}


	private void stepStudentConsumption() {
		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" isStudent "+isStudent+" initial wealth "+wealth);
		}	
		consumption=Context.costEdu;
		if(wealth >= 0){
			wealth=wealth*(1+iD)-consumption;
		}
		else{
			wealth=wealth*(1+iL)-consumption;
		}
		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" wage "+wage+" consumption "+consumption+ " wealth "+wealth);
		}
		industriesListIterator=OfficeForStatistics.industriesList.iterator();
		while(industriesListIterator.hasNext()){
			anIndustry=industriesListIterator.next();
			int tmpDemand=(int)Math.round(Context.costEdu*anIndustry.getProductAttractiveness());
			aProductDemand=new AProductDemand(anIndustry.getAbsoluteRank(),anIndustry.getRelativeRank(),tmpDemand);
			aProductDemand.inform(identity);
			demandsList.add(aProductDemand);
		}



	}
	
	private void stepStudentState() {
		
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
			if(numberOfSuccessfulPeriodsOfEducation<21){
				degree=5;
			}
			if(numberOfSuccessfulPeriodsOfEducation<18){
				degree=4;
			}
			if(numberOfSuccessfulPeriodsOfEducation<16){
				degree=3;
			}
			if(numberOfSuccessfulPeriodsOfEducation<13){
				degree=2;
			}
			if(numberOfSuccessfulPeriodsOfEducation<8){
				degree=1;
			}
			if(numberOfSuccessfulPeriodsOfEducation<5){
				degree=0;
			} 
			productivity=abilityStudent+(numberOfSuccessfulPeriodsOfEducation-1)*0.5/21;

		
	if(Context.verboseFlag){
		System.out.println("Consumer "+identity+" isStudent "+isStudent+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" bocciature "+numberOfFailedPeriodsOfEducation+" cons "+numberOfConsecutiveFailedPeriodsOfEducation+" titolo "+degree+" productivity "+productivity);
		//		System.out.println("ID " +identity+ " ability " +abilityStudent+  " investe in Edu " +InvestEducation+ " updateAbility " +sumAbilityStudent+  " wealth " +wealth);

	}
	}

	public void sendInitialJobApplication(){
		if(isStudent){
			if(Context.verboseFlag){
				System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" non spedisco CV perche' sono uno studente");
			}
		}
		else{
			if(RandomHelper.nextDouble()<0.5){
			if(Context.verboseFlag){
				System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" no CV sent ");
			}
			}
			else{
			if(Context.verboseFlag){
				System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" titolo "+degree+" produttivita "+productivity+" abilityStud "+abilityStudent);
			}
			myCurriculum=new Curriculum(this,degree,identity,10,productivity*Context.parameterOfProductivityInProductionFuncion,10.5);
			try{
				firmsList=myContext.getObjects(Class.forName("sfcabm.Firm"));
				myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("sfcabm.LaborMarket"))).get(0);
			}
			catch(ClassNotFoundException e){
				System.out.println("Class not found");
			}

			aFirm=(Firm)firmsList.get(RandomHelper.nextIntFromTo(0,(firmsList.size()-1)));
			if(Context.verboseFlag){
				System.out.println("     sending application to firm "+aFirm.getID());
			}
			aFirm.receiveCurriculum(myCurriculum);
		}

		}
	}

	public void sendJobApplications(){
		if(isStudent){
			if(Context.verboseFlag){
				System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" I am not sending CV because I am a student");
			}
		}
		else{
			if(isWorking){
			}
			else{
			if(Context.verboseFlag){
				System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" titolo "+degree+" produttivita "+productivity+" abilityStud "+abilityStudent);
			}
			myCurriculum=new Curriculum(this,degree,identity,10,productivity*Context.parameterOfProductivityInProductionFuncion,10.5);
			try{
				firmsList=myContext.getObjects(Class.forName("sfcabm.Firm"));
				myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("sfcabm.LaborMarket"))).get(0);
			}
			catch(ClassNotFoundException e){
				System.out.println("Class not found");
			}
			
			ArrayList<Integer> firmsPositions=new ArrayList<Integer>();
			int numberOfApplicationsToSend=Math.min(Context.numberOfJobApplicationAnUneployedSends,firmsList.size());
			switch(Context.firmsWorkersMatching){
				case 0:
					for(int i=0;i<firmsList.size();i++){
						firmsPositions.add(new Integer(i));
					}
					for(int i=0;i<numberOfApplicationsToSend;i++){
						int position=firmsPositions.remove(RandomHelper.nextIntFromTo(0,(firmsPositions.size()-1)));

						aFirm=(Firm)firmsList.get(position);
						if(Context.verboseFlag){
							System.out.println("       sending application to firm "+aFirm.getID());
						}
						aFirm.receiveCurriculum(myCurriculum);
					}
					break;
				case 1: 
					for(int i=0;i<firmsList.size();i++){
						firmsPositions.add(new Integer(i));
					}
					for(int i=0;i<numberOfApplicationsToSend;i++){
						int position=firmsPositions.remove(RandomHelper.nextIntFromTo(0,(firmsPositions.size()-1)));

						aFirm=(Firm)firmsList.get(position);
						if(Context.verboseFlag){
							System.out.println("  sending application to firm "+aFirm.getID());
						}
						aFirm.receiveCurriculum(myCurriculum);
					}
					if(Context.verboseFlag){
						System.out.println("  sending application to office for labor ");
					}
					myLaborMarket.receiveCurriculum(myCurriculum);
					break;
				case 2:
					if(Context.verboseFlag){
						System.out.println("  sending application to office for labor ");
					}
					myLaborMarket.receiveCurriculum(myCurriculum);
					break;
				default: System.out.println("Unknown workers firms matching mechanism");
					 break;
			}
		}

		}
	}




	public void jobObtained(Firm employer){
		isWorking=true;
		myEmployer=employer;
//		wage=Context.parameterOfProductivityInProductionFuncion*productivity;
		int employerID=myEmployer.getID();
		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" productivity "+productivity+" ... Wow, I got a job from firm "+employerID+" wage "+wage);
		}
	}
	public void setWage(double w){
		wage=w;
		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" isWorking "+isWorking+" degree "+degree+" productivity "+productivity+" my employer firm "+myEmployer.getID()+" sent me the wage "+wage);
		}
	}
	public void receiveRetirementNew(){
		isWorking=false;
		if(Context.verboseFlag){
			System.out.println("       Consumer "+identity+" isWorking "+isWorking+" degree "+degree+" productivity "+productivity+" my employer firm "+myEmployer.getID()+" retire me age "+age);
		}
		myEmployer=null;
	}
	
	public void receiveFiredNew(){
		isWorking=false;
		if(Context.verboseFlag){
			System.out.println("       Consumer "+identity+" isWorking "+isWorking+" degree "+degree+" productivity "+productivity+" my employer firm "+myEmployer.getID()+" fired me ");
		}
		myEmployer=null;
	}
	
	public void receiveHiredNew(Firm employer){
		isWorking=true;
		myEmployer=employer;
		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" isWorking "+isWorking+" degree "+degree+" productivity "+productivity+" wow firm "+myEmployer.getID()+" hired me ");
		}
	}

	public void computeWealth(){
		wealth=0;
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			wealth=wealth+aBankAccount.getAccount();
		}
System.out.println("     Consumer "+identity+" wealth "+wealth);
		
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
	public int getIdentity(){
		return identity;
	}
	public double getProductivity(){
		return productivity;
	}
	public ArrayList getOrders(){
		return demandsList;
	
	}
	public int getDegree(){
		return degree;
	}
	public int getAge(){
		return age;
	}
	public boolean getIsWorkingFlag(){
		return isWorking;
	}


	public void showInfoOnIndustries(){
System.out.println("Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" I am ready to decide my consumption using the following info");
		if(Context.verboseFlag){
			industriesListIterator=OfficeForStatistics.industriesList.iterator();

			while(industriesListIterator.hasNext()){
				anIndustry=industriesListIterator.next();
				System.out.println("absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction());

			}

		}


	}
	
}
