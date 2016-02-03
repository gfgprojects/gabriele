package sfcabm;

//import sfcabm.Bank;
//import sfcabm.LaborMkt; 
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;

import sfcabm.Firm;
import sfcabm.Curriculum;
import sfcabm.AProductDemand;
import repast.simphony.random.RandomHelper;
//import repast.simphony.parameter.Parameters;
//import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
//import repast.simphony.context.Context;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.essentials.RepastEssentials;
public class Consumer {
	int identity,age;
	boolean isStudent,isWorking,isRetired;
	Firm myEmployer=null;
	//public double disposableIncome,desiredConsumption,consumption,desiredChangeInWealth,desiredWealth;
	 double saving=0;
	 double residualResourcesAfterConsuming=0;
	 double wealth=RandomHelper.nextDoubleFromTo(0.0, 1000.00);
	 //double workerWage=RandomHelper.nextDoubleFromTo(100, 2000);
	  double abilityStudent = RandomHelper.nextDoubleFromTo(Context.minAbilityStudent,Context.maxAbilityStudent);
	 //double taxConsumTot=taxRate*consumption;
	 double wage=0;
	 double disposableIncome=0;
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

	 double consumption,desiredDemand,financialResourcesInBankAccounts,debtsInBankAccounts,unpaidAmountInBankAccounts,effectiveConsumption,disposableIncomeWhenDecidingDesiredConsumption,disposableIncomewhenConsuming;
	 
	repast.simphony.context.Context<Object> myContext;
	IndexedIterable<Object> firmsList,banksList;
	ArrayList<LaborOffer> laborOfferList = new ArrayList<LaborOffer>();
	ArrayList<AProductDemand> demandsList = new ArrayList<AProductDemand>();
	ArrayList<BankAccount> bankAccountsList = new ArrayList<BankAccount>();
	ArrayList<BankAccount> bankAccountsListWithNoUnpaidAmount = new ArrayList<BankAccount>();
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
	 BankAccount aBankAccount,bestBankAccount,worstBankAccount;
	 Bank aBank;
	 
	public Consumer(int consumerID){
		super();
		identity = consumerID;
		isStudent=true;
		isWorking=false;
		isRetired=false;
		
	}
	public Consumer(int consumerID, repast.simphony.context.Context<Object> con){
		super();
		identity = consumerID;
		isStudent=true;
		isWorking=false;
		isRetired=false;
		myContext=con;
	}

	public Consumer(int consumerID, repast.simphony.context.Context<Object> con,ArrayList<BankAccount> bankAcc){
		super();
		identity = consumerID;
		isStudent=true;
		isWorking=false;
		isRetired=false;
		myContext=con;
		wealth=0;
		bankAccountsList=bankAcc;
		age=0;
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			wealth+=aBankAccount.getAccount();
		}
	}

	public void initialize(){
//		age=RandomHelper.nextIntFromTo(1,Context.consumerExitAge);
		age=RandomHelper.nextIntFromTo(0,Context.consumerExitAge-1);
		int iterations=0;
		int maxIterations=Math.min(age,Context.maxNumberPeriodsOfEducation+Context.maxNumberOfFailedPeriodsOfEducation);
		while(numberOfFailedPeriodsOfEducation<(Context.maxNumberOfFailedPeriodsOfEducation+1) && iterations<maxIterations){
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
		if(numberOfSuccessfulPeriodsOfEducation>Context.maxNumberPeriodsOfEducation){
			numberOfSuccessfulPeriodsOfEducation=Context.maxNumberPeriodsOfEducation;
		}
		if(numberOfFailedPeriodsOfEducation>Context.maxNumberOfFailedPeriodsOfEducation || numberOfSuccessfulPeriodsOfEducation==21){
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
			System.out.println("     Consumer "+identity+" age "+(age+5)+" isStudent "+isStudent+" successes "+numberOfSuccessfulPeriodsOfEducation+" failures "+numberOfFailedPeriodsOfEducation+" consecutive failures "+numberOfConsecutiveFailedPeriodsOfEducation+" degree "+degree+" productivity "+productivity+" abilityStudent "+abilityStudent);
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
	aBankAccount=new BankAccount(RandomHelper.nextIntFromTo(Context.minConsumerInitialBankAccount,Context.maxConsumerInitialBankAccount),this,aBank);
	bankAccountsList.add(aBankAccount);
	aBank.addAccount(aBankAccount);

	}
}



//	@ScheduledMethod(start = 1, interval = 1,shuffle=false,priority=2.0)

/*
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
*/	    

	public void payBackBankDebt(){

	//reset unpaid amount of bank accounts
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			aBankAccount.setUnpaidAmount(0);
		}


		if(Context.saveMicroData){
			try{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					OfficeForStatistics.microDataWriterForConsumersBankAccounts01.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForConsumersBankAccounts01.append("\n");
				OfficeForStatistics.microDataWriterForConsumersBankAccounts01.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}


//compute totalSmountoToRefund and FinancialResourcesInBankAccounts
		double amountOfThisBankAccount,resourcesAvailableToRefund;
		double totalAmountToRefund=0;
		financialResourcesInBankAccounts=0;
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			amountOfThisBankAccount=aBankAccount.getAccount();
			if(amountOfThisBankAccount<0){
				totalAmountToRefund+=-(amountOfThisBankAccount-aBankAccount.getAllowedCredit());
			}
			else{
				financialResourcesInBankAccounts+=amountOfThisBankAccount;
			}
		}
//assign disposableIncome
		disposableIncome=wage;
//resize disposableIncome
		if(totalAmountToRefund>0){
			resourcesAvailableToRefund=financialResourcesInBankAccounts+disposableIncome-Context.subsistenceConsumption;
			if(resourcesAvailableToRefund<0){
				resourcesAvailableToRefund=0;
			}
			if(Context.verboseFlag){
				System.out.println("     Consumer "+getIdentity()+" to refund "+totalAmountToRefund+" financial resource in bank accounts "+financialResourcesInBankAccounts+" wage "+disposableIncome+" resourcesAvailableToRefund=(wage+deposits-minCinsumption) "+resourcesAvailableToRefund);
			}
			if(resourcesAvailableToRefund>=totalAmountToRefund){
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					amountOfThisBankAccount=aBankAccount.getAccount();
					if(amountOfThisBankAccount<0){
						aBankAccount.setAccount(aBankAccount.getAllowedCredit());
					}
				}
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					amountOfThisBankAccount=aBankAccount.getAccount();
					if(amountOfThisBankAccount>0){
						if(totalAmountToRefund>=amountOfThisBankAccount){
							totalAmountToRefund+=-amountOfThisBankAccount;
							resourcesAvailableToRefund+=-amountOfThisBankAccount;
							aBankAccount.setAccount(0);
						}
						else{
							aBankAccount.setAccount(amountOfThisBankAccount-totalAmountToRefund);
							resourcesAvailableToRefund+=-totalAmountToRefund;
							totalAmountToRefund=0;
						}
					}
				}
				if(totalAmountToRefund>0){
					disposableIncome+=-totalAmountToRefund;
					resourcesAvailableToRefund+=-totalAmountToRefund;
					totalAmountToRefund=0;
				}


			}
			else{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					amountOfThisBankAccount=aBankAccount.getAccount();
					if(amountOfThisBankAccount>0){
						aBankAccount.setAccount(0);
					}
				}
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					amountOfThisBankAccount=aBankAccount.getAccount();
					if(amountOfThisBankAccount<0){
						double toPayBakToThisBankAccount=-(aBankAccount.getAccount()-aBankAccount.getAllowedCredit());
						if(resourcesAvailableToRefund>toPayBakToThisBankAccount){
							resourcesAvailableToRefund+=-toPayBakToThisBankAccount;
							aBankAccount.setAccount(aBankAccount.getAllowedCredit());
							totalAmountToRefund+=-toPayBakToThisBankAccount;
						}
						else{
//							aBankAccount.setDemandedCredit(aBankAccount.getAccount()+resourcesAvailableToRefund);
							aBankAccount.increaseUnpaidAmount(-aBankAccount.getAccount()+aBankAccount.getAllowedCredit()-resourcesAvailableToRefund);
							aBankAccount.setAccount(aBankAccount.getAccount()+resourcesAvailableToRefund);
							totalAmountToRefund+=-resourcesAvailableToRefund;
							//							resourcesAvailableToRefund=0;
							resourcesAvailableToRefund=0;
						}
					}
				}
				disposableIncome=Context.subsistenceConsumption;
			}
			if(Context.verboseFlag){
				System.out.println("     totalAmountToRefund "+totalAmountToRefund+" wage "+wage+" resourcesAvailableToRefund (wage+deposits-minimumConsumption) "+resourcesAvailableToRefund+" disposableIncome "+disposableIncome);
			}
		}

		if(Context.saveMicroData){
			try{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					OfficeForStatistics.microDataWriterForConsumersBankAccounts02.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForConsumersBankAccounts02.append("\n");
				OfficeForStatistics.microDataWriterForConsumersBankAccounts02.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}


	
	}

		


	

	public void stepDesiredConsumption(){
		if(Context.saveMicroData){
			try{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					OfficeForStatistics.microDataWriterForConsumersBankAccounts03.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForConsumersBankAccounts03.append("\n");
				OfficeForStatistics.microDataWriterForConsumersBankAccounts03.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}


	    if(isStudent){
	        stepStudentConsumption();
	    }
	    else{
	        stepWorkerConsumption();
	    }
	   age++; 
	//    disposableIncome=workerWage+saving-iL*workerLoan-iL*studentLoan;
	    //-taxes

		if(Context.saveMicroData){
			try{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					OfficeForStatistics.microDataWriterForConsumersBankAccounts04.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForConsumersBankAccounts04.append("\n");
				OfficeForStatistics.microDataWriterForConsumersBankAccounts04.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}





	}


	

	
	
	private void stepWorkerConsumption() {
		double preferenceParameter=RandomHelper.nextDoubleFromTo(Context.minPreferenceParameter,Context.maxPreferenceParameter);
		financialResourcesInBankAccounts=0;
		debtsInBankAccounts=0;
		unpaidAmountInBankAccounts=0;
		double amountOfThisBankAccount,amountOfBestBankAccount,amountOfWorstBankAccount;
		int positionOfBestAccount,positionOfWorstAccount;
		demandsList = new ArrayList<AProductDemand>();
		desiredDemand=0;

		if(!isWorking){
			wage=(double)Context.unemploymentDole;
			disposableIncome=wage;
			if(preferenceParameter<1){
				preferenceParameter=1;
			}
		}

		if(disposableIncome<=Context.subsistenceConsumption && preferenceParameter<1){
			preferenceParameter=1;
		}

		disposableIncomeWhenDecidingDesiredConsumption=disposableIncome;



		wealth=0;
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			amountOfThisBankAccount=aBankAccount.getAccount();
				wealth+=amountOfThisBankAccount;
		}

		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" wage "+wage+" disposableIncome "+disposableIncome+" wealth "+wealth);
		}

		desiredDemand=preferenceParameter*disposableIncome;

		industriesListIterator=OfficeForStatistics.industriesList.iterator();
		while(industriesListIterator.hasNext()){
			anIndustry=industriesListIterator.next();
//			int tmpDemand=(int)Math.round(desiredDemand*anIndustry.getProductAttractiveness());
			double tmpDemand=desiredDemand*anIndustry.getProductAttractiveness();
			aProductDemand=new AProductDemand(anIndustry.getAbsoluteRank(),anIndustry.getRelativeRank(),tmpDemand);
			aProductDemand.inform(identity);
			demandsList.add(aProductDemand);
		}
//computation of financial resources
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			amountOfThisBankAccount=aBankAccount.getAccount();
			unpaidAmountInBankAccounts+=aBankAccount.getUnpaidAmount();
			if(amountOfThisBankAccount>0){
				financialResourcesInBankAccounts+=amountOfThisBankAccount;
			}
			else{
				debtsInBankAccounts+=amountOfThisBankAccount;
			}
		}

//identify the worst bank account (where the possible unused income will be deposited) and accounts with no unpaid amount
		positionOfWorstAccount=0;
		aBankAccount=(BankAccount)bankAccountsList.get(0);
		amountOfWorstBankAccount=aBankAccount.getAccount();
		bankAccountsListWithNoUnpaidAmount = new ArrayList<BankAccount>();
		if(aBankAccount.getUnpaidAmount()>0){
		}
		else{
			bankAccountsListWithNoUnpaidAmount.add(aBankAccount);
		}

		for(int j=1;j<bankAccountsList.size();j++){
			aBankAccount=(BankAccount)bankAccountsList.get(j);
			amountOfThisBankAccount=aBankAccount.getAccount();
			if(amountOfThisBankAccount<amountOfWorstBankAccount){
				amountOfWorstBankAccount=amountOfThisBankAccount;
				positionOfWorstAccount=j;
			}

		}
		worstBankAccount=(BankAccount)bankAccountsList.get(positionOfWorstAccount);

//identify the best bank account (the one with best position among those with no unpaid amount. Consumers will ask credit in this account if they want to consume more than the available resources)

		if(bankAccountsListWithNoUnpaidAmount.size()>0){
			positionOfBestAccount=0;
			aBankAccount=(BankAccount)bankAccountsListWithNoUnpaidAmount.get(0);
			amountOfBestBankAccount=aBankAccount.getAccount();
			for(int j=1;j<bankAccountsListWithNoUnpaidAmount.size();j++){
				aBankAccount=(BankAccount)bankAccountsListWithNoUnpaidAmount.get(j);
				amountOfThisBankAccount=aBankAccount.getAccount();
				if(amountOfThisBankAccount>amountOfBestBankAccount){
					amountOfBestBankAccount=amountOfThisBankAccount;
					positionOfBestAccount=j;
				}
			}
			bestBankAccount=(BankAccount)bankAccountsList.get(positionOfBestAccount);
		}
		else{
			bestBankAccount=null;
		}

		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" n of banks with no unpaid amount "+bankAccountsListWithNoUnpaidAmount.size());
		}

//ask for new credit
		if(desiredDemand+unpaidAmountInBankAccounts>disposableIncome+financialResourcesInBankAccounts){
			if(bankAccountsListWithNoUnpaidAmount.size()>0){
				bestBankAccount.setDesiredCredit(0.0,desiredDemand+unpaidAmountInBankAccounts-disposableIncome-financialResourcesInBankAccounts);
			}
		}
	}
	
	
	public void receiveLaborDemand(LaborOffer aOffer){
		laborOfferList.add(aOffer);
		if(Context.verboseFlag){
			System.out.println("  Consumer "+identity+" received offer from firm "+aOffer.getSenderID());
		}
	}

/*
public void stepWorkerState() {
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
*/


	private void stepStudentConsumption() {
		double preferenceParameter=RandomHelper.nextDoubleFromTo(Context.minPreferenceParameter,Context.maxPreferenceParameter);
		if(preferenceParameter<1){
			preferenceParameter=1;
		}
		financialResourcesInBankAccounts=0;
		debtsInBankAccounts=0;
		unpaidAmountInBankAccounts=0;
		double amountOfThisBankAccount,amountOfBestBankAccount,amountOfWorstBankAccount;
		int positionOfBestAccount,positionOfWorstAccount;
		demandsList = new ArrayList<AProductDemand>();
		desiredDemand=0;

		wage=0;
		disposableIncome=0;
		disposableIncomeWhenDecidingDesiredConsumption=disposableIncome;

		wealth=0;
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			amountOfThisBankAccount=aBankAccount.getAccount();
			wealth+=amountOfThisBankAccount;
		}

		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" wage "+wage+" disposableIncome "+disposableIncome+" wealth "+wealth);
		}

		desiredDemand=Context.costEdu;
		industriesListIterator=OfficeForStatistics.industriesList.iterator();
		while(industriesListIterator.hasNext()){
			anIndustry=industriesListIterator.next();
			int tmpDemand=(int)Math.round(desiredDemand*anIndustry.getProductAttractiveness());
//			desiredDemand+=tmpDemand;
			aProductDemand=new AProductDemand(anIndustry.getAbsoluteRank(),anIndustry.getRelativeRank(),tmpDemand);
			aProductDemand.inform(identity);
			demandsList.add(aProductDemand);
		}

		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			amountOfThisBankAccount=aBankAccount.getAccount();
			unpaidAmountInBankAccounts+=aBankAccount.getUnpaidAmount();
			if(amountOfThisBankAccount>0){
				financialResourcesInBankAccounts+=amountOfThisBankAccount;
			}
			else{
				debtsInBankAccounts+=amountOfThisBankAccount;
			}
		}

		//identify the worst bank account (where the possible unused income will be deposited) and accounts with no unpaid amount
		positionOfWorstAccount=0;
		aBankAccount=(BankAccount)bankAccountsList.get(0);
		amountOfWorstBankAccount=aBankAccount.getAccount();
		bankAccountsListWithNoUnpaidAmount = new ArrayList<BankAccount>();
		if(aBankAccount.getUnpaidAmount()>0){
		}
		else{
			bankAccountsListWithNoUnpaidAmount.add(aBankAccount);
		}

		for(int j=1;j<bankAccountsList.size();j++){
			aBankAccount=(BankAccount)bankAccountsList.get(j);
			amountOfThisBankAccount=aBankAccount.getAccount();
			if(amountOfThisBankAccount<amountOfWorstBankAccount){
				amountOfWorstBankAccount=amountOfThisBankAccount;
				positionOfWorstAccount=j;
			}

		}
		worstBankAccount=(BankAccount)bankAccountsList.get(positionOfWorstAccount);

		//identify the best bank account (the one with best position among those with no unpaid amount. Consumers will ask credit in this account if they want to consume more than the available resources)
		if(bankAccountsListWithNoUnpaidAmount.size()>0){
			positionOfBestAccount=0;
			aBankAccount=(BankAccount)bankAccountsListWithNoUnpaidAmount.get(0);
			amountOfBestBankAccount=aBankAccount.getAccount();
			for(int j=1;j<bankAccountsListWithNoUnpaidAmount.size();j++){
				aBankAccount=(BankAccount)bankAccountsListWithNoUnpaidAmount.get(j);
				amountOfThisBankAccount=aBankAccount.getAccount();
				if(amountOfThisBankAccount>amountOfBestBankAccount){
					amountOfBestBankAccount=amountOfThisBankAccount;
					positionOfBestAccount=j;
				}
			}
			bestBankAccount=(BankAccount)bankAccountsList.get(positionOfBestAccount);
		}
		else{
			bestBankAccount=null;
		}

		if(Context.verboseFlag){
			System.out.println("     Consumer "+identity+" n of banks with no unpaid amount "+bankAccountsListWithNoUnpaidAmount.size());
		}

		//ask for new credit
			if(desiredDemand+unpaidAmountInBankAccounts>disposableIncome+financialResourcesInBankAccounts){
				if(bankAccountsListWithNoUnpaidAmount.size()>0){
					bestBankAccount.setDesiredCredit(0.0,desiredDemand+unpaidAmountInBankAccounts-disposableIncome-financialResourcesInBankAccounts);
//				if(desiredDemand+unpaidAmountInBankAccounts>wage+financialResourcesInBankAccounts){
//					bestBankAccount.setDesiredCredit(0.0,desiredDemand+unpaidAmountInBankAccounts-wage-financialResourcesInBankAccounts);
				}
		}
	}
	

	
	public void stepStudentState() {
		if(isStudent){	
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
			if(numberOfFailedPeriodsOfEducation>Context.maxNumberOfFailedPeriodsOfEducation || numberOfSuccessfulPeriodsOfEducation>21){
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
				System.out.println("     student "+identity+" isStudent "+isStudent+" successes "+numberOfSuccessfulPeriodsOfEducation+" failures "+numberOfFailedPeriodsOfEducation+" consecutive failures "+numberOfConsecutiveFailedPeriodsOfEducation+" degree "+degree+" productivity "+productivity);
				//		System.out.println("ID " +identity+ " ability " +abilityStudent+  " investe in Edu " +InvestEducation+ " updateAbility " +sumAbilityStudent+  " wealth " +wealth);

			}

			}
		}

		public void adjustConsumptionAccordingToExtendedCredit(){
			if(Context.saveMicroData){
				try{
					for(int i=0;i<bankAccountsList.size();i++){
						aBankAccount=(BankAccount)bankAccountsList.get(i);
						OfficeForStatistics.microDataWriterForConsumersBankAccounts05.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
					}
					OfficeForStatistics.microDataWriterForConsumersBankAccounts05.append("\n");
					OfficeForStatistics.microDataWriterForConsumersBankAccounts05.flush();
				}
				catch(IOException e) {System.out.println("IOException");}
			}


			double allowedDemand=0;
			double askedCredit=0;
			double allowedCredit=0;
			residualResourcesAfterConsuming=0;
			if(bankAccountsListWithNoUnpaidAmount.size()>0){
				askedCredit=bestBankAccount.getDemandedCredit();
				allowedCredit=bestBankAccount.getAllowedCredit();
				if(allowedCredit>askedCredit){
					allowedDemand=desiredDemand-(allowedCredit-askedCredit);
					if(allowedDemand<Context.subsistenceConsumption){
						allowedDemand=Context.subsistenceConsumption;
					}
				}
				else{
					allowedDemand=desiredDemand;
				}
			}
			else{
				if(desiredDemand>(disposableIncome+financialResourcesInBankAccounts)){
					allowedDemand=disposableIncome+financialResourcesInBankAccounts;
				}
				else{
					allowedDemand=desiredDemand;
				}
			}



			if(Context.verboseFlag){
				System.out.println("     Consumer "+identity+" askedCred "+askedCredit+" allow "+allowedCredit+" wage "+wage+" desired Demand "+desiredDemand+" allowed Demand "+allowedDemand);
			}
			for(int i=0;i<demandsList.size();i++){
				aProductDemand=demandsList.get(i);
				aProductDemand.adjustDemand(identity,allowedDemand/desiredDemand);
			}
		}

		public void updateBankAccountAccordingToEffectiveConsumption(){
//compute effective consumption
			consumption=0;
			for(int j=0;j<demandsList.size();j++){
				aProductDemand=demandsList.get(j);
				consumption+=aProductDemand.getDemand();
			}
		if(Context.verboseFlag){
			System.out.println("    consumer "+getIdentity()+" consumption "+consumption+" disposableIncome "+disposableIncome);
		}
			effectiveConsumption=consumption;
			disposableIncomewhenConsuming=disposableIncome;
			if(disposableIncomewhenConsuming>effectiveConsumption){
				saving=disposableIncomewhenConsuming-effectiveConsumption;
			}
			else{
				saving=0;
			}

//use disposable income to consume
			if(disposableIncome>=consumption){
				residualResourcesAfterConsuming=disposableIncome-consumption;
				consumption=0;
				if(unpaidAmountInBankAccounts>0){
					double multiplier=1-residualResourcesAfterConsuming/unpaidAmountInBankAccounts;
					if(multiplier<0){
						multiplier=0;
						residualResourcesAfterConsuming+=-unpaidAmountInBankAccounts;
						unpaidAmountInBankAccounts=0;
					}
					else{
						unpaidAmountInBankAccounts+=-residualResourcesAfterConsuming;
						residualResourcesAfterConsuming=0;
					}
					for(int i=0;i<bankAccountsList.size();i++){
						aBankAccount=(BankAccount)bankAccountsList.get(i);
						aBankAccount.setAccount(aBankAccount.getAccount()+aBankAccount.getUnpaidAmount()*(1-multiplier));
						aBankAccount.setUnpaidAmount(aBankAccount.getUnpaidAmount()*multiplier);
					}
				}
		if(Context.verboseFlag){
				System.out.println("      updating worstBankAccount by "+residualResourcesAfterConsuming);
		}
				worstBankAccount.setAccount(worstBankAccount.getAccount()+residualResourcesAfterConsuming);
				residualResourcesAfterConsuming=0;
	
			}
			else{
				consumption+=-disposableIncome;
				//use positive bank accounts to consume
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					double tmpAccount=aBankAccount.getAccount();
					if(tmpAccount>=0){
						if(tmpAccount<consumption){
							consumption+=-tmpAccount;
							aBankAccount.setAccount(0);
							if(Context.verboseFlag){
								System.out.println("      deposits in this account are lower than residual desired consumption. All deposits are withdrawn to buy consumption goods and the residual desired consumption is "+consumption);      
							}
						}
						else{
							if(Context.verboseFlag){
								System.out.println("      deposits in this account are higher than residual desired consumption. Deposits are thus reduced by "+consumption);      
							}
							aBankAccount.setAccount(tmpAccount-consumption);
							consumption=0;
						}
					}
				}


				//use allowed credit to consume

				if(consumption>0){
					if(bankAccountsListWithNoUnpaidAmount.size()>0){
						//					System.out.println("      residual desired consumption "+consumption+" covered with new debt "+(bestBankAccount.getAllowedCredit())+"    "+(-bestBankAccount.getAccount()));
						double newCreditAvailableInBestBankAccount=bestBankAccount.getAllowedCredit()-bestBankAccount.getAccount();
		if(Context.verboseFlag){
						System.out.println("       new credit available in bestBankAccount "+newCreditAvailableInBestBankAccount);
		}
						if(newCreditAvailableInBestBankAccount<0){
		if(Context.verboseFlag){
							System.out.println("       residual desired consumption "+consumption+" covered with new debt "+newCreditAvailableInBestBankAccount);
		}
							if(consumption<(-newCreditAvailableInBestBankAccount)){
								bestBankAccount.setAccount(bestBankAccount.getAccount()-consumption);
							}
							else{
								bestBankAccount.setAccount(bestBankAccount.getAllowedCredit());
							}
						}
						//Now, the consumer can have residual additional credit in the bestBankAccount (if rationed on the goods market). On the other hand s/he can have unpaid amounts in some bankAccounts. If unpaid credit exists, these available funds are used to decrease unpaid amount.
						double additionalCreditAvailableInBestBankAccount=0;
						if(bankAccountsListWithNoUnpaidAmount.size()>0){
							additionalCreditAvailableInBestBankAccount=bestBankAccount.getAccount()-bestBankAccount.getAllowedCredit();
						}
		if(Context.verboseFlag){
						System.out.println("      additional credit available in bestBankAccount "+additionalCreditAvailableInBestBankAccount+" available to reduce unpaid amount if exists");
		}
						if(additionalCreditAvailableInBestBankAccount>0 && unpaidAmountInBankAccounts>0){
							System.out.println("      reducing unpaid amount");
							double multiplier=1-additionalCreditAvailableInBestBankAccount/unpaidAmountInBankAccounts;
							if(multiplier<0){
								multiplier=0;
							}
							for(int i=0;i<bankAccountsList.size();i++){
								aBankAccount=(BankAccount)bankAccountsList.get(i);
								aBankAccount.setAccount(aBankAccount.getAccount()+aBankAccount.getUnpaidAmount()*(1-multiplier));
								aBankAccount.setUnpaidAmount(aBankAccount.getUnpaidAmount()*multiplier);
							}
						}
						else{
		if(Context.verboseFlag){
							System.out.println("      no unpaid amount to be reduced");
		}
						}




					}
					else{
						System.out.println("      residual desired consumption "+consumption+" but no bestBankAccountExists ");
					}
				}
				
			}


			if(Context.saveMicroData){
				try{
					for(int i=0;i<bankAccountsList.size();i++){
						aBankAccount=(BankAccount)bankAccountsList.get(i);
						OfficeForStatistics.microDataWriterForConsumersBankAccounts06.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
					}
					OfficeForStatistics.microDataWriterForConsumersBankAccounts06.append("\n");
					OfficeForStatistics.microDataWriterForConsumersBankAccounts06.flush();
				}
				catch(IOException e) {System.out.println("IOException");}
			}


		}

public void saveDataToFile(){
			try{
//				microDataWriterForConsumers.append("t;id;ec;di2\n");
				OfficeForStatistics.microDataWriterForConsumers.append(""+RepastEssentials.GetTickCount()+";"+identity+";"+age+";"+isStudent+";"+isWorking+";"+numberOfSuccessfulPeriodsOfEducation+";"+numberOfFailedPeriodsOfEducation+";"+degree+";"+productivity+";"+wage+";"+disposableIncomeWhenDecidingDesiredConsumption+";"+disposableIncomewhenConsuming+";"+desiredDemand+";"+effectiveConsumption+"\n");
				OfficeForStatistics.microDataWriterForConsumers.flush();
			}
			catch(IOException e) {System.out.println("IOException");}

}


		public void adjustConsumtionToMatchDemandAndSupply(int rankPosition, double multiplier){
			aProductDemand=demandsList.get(rankPosition);
			aProductDemand.adjustDemand(identity,multiplier);
		}	

		public void sendInitialJobApplication(){
			if(isStudent){
				if(Context.verboseFlag){
					System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" I am not sending CV because I am a student");
				}
			} 
			else{
				if(RandomHelper.nextDouble()<Context.probabilityToBeUnemployedAtTheBeginning){
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
					if(isRetired){
						if(Context.verboseFlag){
							System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" I am not sending CV because I retired");
						}
					}
				}
				else{
					if(Context.verboseFlag){
						System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" promozioni "+numberOfSuccessfulPeriodsOfEducation+" titolo "+degree+" produttivita "+productivity+" abilityStud "+abilityStudent);
					}


					if(isRetired){
						if(Context.verboseFlag){
							System.out.println("     Consumer "+identity+" isStudent "+isStudent+" isWorking "+isWorking+" I am not sending CV because I retired");
						}
					}
					else{
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
			isRetired=true;
			if(Context.verboseFlag){
				System.out.println("       Consumer "+identity+" isWorking "+isWorking+" degree "+degree+" productivity "+productivity+" my employer firm "+myEmployer.getID()+" retire me age "+age);
			}
			myEmployer=null;
		}

		public void receiveFiredNew(){
			isWorking=false;
			if(Context.verboseFlag){
				System.out.println("       Consumer "+identity+" isWorking "+isWorking+" degree "+degree+" productivity "+productivity+" potential production "+productivity*Context.parameterOfProductivityInProductionFuncion+" my employer firm "+myEmployer.getID()+" fired me ");
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
			if(Context.verboseFlag){
				System.out.println("     Consumer "+identity+" wealth "+wealth);
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
		public boolean getIsRetiredFlag(){
			return isRetired;
		}
		public boolean getIsStudentFlag(){
			return isStudent;
		}
		public double getWage(){
			return wage;
		}
		public ArrayList<BankAccount> getBankAccountsList(){
			return bankAccountsList;
		}
		public void changeBankAccountsOwner(){
			for(int i=0;i<bankAccountsList.size();i++){
				aBankAccount=(BankAccount)bankAccountsList.get(i);
				aBankAccount.setOwner(this);
			}
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
