package sfcabm;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;

import repast.simphony.random.RandomHelper;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.essentials.RepastEssentials;

//import java.util.ArrayList;
//import sfcabm.LaborMkt;
import sfcabm.Curriculum;
import sfcabm.LaborOffer;
import sfcabm.LaborMarket;
import sfcabm.Context;
import sfcabm.BankAccount;

public class Firm {
	long production;
	int FirmID,identity,age,vacancySum,totalJobs,numVacancy,numJobs,productAbsoluteRank,demand,desiredDemand,numberOfEmployees,workersPotentialProduction,capitalPotentialProduction;
	double reservationWageFirmSum,firmWageSum;
	double sumOfWorkersProductivity=0;
	double productionCapacityAfterWorkforceAdjustment;
	public double desiredProductionCapital,productionCapital,debt,equity,sumOfBankAccounts,productionCapitalBeforeDepreciation,productionCapitalAfterDepreciation;
	double cashOnHand,depreciationOfUsedCapital,depreciationOfUnusedCapital,capitalDepreciation,financialResourcesInBankAccounts,creditToAskInSetDesiredCredit,cashOnHandWhenComputingEconomicResult,cashOnHandAfterRefundingBank;
	double unpaidAmountInBankAccounts=0;	
	double firmInvestment=0;
	double ordersOfProductsForInvestmentPurpose=0;
	double previousPeriodOrdersOfProductsForInvestmentPurpose=0;
	double productionPlusOrdersForInvestments=0;
	boolean hadFired=false;
		/*
	   public double desiredOutput;
	   public double expectedSales;
	   public double potentialOutput;
	   public int ActualCapital;
	   public double sigma=0.3;
	   */

	double senderProductivity;
	int senderID;
	double senderFirmReservationWage;


	public double initialOutput;
	public double initialCapitalStock;
	public double firmTech=1;
	public double averageAbilityFirm=0.8;

	Industry myIndustry;
	


	repast.simphony.context.Context<Object> myContext;

	ArrayList<Curriculum> applicationList = new ArrayList<Curriculum>();
	ArrayList<Consumer> workersList = new ArrayList<Consumer>();
	ArrayList<BankAccount> bankAccountsList = new ArrayList<BankAccount>();
	ArrayList<BankAccount> bankAccountsListWithNoUnpaidAmount = new ArrayList<BankAccount>();

	Curriculum aCurriculum;
	Consumer aConsumer;
	IndexedIterable<Object> consumersList,banksList;
	Firm aFirm;	
	LaborMarket myLaborMarket;
	LaborOffer myOffer;

	int[] numberOfWokersInADegree;
	double[] totalProductivityOfWorkersInADegree;
	double[] averageProductivityOfWorkersInADegree;

	Iterator<Consumer> workersListIterator;
	Iterator<Curriculum> applicationListIterator;
	 BankAccount aBankAccount,bestBankAccount,worstBankAccount;
	 Bank aBank;

	public Firm(int FirmID) {
		super();
		identity = FirmID;
		age=0;
	}

	public Firm(int FirmID,repast.simphony.context.Context<Object> con) {
		super();
		identity = FirmID;
		myContext=con;
		age=0;
		productAbsoluteRank=1;
//		productAbsoluteRank=RandomHelper.nextIntFromTo(1,10);
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" created with productAbsoluteRank "+productAbsoluteRank);
		}
	}

	public int getID(){
		return identity;
	}
	public int getIdentity(){
		return identity;
	}
	public void printID(){
		System.out.println("I am firm "+identity);
	}

	public void receiveCurriculum(Curriculum aCV){
		applicationList.add(aCV);
		if(Context.verboseFlag){
			System.out.println("       Firm "+identity+" received CV from consumer "+aCV.getSenderID()+" degree "+aCV.getSenderDegree());
		}
	}

	public void setInitialWorkers(){
		sumOfWorkersProductivity=0;
		if(applicationList.size()>0){
			/*
			   try{
			   consumersList=myContext.getObjects(Class.forName("sfcabm.Consumer"));
			   }
			   catch(ClassNotFoundException e){
			   System.out.println("Class not found");
			   }
			   */
			for(int i=0;i<applicationList.size();i++){
				aCurriculum=(Curriculum)applicationList.get(i);
				aConsumer=aCurriculum.getSender();
				workersList.add(aConsumer);
				aConsumer.jobObtained(this);
				sumOfWorkersProductivity=sumOfWorkersProductivity+aConsumer.getProductivity();
			}
			/*
			   for(int i=0;i<applicationList.size();i++){
			   aCurriculum=(Curriculum)applicationList.get(i);
			   int curriculumSenderID=aCurriculum.getSenderID();
			   for(int j=0;j<consumersList.size();j++){
			   aConsumer=(Consumer)consumersList.get(j);
			   if(aConsumer.getIdentity()==curriculumSenderID){
			   workersList.add(aConsumer);
			   aConsumer.jobObtained(this);
			   sumOfWorkersProductivity=sumOfWorkersProductivity+aConsumer.getProductivity();
			   }
			   }
			   }
			   */
		}
		else{
			if(Context.verboseFlag){
				System.out.println("  Firm "+identity+" NO WORKERS");
			}
		}
		production=Math.round(sumOfWorkersProductivity*Context.parameterOfProductivityInProductionFuncion);
		demand=(int)production;
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" sum of productivity "+sumOfWorkersProductivity+ " production "+production);
		}
	}

	public void setupBankAccountInInitialization(){
		productionCapital=production;//workersList.size()*Context.parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion;
		equity=productionCapital*RandomHelper.nextDoubleFromTo(Context.minFirmInitialEquityRatio,Context.minFirmInitialEquityRatio);
		debt=productionCapital-equity;
		if(Context.verboseFlag){
		System.out.println("     Firm "+identity+" production capital "+productionCapital+" debt "+debt+" equity "+equity);
		}
		try{
			banksList=myContext.getObjects(Class.forName("sfcabm.Bank"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		ArrayList<Integer> banksPositions=new ArrayList<Integer>();
		int numberOfBanksToBeCustomerOf=Math.min(Context.numberOfBanksAFirmCanBeCustumerOf,banksList.size());
		for(int i=0;i<banksList.size();i++){
			banksPositions.add(new Integer(i));
		}
		for(int i=0;i<numberOfBanksToBeCustomerOf;i++){
			int position=banksPositions.remove(RandomHelper.nextIntFromTo(0,(banksPositions.size()-1)));
			aBank=(Bank)banksList.get(position);
			if(Context.verboseFlag){
				System.out.println("       open account");
			}
			aBankAccount=new BankAccount(-debt/numberOfBanksToBeCustomerOf,this,aBank);
			bankAccountsList.add(aBankAccount);
			aBank.addAccount(aBankAccount);

		}


	}

	public void setupBankAccount(){
//		productionCapital=Context.parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion;
		productionCapital=Context.productionOfNewEnteringFirm;
		firmInvestment=productionCapital;
		desiredProductionCapital=productionCapital;
//		desiredDemand=Context.parameterOfProductivityInProductionFuncion;
		desiredDemand=(int)productionCapital;
		equity=productionCapital*RandomHelper.nextDoubleFromTo(0.1,0.3);
		debt=productionCapital-equity;
		if(Context.verboseFlag){
		System.out.println("     Firm "+identity+" production capital "+productionCapital+" debt "+debt+" equity "+equity);
		}
		try{
			banksList=myContext.getObjects(Class.forName("sfcabm.Bank"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		ArrayList<Integer> banksPositions=new ArrayList<Integer>();
		int numberOfBanksToBeCustomerOf=Math.min(Context.numberOfBanksAFirmCanBeCustumerOf,banksList.size());
		for(int i=0;i<banksList.size();i++){
			banksPositions.add(new Integer(i));
		}
		for(int i=0;i<numberOfBanksToBeCustomerOf;i++){
			int position=banksPositions.remove(RandomHelper.nextIntFromTo(0,(banksPositions.size()-1)));
			aBank=(Bank)banksList.get(position);
			if(Context.verboseFlag){
				System.out.println("       open account");
			}
			aBankAccount=new BankAccount(-debt,this,aBank);
			aBankAccount.setAccount(-debt);
			aBankAccount.setAllowedCredit(-debt);
			bankAccountsList.add(aBankAccount);
			aBank.addAccount(aBankAccount);

		}


	}


	public void makeProduction(){
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" production capital "+productionCapital+" numberOfWorkers "+workersList.size());
		}
			numberOfEmployees=workersList.size();
			sumOfWorkersProductivity=0;
			for(int i=0;i<workersList.size();i++){
				aConsumer=workersList.get(i);
				sumOfWorkersProductivity+=aConsumer.getProductivity();
			}

		workersPotentialProduction=(int)Math.round(sumOfWorkersProductivity*Context.parameterOfProductivityInProductionFuncion);
//		int capitalPotentialProduction=(int)Math.round(productionCapital/Context.parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion*Context.parameterOfProductivityInProductionFuncion);
		capitalPotentialProduction=(int)productionCapital;
		productionPlusOrdersForInvestments=Math.min(workersPotentialProduction,capitalPotentialProduction);
		production=(int)(productionPlusOrdersForInvestments-ordersOfProductsForInvestmentPurpose);
		age++;
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" workers Potential Production "+workersPotentialProduction+" capital potential Production "+capitalPotentialProduction+" production plus Investments "+productionPlusOrdersForInvestments+" production for howseholds "+production);
		}
	

}

	public void setDesiredCredit(){
//		double desiredProductionCapitalMultiplier=desiredDemand/production;
//		double desiredProductionCapitalMultiplier=(desiredDemand+ordersOfProductsForInvestmentPurpose)/production;
//		System.out.println("     desiredProductionCapitalMultiplier "+desiredProductionCapitalMultiplier+" production "+production);
		/*
		if(desiredProductionCapitalMultiplier>1.1){
			desiredProductionCapitalMultiplier=1.1;
		}
		if(desiredProductionCapitalMultiplier<0.9){
			desiredProductionCapitalMultiplier=0.9;
		}
		*/
//		desiredProductionCapital=Math.round((desiredProductionCapitalMultiplier*(productionCapital+capitalDepreciation))+ordersOfProductsForInvestmentPurpose);
//		desiredProductionCapital=Math.round((desiredProductionCapitalMultiplier*(productionCapital+capitalDepreciation)));


		if(Context.saveMicroData){
			try{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					OfficeForStatistics.microDataWriterForFirmsBankAccounts03.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForFirmsBankAccounts03.append("\n");
				OfficeForStatistics.microDataWriterForFirmsBankAccounts03.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}


		desiredProductionCapital=desiredDemand+ordersOfProductsForInvestmentPurpose;
//compute financial resources and unpaid amounts in bank accounts
		financialResourcesInBankAccounts=0;
		unpaidAmountInBankAccounts=0;
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			unpaidAmountInBankAccounts=unpaidAmountInBankAccounts+aBankAccount.getUnpaidAmount();
			if(aBankAccount.getAccount()>0){
				financialResourcesInBankAccounts+=aBankAccount.getAccount();
				aBankAccount.setAccount(0);
			}
		}
//identify the bank account where resources are deposited and accounts with no unpaid amount
		int positionOfWorstBankAccount=0;
		aBankAccount=(BankAccount)bankAccountsList.get(0);
		double worstAccount=aBankAccount.getAccount();
		bankAccountsListWithNoUnpaidAmount = new ArrayList<BankAccount>();
		if(aBankAccount.getUnpaidAmount()>0){
		}
		else{
			bankAccountsListWithNoUnpaidAmount.add(aBankAccount);
		}

		for(int j=1;j<bankAccountsList.size();j++){
			aBankAccount=(BankAccount)bankAccountsList.get(j);
			if(aBankAccount.getAccount()<worstAccount){
				worstAccount=aBankAccount.getAccount();
				positionOfWorstBankAccount=j;
			}
			if(aBankAccount.getUnpaidAmount()>0){
			}
			else{
				bankAccountsListWithNoUnpaidAmount.add(aBankAccount);
			}
		}
		worstBankAccount=(BankAccount)bankAccountsList.get(positionOfWorstBankAccount);

//identify the bank account where asking new credit (the one with best position among them with no unpaid amount)
		
		if(bankAccountsListWithNoUnpaidAmount.size()>0){
			int positionOfBestBankAccount=0;
			aBankAccount=(BankAccount)bankAccountsListWithNoUnpaidAmount.get(0);
			double bestAccount=aBankAccount.getAccount();
			for(int j=1;j<bankAccountsListWithNoUnpaidAmount.size();j++){
				aBankAccount=(BankAccount)bankAccountsListWithNoUnpaidAmount.get(j);
				if(aBankAccount.getAccount()>bestAccount){
					bestAccount=aBankAccount.getAccount();
					positionOfBestBankAccount=j;
				}
			}
			bestBankAccount=(BankAccount)bankAccountsList.get(positionOfBestBankAccount);
		}
		else{
			bestBankAccount=null;
		}

		System.out.println("     Firm "+identity+" n of banks with no unpaid amount "+bankAccountsListWithNoUnpaidAmount.size());

//computation and requests of new credit
		double creditToAsk;
		if(desiredProductionCapital>productionCapital){
			creditToAsk=desiredProductionCapital-productionCapital-cashOnHand-financialResourcesInBankAccounts+unpaidAmountInBankAccounts;
			creditToAskInSetDesiredCredit=creditToAsk;

			if(creditToAsk>0){
				if(bankAccountsListWithNoUnpaidAmount.size()>0){
					bestBankAccount.setDesiredCredit(0.0,creditToAsk);
				}
			}
			else{
				creditToAsk=0;
				creditToAskInSetDesiredCredit=creditToAsk;
			}
		}
		else{
			if(cashOnHand+financialResourcesInBankAccounts<=0){
				creditToAsk=-(cashOnHand+financialResourcesInBankAccounts)+unpaidAmountInBankAccounts;
				creditToAskInSetDesiredCredit=creditToAsk;
				if(bankAccountsListWithNoUnpaidAmount.size()>0){
					bestBankAccount.setDesiredCredit(0.0,creditToAsk);
				}
			}
			else{
				creditToAsk=0;
				creditToAskInSetDesiredCredit=creditToAsk;
			}
		}
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" depreciated production Capital "+productionCapital+" demand from household +firms "+(demand+ordersOfProductsForInvestmentPurpose)+" desiredDemand from household + firms "+(desiredDemand+ordersOfProductsForInvestmentPurpose)+" desiredProductionCapital "+desiredProductionCapital+" cashOnHand "+cashOnHand+" financialResourcesInBankAccounts "+financialResourcesInBankAccounts+" asked credit "+creditToAsk);
System.out.println("      ----------------");
		}



		if(Context.saveMicroData){
			try{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					OfficeForStatistics.microDataWriterForFirmsBankAccounts04.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForFirmsBankAccounts04.append("\n");
				OfficeForStatistics.microDataWriterForFirmsBankAccounts04.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}



	}

	public void adjustProductionCapitalAndBankAccount(){

	if(Context.saveMicroData){
			try{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					OfficeForStatistics.microDataWriterForFirmsBankAccounts05.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForFirmsBankAccounts05.append("\n");
				OfficeForStatistics.microDataWriterForFirmsBankAccounts05.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}



		double creditToAsk;
		if(desiredProductionCapital>productionCapital){
				System.out.println("     increasing capital");
			creditToAsk=desiredProductionCapital-productionCapital-cashOnHand-financialResourcesInBankAccounts+unpaidAmountInBankAccounts;

			if(creditToAsk>0){
				System.out.println("     asked credit");
				double amountAvailableFromBestBank=0;
				if(bankAccountsListWithNoUnpaidAmount.size()>0){
					amountAvailableFromBestBank=-(bestBankAccount.getAllowedCredit()-bestBankAccount.getAccount());
				}
				if(unpaidAmountInBankAccounts<amountAvailableFromBestBank){
					for(int i=0;i<bankAccountsList.size();i++){
						aBankAccount=(BankAccount)bankAccountsList.get(i);
						aBankAccount.setUnpaidAmount(0);
					}
					amountAvailableFromBestBank+=-unpaidAmountInBankAccounts;
					if(cashOnHand<0){
						if(amountAvailableFromBestBank>(-cashOnHand)){
							amountAvailableFromBestBank+=cashOnHand;
							firmInvestment=amountAvailableFromBestBank;
						}
						else{
							cashOnHand+=amountAvailableFromBestBank;
							amountAvailableFromBestBank=0;
							firmInvestment=amountAvailableFromBestBank;
						}
							productionCapital+=firmInvestment;
							bestBankAccount.setAccount(bestBankAccount.getAllowedCredit());
					}
				}
				else{
					double multiplier=1-(amountAvailableFromBestBank/unpaidAmountInBankAccounts);
					for(int i=0;i<bankAccountsList.size();i++){
						aBankAccount=(BankAccount)bankAccountsList.get(i);
						aBankAccount.setUnpaidAmount(aBankAccount.getUnpaidAmount()*multiplier);
					}
					bestBankAccount.setAccount(bestBankAccount.getAllowedCredit());
				}


			/*

				if(bestBankAccount.getAllowedCredit()<=bestBankAccount.getDemandedCredit()){
					double tmpProductionCapital=productionCapital+cashOnHand+financialResourcesInBankAccounts-aBankAccount.getAllowedCredit()+aBankAccount.getAccount();
					if(tmpProductionCapital>=desiredProductionCapital){
						System.out.println("      asked credit; totally allowed    tmpPC "+tmpProductionCapital+" desiredPC "+desiredProductionCapital);
						firmInvestment=desiredProductionCapital-productionCapital;
						productionCapital=desiredProductionCapital;
						bestBankAccount.setAccount(aBankAccount.getDemandedCredit());
					}
					else{
						System.out.println("      asked credit; partially allowed    tmpPC "+tmpProductionCapital+" desiredPC "+desiredProductionCapital);
						aBankAccount.setAccount(aBankAccount.getAllowedCredit());
						firmInvestment=tmpProductionCapital-productionCapital;
						productionCapital=tmpProductionCapital;
					}
				}
				//if refund is asked (however, this is performed in one of the previous step and excluded here: the program will never enter in this if) 
				else{
						System.out.println("      asked credit; refund asked");
					if(aBankAccount.getAccount()+cashOnHand+financialResourcesInBankAccounts>aBankAccount.getAllowedCredit()){
						firmInvestment=aBankAccount.getAccount()+aBankAccount.getAllowedCredit()+cashOnHand+financialResourcesInBankAccounts;
						productionCapital+=(aBankAccount.getAccount()+aBankAccount.getAllowedCredit()+cashOnHand+financialResourcesInBankAccounts);
						aBankAccount.setAccount(aBankAccount.getAllowedCredit());
					}
					else{
						System.out.println("      Firm cannot refund");
						aBankAccount.increaseUnpaidAmount(-aBankAccount.getAccount()+aBankAccount.getAllowedCredit());
						firmInvestment=0;
					}
				}

		*/
			}
			else{
						System.out.println("      available funds are enough to finance new investment and repay unpaid credit; the excess is deposited");
				firmInvestment=desiredProductionCapital-productionCapital;
				productionCapital=desiredProductionCapital;
				worstBankAccount.setAccount(aBankAccount.getAccount()-creditToAsk);
				creditToAsk=0;
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					if(aBankAccount.getUnpaidAmount()>0){
						aBankAccount.setAccount(aBankAccount.getAccount()+aBankAccount.getUnpaidAmount());
						aBankAccount.setUnpaidAmount(0);
					}
				}
			}
			
		}
		else{
				System.out.println("     decreasing capital");
			   firmInvestment=desiredProductionCapital-productionCapital;
//			   productionCapital=desiredProductionCapital;
			creditToAsk=-cashOnHand-financialResourcesInBankAccounts+unpaidAmountInBankAccounts;

			if(creditToAsk>0){
				System.out.println("     credit asked "+creditToAsk);
				double amountAvailableFromBestBank=0;
				if(bankAccountsListWithNoUnpaidAmount.size()>0){
					amountAvailableFromBestBank=-(bestBankAccount.getAllowedCredit()-bestBankAccount.getAccount());
				}
				if(unpaidAmountInBankAccounts<amountAvailableFromBestBank){
					for(int i=0;i<bankAccountsList.size();i++){
						aBankAccount=(BankAccount)bankAccountsList.get(i);
						if(aBankAccount.getUnpaidAmount()>0){
							aBankAccount.setAccount(aBankAccount.getAccount()+aBankAccount.getUnpaidAmount());
							aBankAccount.setUnpaidAmount(0);
						}
					}
					amountAvailableFromBestBank+=-unpaidAmountInBankAccounts;
					if(cashOnHand<0){
						if(amountAvailableFromBestBank>(-cashOnHand)){
							amountAvailableFromBestBank+=cashOnHand;
							cashOnHand=0;
						}
						else{
							cashOnHand+=amountAvailableFromBestBank;
							amountAvailableFromBestBank=0;
						}
							bestBankAccount.setAccount(bestBankAccount.getAllowedCredit());
					}
				}
				else{
					double multiplier=1-(amountAvailableFromBestBank/unpaidAmountInBankAccounts);
					for(int i=0;i<bankAccountsList.size();i++){
						aBankAccount=(BankAccount)bankAccountsList.get(i);
						if(aBankAccount.getUnpaidAmount()>0){
							aBankAccount.setAccount(aBankAccount.getAccount()+aBankAccount.getUnpaidAmount()*(1-multiplier));
							aBankAccount.setUnpaidAmount(aBankAccount.getUnpaidAmount()*multiplier);
						}
					}
					if(bankAccountsListWithNoUnpaidAmount.size()>0){
						bestBankAccount.setAccount(bestBankAccount.getAllowedCredit());
					}
				}
			}
			else{
				System.out.println("      credit not asked "+creditToAsk);
				firmInvestment=desiredProductionCapital-productionCapital;
//				productionCapital=desiredProductionCapital;
				worstBankAccount.setAccount(aBankAccount.getAccount()-creditToAsk);
				creditToAsk=0;
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					if(aBankAccount.getUnpaidAmount()>0){
						aBankAccount.setAccount(aBankAccount.getAccount()+aBankAccount.getUnpaidAmount());
						aBankAccount.setUnpaidAmount(0);
					}
				}creditToAsk=0;
			}


			/*
			   firmInvestment=desiredProductionCapital-productionCapital;
			   productionCapital=desiredProductionCapital;
			   if(cashOnHand+financialResourcesInBankAccounts<0){
			   if(aBankAccount.getDemandedCredit()<aBankAccount.getAllowedCredit()){
			   System.out.println("      capital reduction; refund asked but not enough internal funds");
			   System.out.println("      Firm cannot refund");
			   aBankAccount.increaseUnpaidAmount(-aBankAccount.getAccount()+aBankAccount.getAllowedCredit());
			   }
			   else{
			   aBankAccount.setAccount(aBankAccount.getDemandedCredit());
			   System.out.println("      capital reduction; allowed credit is enough to cover negative internal funds");
			   }
			   }
			   else{
			   aBankAccount.setAccount(aBankAccount.getAccount()+cashOnHand+financialResourcesInBankAccounts);
			   System.out.println("      capital reduction; positive internal funds that are deposited");
			   creditToAsk=0;
			   }
			   */
		}

		cashOnHandAfterRefundingBank=cashOnHand;

		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" investment "+firmInvestment+" production Capital "+productionCapital+" desiredProductionCapital "+desiredProductionCapital);
			System.out.println("     -----------");
		}

	if(Context.saveMicroData){
			try{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					OfficeForStatistics.microDataWriterForFirmsBankAccounts06.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForFirmsBankAccounts06.append("\n");
				OfficeForStatistics.microDataWriterForFirmsBankAccounts06.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}





	}



	public void laborForceDownwardAdjustment(){
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" downward labor force adjustment ");
		}
		productionCapacityAfterWorkforceAdjustment=0;
		workersListIterator=workersList.iterator();
		while(workersListIterator.hasNext()){
			aConsumer=workersListIterator.next();
			if(aConsumer.getAge()<Context.consumerExitAge){
				productionCapacityAfterWorkforceAdjustment=productionCapacityAfterWorkforceAdjustment+aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
			}
			else{
				aConsumer.receiveRetirementNew();
				workersListIterator.remove();
			}
		}
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" desired Demand "+desiredDemand+" orders of products for investments "+ordersOfProductsForInvestmentPurpose+" desired demand + invest goods "+(desiredDemand+ordersOfProductsForInvestmentPurpose)+" productionCapacityAfterRetirements "+productionCapacityAfterWorkforceAdjustment);
		}
		//		if((productionCapacityAfterWorkersRetire-demand)>(OfficeForStatistics.averageProductivity*Context.parameterOfProductivityInProductionFuncion)){
		hadFired=false;
		if(productionCapacityAfterWorkforceAdjustment-(desiredDemand+ordersOfProductsForInvestmentPurpose)>0){
			while(productionCapacityAfterWorkforceAdjustment>(desiredDemand+ordersOfProductsForInvestmentPurpose)){
				int latestWorkerPosition=workersList.size()-1;
				aConsumer=workersList.get(latestWorkerPosition);
				productionCapacityAfterWorkforceAdjustment+=-aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
				if(productionCapacityAfterWorkforceAdjustment-(desiredDemand+ordersOfProductsForInvestmentPurpose)>0){
					aConsumer.receiveFiredNew();
					workersList.remove(latestWorkerPosition);
					hadFired=true;
				}
			}
			productionCapacityAfterWorkforceAdjustment+=aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
			System.out.println("     Firm "+identity+" desired Demand "+desiredDemand+" orders of products for investments "+ordersOfProductsForInvestmentPurpose+" desired demand + invest goods "+(desiredDemand+ordersOfProductsForInvestmentPurpose)+" productionCapacityAfterFiring "+productionCapacityAfterWorkforceAdjustment);

		}
		else{
			System.out.println("     Firm "+identity+" desired Demand "+desiredDemand+" orders of products for investments "+ordersOfProductsForInvestmentPurpose+" desired demand + invest goods "+(desiredDemand+ordersOfProductsForInvestmentPurpose)+" productionCapacityAfterFiring "+productionCapacityAfterWorkforceAdjustment);
		}
		}

	public void laborForceUpwardAdjustment(){
		switch(Context.firmsWorkersMatching){
			case 0:
				hireUsingReceivedCV();
				break;
			case 1:
				hireUsingReceivedCV();
				sendVacancies();
				break;
			case 2:
				sendVacancies();
				break;
			default: System.out.println("Unknown workers firms matching mechanism");
				 break;
		}
	}

	public void hireUsingReceivedCV(){
		applicationListIterator=applicationList.iterator();
		if(Context.verboseFlag){
			System.out.println("     firm "+identity+" application list size "+applicationList.size()+" productionCapacity "+productionCapacityAfterWorkforceAdjustment+" desired demand + investment "+(desiredDemand+ordersOfProductsForInvestmentPurpose));
		}
		if(hadFired){
			System.out.println("     I am not hiring because I have just fired");
		}
		else{
			while(productionCapacityAfterWorkforceAdjustment<(desiredDemand+ordersOfProductsForInvestmentPurpose) && applicationListIterator.hasNext()){
				aCurriculum=applicationListIterator.next();
				aConsumer=aCurriculum.getSender();
				if(aConsumer.getIsWorkingFlag()){
					applicationListIterator.remove();
					if(Context.verboseFlag){
						System.out.println("     the sender was hired by another firm");
					}
				}
				else{
					aConsumer.receiveHiredNew(this);
					productionCapacityAfterWorkforceAdjustment=productionCapacityAfterWorkforceAdjustment+aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
					workersList.add(aConsumer);
					applicationListIterator.remove();
				}
				if(Context.verboseFlag){
					System.out.println("     firm "+identity+" application list size "+applicationList.size()+" productionCapacityAfterHiring "+productionCapacityAfterWorkforceAdjustment+" desired demand + investment "+(desiredDemand+ordersOfProductsForInvestmentPurpose));
				}
			}
		}

	}

	public void hire(Consumer newWorker){
		workersList.add(newWorker);
		newWorker.receiveHiredNew(this);
	}

	public void computeEconomicResultAndCapitalDepreciation(){
		firmWageSum=0;
		cashOnHand=0;
		for(int i =0; i<workersList.size();i++){
			aConsumer=(Consumer)workersList.get(i);
			firmWageSum+=aConsumer.getWage();
		}
		previousPeriodOrdersOfProductsForInvestmentPurpose=ordersOfProductsForInvestmentPurpose;
		cashOnHand=demand+ordersOfProductsForInvestmentPurpose-firmWageSum;
		cashOnHandWhenComputingEconomicResult=cashOnHand;
		//		capitalDepreciation=productionCapital*Context.percentageOfCapitalDepreciation;
		depreciationOfUsedCapital=demand*Context.percentageOfUsedCapitalDepreciation;
		depreciationOfUnusedCapital=(productionCapital-demand)*Context.percentageOfUnusedCapitalDepreciation;
		capitalDepreciation=depreciationOfUsedCapital+depreciationOfUnusedCapital;
		if(Context.verboseFlag){
			System.out.print("     firm "+identity+" demand "+(demand+ordersOfProductsForInvestmentPurpose)+" payed wages "+firmWageSum+" cashOnHand "+cashOnHand+" productionCapital "+productionCapital+" depreciation "+capitalDepreciation);
		}

		productionCapitalBeforeDepreciation=productionCapital;

		productionCapital+=-capitalDepreciation;

		productionCapitalAfterDepreciation=productionCapital;

		if(Context.verboseFlag){
			System.out.println(" productionCapital "+productionCapital);
		}

	}


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
					OfficeForStatistics.microDataWriterForFirmsBankAccounts01.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForFirmsBankAccounts01.append("\n");
				OfficeForStatistics.microDataWriterForFirmsBankAccounts01.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}




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
		if(totalAmountToRefund>0){
			//		if(Context.verboseFlag){
			System.out.println("     Firm "+getIdentity()+": to refund "+totalAmountToRefund+" financial resource in bank accounts "+financialResourcesInBankAccounts+" cashOnHand "+cashOnHand);
			//		}
			resourcesAvailableToRefund=financialResourcesInBankAccounts+cashOnHand;
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
							aBankAccount.setAccount(0);
						}
						else{
							aBankAccount.setAccount(amountOfThisBankAccount-totalAmountToRefund);
							totalAmountToRefund=0;
						}
					}
				}
				if(totalAmountToRefund>0){
					cashOnHand+=-totalAmountToRefund;
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
				if(cashOnHand>0){
					cashOnHand=0;
				}

				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					amountOfThisBankAccount=aBankAccount.getAccount();
					if(amountOfThisBankAccount<0){
						double toPayBakToThisBankAccount=-(aBankAccount.getAccount()-aBankAccount.getAllowedCredit());
						if(resourcesAvailableToRefund>toPayBakToThisBankAccount){
							resourcesAvailableToRefund+=-toPayBakToThisBankAccount;
							aBankAccount.setAccount(aBankAccount.getAllowedCredit());
						}
						else{
							if(resourcesAvailableToRefund>=0){	
								aBankAccount.setAccount(amountOfThisBankAccount+resourcesAvailableToRefund);
								aBankAccount.increaseUnpaidAmount(-aBankAccount.getAccount()+aBankAccount.getAllowedCredit());
								resourcesAvailableToRefund+=-toPayBakToThisBankAccount;
							}
							else{
								aBankAccount.increaseUnpaidAmount(-aBankAccount.getAccount()+aBankAccount.getAllowedCredit());
								resourcesAvailableToRefund+=-toPayBakToThisBankAccount;
							}
						}
					}
				}
			}
			//		if(Context.verboseFlag){
			System.out.println("      totalAmountToRefund "+totalAmountToRefund+" resourcesAvailableToRefund "+resourcesAvailableToRefund+" cashOnHand "+cashOnHand);
			//		}
		}


		cashOnHandAfterRefundingBank=cashOnHand;


		if(Context.saveMicroData){
			try{
				for(int i=0;i<bankAccountsList.size();i++){
					aBankAccount=(BankAccount)bankAccountsList.get(i);
					OfficeForStatistics.microDataWriterForFirmsBankAccounts02.append(""+RepastEssentials.GetTickCount()+","+identity+","+aBankAccount.getHostingBank().getIdentity()+","+aBankAccount.getAccount()+","+aBankAccount.getDemandedCredit()+","+aBankAccount.getAllowedCredit()+","+aBankAccount.getUnpaidAmount()+"|");
				}
				OfficeForStatistics.microDataWriterForFirmsBankAccounts02.append("\n");
				OfficeForStatistics.microDataWriterForFirmsBankAccounts02.flush();
			}
			catch(IOException e) {System.out.println("IOException");}
		}





	}


	//compute average productivity for each degree of education
	public void computeAverageProductivityForEachDegreeOfEducation(){
		numberOfWokersInADegree=new int[7];
		totalProductivityOfWorkersInADegree=new double[7];
		averageProductivityOfWorkersInADegree=new double[7];
		for(int i=0;i<workersList.size();i++){
			aConsumer=(Consumer)workersList.get(i);
			int degree=aConsumer.getDegree();
			numberOfWokersInADegree[degree]++;
			totalProductivityOfWorkersInADegree[degree]=totalProductivityOfWorkersInADegree[degree]+aConsumer.getProductivity();
			averageProductivityOfWorkersInADegree[degree]=totalProductivityOfWorkersInADegree[degree]/numberOfWokersInADegree[degree];
		}
		if(Context.verboseFlag){
			for(int j=0;j<7;j++){
				System.out.println("     Firm "+identity+" degree "+j+" n. of employed workers "+numberOfWokersInADegree[j]+" tot. poductivity "+totalProductivityOfWorkersInADegree[j]+" average productivity "+averageProductivityOfWorkersInADegree[j]);
			}
		}
	}

	/**
	 * This method cycles on the list of workers and sets the worker's wage according to your parametrization.
	 * <p> 
	 * According to the parametrization you have chosen for your model, the wage is proportional to the worker's productivity; the average productivity of workers employed by the firm and having the same education degree; the average productivity of workers having the same education degree in the whole economy.
	 */
	public void setWorkersWage(){
		for(int i=0;i<workersList.size();i++){
			aConsumer=workersList.get(i);
			int degree=aConsumer.getDegree();
			//double aWage;
			switch(Context.wageSettingRule){
				case 0: aConsumer.setWage(Context.unemploymentDole+Context.laborMarketStateToSetWage*Context.parameterOfProductivityInProductionFuncion*aConsumer.getProductivity()); 
					break;
				case 1: aConsumer.setWage(Context.unemploymentDole+Context.laborMarketStateToSetWage*Context.parameterOfProductivityInProductionFuncion*averageProductivityOfWorkersInADegree[degree]);
					break;
				case 2: aConsumer.setWage(Context.unemploymentDole+Context.laborMarketStateToSetWage*Context.parameterOfProductivityInProductionFuncion*OfficeForStatistics.averageProductivityOfWorkersInADegree[degree]);
					break;
				default: System.out.println("Unknown wage setting rule");
					 break;
			}
		}
	}

	// SEND LABOR DEMAND TO LABOR MARKET

	public void sendVacancies(){
		if(hadFired){
		}
		else{
			if(productionCapacityAfterWorkforceAdjustment<(desiredDemand*productionCapital/desiredProductionCapital)){
				myOffer = new LaborOffer(this,identity,(demand-productionCapacityAfterWorkforceAdjustment),senderFirmReservationWage);
				try{
					myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("sfcabm.LaborMarket"))).get(0);
				}
				catch(ClassNotFoundException e){
					System.out.println("Class not found");
				}

				myLaborMarket.receiveVacancies(myOffer);
			}
		}
	}

	/*
	   public void hire(){

	   }
	   questo viene dopo
	   */


	//INIZIALIZZARE OUTPUT LEVEL:
	//PUO SERVIRE PER QUANDO SI INTRODUCE IL CAPITALE? SERVE UNA COMBINAZIONE DI K ED L?
	/*
	   public void setInitialProduction(){
	   initialOutput=(Context.NumConsumers/Context.NumFirms)*Math.min(firmTech,averageAbilityFirm);
	   initialCapitalStock=initialOutput/Math.min(firmTech,averageAbilityFirm);
	   }

	//non so calcolare il numero dei workers di ogni firm
	//si deve inizializzare past sales: si inizia con un valore a caso e poi si lega al consumo dei worker? 
	 * 
	 * 
	 public void productionDecision(){
	 potentialOutput=(1-sigma)*ActualCapital*Math.min(sumOfWorkersProductivity,averageAbilityFirm);
	 expectedSales=???
	 desiredOutput=expectedSales;

	 if (potentialOutput<desiredOutput){
	 laborDemand++;
	 }
	 else laborDemand=0;
	 }

*/
	public void computeSumOfBankAccounts(){
		sumOfBankAccounts=0;
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			sumOfBankAccounts+=aBankAccount.getAccount();
		}
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" account "+sumOfBankAccounts);
		}

	}
	public double getSumOfBankAccounts(){
		return sumOfBankAccounts;
	}

	public void setProductAbsoluteRank(int pab){
		productAbsoluteRank=pab;
	}


	public void setDesiredDemand(double industryProduction,double industryDemand){
		demand=(int)Math.round(production/industryProduction*industryDemand);
		desiredDemand=demand;
		if(Context.verboseFlag){
			System.out.println("         Firm "+identity+" calcolo "+production+"/"+industryProduction+"*"+industryDemand);
			System.out.println("         Firm "+identity+" production for howsehold "+production+" demand from howsehold "+demand+" desired demand from howsehold "+desiredDemand+" demand from firms "+ordersOfProductsForInvestmentPurpose);
		}
	}


	public void setDemand(double industryProduction,double industryDemand){
		demand=(int)Math.round(production/industryProduction*industryDemand);
		if(Context.verboseFlag){
			System.out.println("         Firm "+identity+" production for howsehold "+production+" demand from howsehold "+demand+" desired demand from howsehold "+desiredDemand+" demand from firms "+ordersOfProductsForInvestmentPurpose);
			//				System.out.println("         Firm "+identity+" production "+production+" demand "+demand+" desired demand "+desiredDemand);
		}
	}

	public void setOrdersOfProductsForInvestmentPurpose(double industryProduction,double industryInvest){
		ordersOfProductsForInvestmentPurpose=(int)Math.round(production/industryProduction*industryInvest);
		if(Context.verboseFlag){
			System.out.println("         Firm "+identity+" production "+production+" demand from houseld "+demand+" demand from firms "+ordersOfProductsForInvestmentPurpose);
		}
	}




	public void jettisoningCurricula(){
		applicationList = new ArrayList<Curriculum>();
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" jettisoning curricula ");
		}
	}
	/*
	   public void setDesiredProductionCapital(){
	   desiredProductionCapital=workersList.size()*Context.parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion;
	   System.out.println("     Firm "+identity+" desired production capital "+desiredProductionCapital);
	   }
	   */
	public int getProductAbsoluteRank(){
		return productAbsoluteRank;
	}

	public long getProduction(){
		return production;
	}
	public int[] getnumberOfWokersInADegree(){
		return numberOfWokersInADegree;
	}
	public double[] getTotalProductivityOfWorkersInADegree(){
		return totalProductivityOfWorkersInADegree;
	}

	public int getNumberOfWorkers(){
		return workersList.size();
	}
	public double getInvestment(){
		return firmInvestment;
	}
	public void setBankAccountsShutDown(){
		for(int i=0;i<bankAccountsList.size();i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			aBankAccount.setAccountShutDown();	
		}
	}
	public void sendWorkersShutDownNew(){
		for(int i=0;i<workersList.size();i++){
			aConsumer=(Consumer)workersList.get(i);
			aConsumer.receiveFiredNew();	
		}
	}



	public double getDemand(){
		return demand;
	}
	public void setIndustry(Industry myI){
		myIndustry=myI;
	}
	public Industry getIndustry(){
		return myIndustry;
	}

	public void setProductionAndDemandIfNewEntry(){
		production=desiredDemand;
		demand=(int)production;
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" desired Demand "+desiredDemand+" demand "+demand+" production "+production+" orders of products for investments "+ordersOfProductsForInvestmentPurpose+" desired demand + invest goods "+(desiredDemand+ordersOfProductsForInvestmentPurpose));
		}
	}

	public double getOrdersOfProductsForInvestmentPurpose(){
		return ordersOfProductsForInvestmentPurpose;
	}

	public void innovate(){
		productAbsoluteRank++;
	}

	public void saveDataToFile(){
		try{
			OfficeForStatistics.microDataWriterForFirms.append(""+RepastEssentials.GetTickCount()+";"+identity+";"+age+";"+numberOfEmployees+";"+workersPotentialProduction+";"+capitalPotentialProduction+";"+desiredDemand+";"+demand+";"+previousPeriodOrdersOfProductsForInvestmentPurpose+";"+firmWageSum+";"+productionCapitalBeforeDepreciation+";"+productionCapitalAfterDepreciation+";"+cashOnHandWhenComputingEconomicResult+";"+cashOnHandAfterRefundingBank+";"+ creditToAskInSetDesiredCredit+";"+unpaidAmountInBankAccounts+";"+desiredProductionCapital+";"+productionCapital+"\n");//+";"+isStudent+";"+isWorking+";"+numberOfSuccessfulPeriodsOfEducation+";"+numberOfFailedPeriodsOfEducation+";"+degree+";"+productivity+";"+wage+";"+disposableIncomeWhenDecidingDesiredConsumption+";"+disposableIncomewhenConsuming+";"+desiredDemand+";"+effectiveConsumption+"\n");
			OfficeForStatistics.microDataWriterForFirms.flush();
		}
		catch(IOException e) {System.out.println("IOException");}

	}
	public void sellUnusedCapital(double shareOfUnusedCapitalSold){
		productionCapital+=firmInvestment*shareOfUnusedCapitalSold;
		worstBankAccount.setAccount(worstBankAccount.getAccount()-firmInvestment*shareOfUnusedCapitalSold*Context.percentageOfRealizedUnusedProductionCapital);
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" sold capital "+(-firmInvestment*shareOfUnusedCapitalSold)+" realizing "+(-firmInvestment*shareOfUnusedCapitalSold*Context.percentageOfRealizedUnusedProductionCapital));
		}
	}


	//reset the firm each time step 
	/*public void reset(){
	  numJobs=0;
	  firmWageSum=0;
	  applicationList.clear();
	  jobList.clear();
	}
	*/  
}
