package sfcabm;

import java.util.ArrayList;
import java.util.Iterator;

import repast.simphony.random.RandomHelper;
import repast.simphony.util.collections.IndexedIterable;

//import java.util.ArrayList;
//import sfcabm.LaborMkt;
import sfcabm.Curriculum;
import sfcabm.LaborOffer;
import sfcabm.LaborMarket;
import sfcabm.Context;

public class Firm {
	long production;
	int FirmID;
	int identity;
	double reservationWageFirmSum;
	int vacancySum;
	int totalJobs;
	double firmWageSum;
	int numVacancy;
	int numJobs;
	double sumOfWorkersProductivity=0;
	int productAbsoluteRank;
	int demand,desiredDemand;
	double productionCapacityAfterWorkforceAdjustment;
	public double desiredProductionCapital,productionCapital,debt,equity,sumOfBankAccounts;
	double cashOnHand,capitalDepreciation,financialResourcesInBankAccounts;	
	double firmInvestment=0;
	double ordersOfProductsForInvestmentPurpose;
	double productionPlusOrdersForInvestments=0;
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
	


	repast.simphony.context.Context<Object> myContext;

	ArrayList<Curriculum> applicationList = new ArrayList<Curriculum>();
	ArrayList<Consumer> workersList = new ArrayList<Consumer>();
	ArrayList<BankAccount> bankAccountsList = new ArrayList<BankAccount>();

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
	}

	public Firm(int FirmID,repast.simphony.context.Context<Object> con) {
		super();
		identity = FirmID;
		myContext=con;
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
		//	productAbsoluteRank=1;
		productAbsoluteRank=RandomHelper.nextIntFromTo(1,10);
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" sum of productivity "+sumOfWorkersProductivity+ " production "+production);
		}
	}

	public void setupBankAccountInInitialization(){
		productionCapital=workersList.size()*Context.parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion;
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
			bankAccountsList.add(aBankAccount);
			aBank.addAccount(aBankAccount);

		}


	}

	public void setupBankAccount(){
		productionCapital=Context.parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion;
		firmInvestment=productionCapital;
		desiredProductionCapital=productionCapital;
		desiredDemand=Context.parameterOfProductivityInProductionFuncion;
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
		System.out.println("     Firm "+identity+" production capital "+productionCapital+" numberOfWorkers "+workersList.size());
			sumOfWorkersProductivity=0;
			for(int i=0;i<workersList.size();i++){
				aConsumer=workersList.get(i);
				sumOfWorkersProductivity+=aConsumer.getProductivity();
			}

		int workersPotentialProduction=(int)Math.round(sumOfWorkersProductivity*Context.parameterOfProductivityInProductionFuncion);
		int capitalPotentialProduction=(int)Math.round(productionCapital/Context.parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion*Context.parameterOfProductivityInProductionFuncion);
		productionPlusOrdersForInvestments=Math.min(workersPotentialProduction,capitalPotentialProduction);
		production=(int)(productionPlusOrdersForInvestments-ordersOfProductsForInvestmentPurpose);
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" workers Potential Production "+workersPotentialProduction+" capital potential Production "+capitalPotentialProduction+" production plus Investments "+productionPlusOrdersForInvestments+" production "+production);
		}
	

	}

	public void setDesiredCredit(){
		desiredProductionCapital=Math.round((desiredDemand*(productionCapital+capitalDepreciation)/production)+ordersOfProductsForInvestmentPurpose);
		financialResourcesInBankAccounts=0;
		for(int i=0;i<bankAccountsList.size()-1;i++){
			aBankAccount=(BankAccount)bankAccountsList.get(i);
			if(aBankAccount.getAccount()>0){
				financialResourcesInBankAccounts+=aBankAccount.getAccount();
				aBankAccount.setAccount(0);
			}
		}
//identify the bank account with the best position
		int positionOfBestBankAccount=0;
		int positionOfWorstBankAccount=0;
		double creditToAsk;
		aBankAccount=(BankAccount)bankAccountsList.get(0);
		double bestAccount=aBankAccount.getAccount();
		double worstAccount=aBankAccount.getAccount();
		for(int j=1;j<bankAccountsList.size()-1;j++){
			aBankAccount=(BankAccount)bankAccountsList.get(j);
			if(aBankAccount.getAccount()>bestAccount){
				positionOfBestBankAccount=j;
			}
			if(aBankAccount.getAccount()<worstAccount){
				positionOfWorstBankAccount=j;
			}
		}

		bestBankAccount=(BankAccount)bankAccountsList.get(positionOfBestBankAccount);
		worstBankAccount=(BankAccount)bankAccountsList.get(positionOfWorstBankAccount);
		aBankAccount=(BankAccount)bankAccountsList.get(positionOfBestBankAccount);

		if(desiredProductionCapital>productionCapital){
			creditToAsk=desiredProductionCapital-productionCapital-cashOnHand-financialResourcesInBankAccounts;

			if(creditToAsk>0){
				bestBankAccount.setDesiredCredit(0.0,creditToAsk);
			}
			else{
				creditToAsk=0;
			}
		}
		else{
			if(cashOnHand+financialResourcesInBankAccounts<0){
				creditToAsk=cashOnHand+financialResourcesInBankAccounts;
				bestBankAccount.setDesiredCredit(0.0,-creditToAsk);
			}
			else{
				creditToAsk=0;
			}
		}
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" production Capital "+productionCapital+" demand "+demand+" desiredDemand "+desiredDemand+" desiredProductionCapital "+desiredProductionCapital+" cashOnHand "+cashOnHand+" financialResourcesInBankAccounts "+financialResourcesInBankAccounts+" asked credit "+creditToAsk);
System.out.println("      ----------------");
		}
	

	}

	public void adjustProductionCapitalAndBankAccount(){
		double creditToAsk;
		if(desiredProductionCapital>productionCapital){
			creditToAsk=desiredProductionCapital-productionCapital-cashOnHand-financialResourcesInBankAccounts;

			if(creditToAsk>0){
				if(aBankAccount.getAllowedCredit()<aBankAccount.getAccount()){
					double tmpProductionCapital=productionCapital+cashOnHand+financialResourcesInBankAccounts-aBankAccount.getAllowedCredit()+aBankAccount.getAccount();
					if(tmpProductionCapital>=desiredProductionCapital){
						System.out.println("      asked credit; totally allowed    tmpPC "+tmpProductionCapital+" desiredPC "+desiredProductionCapital);
						firmInvestment=desiredProductionCapital-productionCapital;
						productionCapital=desiredProductionCapital;
						aBankAccount.setAccount(aBankAccount.getDemandedCredit());
					}
					else{
						System.out.println("      asked credit; partially allowed    tmpPC "+tmpProductionCapital+" desiredPC "+desiredProductionCapital);
						aBankAccount.setAccount(aBankAccount.getAllowedCredit());
						firmInvestment=tmpProductionCapital-productionCapital;
						productionCapital=tmpProductionCapital;
					}
				}
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
			}
			else{
						System.out.println("      available funds are enough to finance new investment; the excess is deposited");
				firmInvestment=desiredProductionCapital-productionCapital;
				productionCapital=desiredProductionCapital;
				aBankAccount.setAccount(aBankAccount.getAccount()-creditToAsk);
				creditToAsk=0;
			}
		}
		else{
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
		}

		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" investment "+firmInvestment+" production Capital "+productionCapital+" desiredProductionCapital "+desiredProductionCapital);
			System.out.println("     -----------");
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
			System.out.println("     Firm "+identity+" desired Demand "+desiredDemand+" orders of for investments "+ordersOfProductsForInvestmentPurpose);
		}
//		if((productionCapacityAfterWorkersRetire-demand)>(OfficeForStatistics.averageProductivity*Context.parameterOfProductivityInProductionFuncion)){
		if(productionCapacityAfterWorkforceAdjustment-(desiredDemand+ordersOfProductsForInvestmentPurpose)>0){
			while(productionCapacityAfterWorkforceAdjustment>(desiredDemand+ordersOfProductsForInvestmentPurpose)){
				int latestWorkerPosition=workersList.size()-1;
				aConsumer=workersList.get(latestWorkerPosition);
				productionCapacityAfterWorkforceAdjustment=productionCapacityAfterWorkforceAdjustment-aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
				aConsumer.receiveFiredNew();
				workersList.remove(latestWorkerPosition);
			}
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
			System.out.println("     firm "+identity+" application list size "+applicationList.size()+" prodcap "+productionCapacityAfterWorkforceAdjustment+" desired demand "+(desiredDemand*productionCapital/desiredProductionCapital)+" orders for investments "+ordersOfProductsForInvestmentPurpose);
		}
		while(productionCapacityAfterWorkforceAdjustment<((desiredDemand*productionCapital/desiredProductionCapital)+ordersOfProductsForInvestmentPurpose) && applicationListIterator.hasNext()){
			//		while(applicationListIterator.hasNext()){
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
				System.out.println("     firm "+identity+" application list size "+applicationList.size()+" prodcap "+productionCapacityAfterWorkforceAdjustment+" dem "+demand);
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
			cashOnHand=demand+ordersOfProductsForInvestmentPurpose-firmWageSum;
			capitalDepreciation=productionCapital*Context.percentageOfCapitalDepreciation;
			if(Context.verboseFlag){
				System.out.print("     firm "+identity+" demand "+demand+" payed wages "+firmWageSum+" cashOnHand "+cashOnHand+" productionCapital "+productionCapital+" depreciation "+capitalDepreciation);
			}

			productionCapital+=-capitalDepreciation;
			if(Context.verboseFlag){
				System.out.println(" productionCapital "+productionCapital);
			}

		}


		public void payBackBankDebt(){
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
								//		if(Context.verboseFlag){
								System.out.println("      insolvency: occunting delayed to next method");
								//		}
								//							aBankAccount.setDemandedCredit(aBankAccount.getAccount()+resourcesAvailableToRefund);
								//							aBankAccount.increaseUnpaidAmount(-aBankAccount.getAccount()+aBankAccount.getAllowedCredit());
								//							resourcesAvailableToRefund=0;
								resourcesAvailableToRefund+=-toPayBakToThisBankAccount;
							}
						}
					}
				}
				//		if(Context.verboseFlag){
				System.out.println("      totalAmountToRefund "+totalAmountToRefund+" resourcesAvailableToRefund "+resourcesAvailableToRefund+" cashOnHand "+cashOnHand);
				//		}
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
					case 0: aConsumer.setWage(Context.laborMarketStateToSetWage*Context.parameterOfProductivityInProductionFuncion*aConsumer.getProductivity()); 
						break;
					case 1: aConsumer.setWage(Context.laborMarketStateToSetWage*Context.parameterOfProductivityInProductionFuncion*averageProductivityOfWorkersInADegree[degree]);
						break;
					case 2: aConsumer.setWage(Context.laborMarketStateToSetWage*Context.parameterOfProductivityInProductionFuncion*OfficeForStatistics.averageProductivityOfWorkersInADegree[degree]);
						break;
					default: System.out.println("Unknown wage setting rule");
						 break;
				}
			}
		}

		// SEND LABOR DEMAND TO LABOR MARKET

		public void sendVacancies(){
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

		public void setProductAbsoluteRank(int pab){
			productAbsoluteRank=pab;
		}


		public void setDesiredDemand(double industryProduction,double industryDemand){
			demand=(int)Math.round(production/industryProduction*industryDemand);
			desiredDemand=demand;
			if(Context.verboseFlag){
				System.out.println("         Firm "+identity+" production "+production+" demand "+demand+" desired demand "+desiredDemand);
			}
		}


		public void setDemand(double industryProduction,double industryDemand){
			demand=(int)Math.round(production/industryProduction*industryDemand);
			if(Context.verboseFlag){
				System.out.println("         Firm "+identity+" production "+production+" demand "+demand+" desired demand "+desiredDemand);
			}
		}

		public void setOrdersOfProductsForInvestmentPurpose(double industryProduction,double industryInvest){
			ordersOfProductsForInvestmentPurpose=(int)Math.round(production/industryProduction*industryInvest);
			if(Context.verboseFlag){
				System.out.println("         Firm "+identity+" production "+production+" demand "+demand+" products ordered for investments "+ordersOfProductsForInvestmentPurpose);
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




		//reset the firm each time step 
		/*public void reset(){
		  numJobs=0;
		  firmWageSum=0;
		  applicationList.clear();
		  jobList.clear();
		}
		*/
}
