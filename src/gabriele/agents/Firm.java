/*
GABRIELE: the General Agent Based Repast Implemented Extensible Laboratory for Economics
Copyright (C) 2018  Gianfranco Giulioni
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package gabriele.agents;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;

import repast.simphony.random.RandomHelper;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.essentials.RepastEssentials;

//import java.util.ArrayList;
//import gabriele.LaborMkt;
import gabriele.institutions.OfficeForStatistics;
import gabriele.bargaining.Curriculum;
import gabriele.bargaining.LaborOffer;
import gabriele.institutions.LaborMarket;
import gabriele.Context;
import gabriele.financialContracts.BankAccount;
import gabriele.agents.Bank;
import gabriele.bargaining.Curriculum;

public class Firm {
	long production;
	int FirmID,identity,age,vacancySum,totalJobs,numVacancy,numJobs,productAbsoluteRank,demand,desiredDemand,numberOfEmployees,workersPotentialProduction,capitalPotentialProduction;
	double reservationWageFirmSum,firmWageSum;
	double sumOfWorkersProductivity=0;
	double productionCapacityAfterWorkforceAdjustment;
	double targetLevelOfworkersProductionCapacity;
	public double desiredProductionCapital,productionCapital,debt,equity,sumOfBankAccounts,productionCapitalBeforeDepreciation,productionCapitalAfterDepreciation;
	double cashOnHand,depreciationOfUsedCapital,depreciationOfUnusedCapital,capitalDepreciation,financialResourcesInBankAccounts,amountToWithdrawFromBanksForInvestments,creditToAskInSetDesiredCredit,cashOnHandWhenComputingEconomicResult,cashOnHandAfterRefundingBank;
	double unpaidAmountInBankAccounts=0;	
	double firmInvestment=0;
	double promissoryNotes=0;
	double firmRealizedInvestment=0;
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
	double[] senderFirmReservationWage;


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
/**
 * Method used only once during initialization: all the received job applications are accepted, the sum of workers' productivity is computed and the production is computed as a function of the sum of productivities.
 */
	public void setInitialWorkers(){
		sumOfWorkersProductivity=0;
		if(applicationList.size()>0){
			/*
			   try{
			   consumersList=myContext.getObjects(Class.forName("gabriele.agents.Consumer"));
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
/**
*Each Firm setup its balance sheet ant thus determine the level of debt....
Then, selects randomly a number of banks equal to Context.numberOfBanksAFirmCanBeCustumerOf and open a bank account in each of them. The amount of the bank account is set to zero (will be assigned in the next step when the bank balance sheet will be initialized), but the variable demandedCredit is set to the debt divided by the number of banks the firm is customer of.
*/
	public void setupBankAccountInInitialization(){
		productionCapital=production;//workersList.size()*Context.parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion;
		equity=productionCapital*RandomHelper.nextDoubleFromTo(Context.minFirmInitialEquityRatio,Context.minFirmInitialEquityRatio);
		debt=productionCapital-equity;
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" production capital "+productionCapital+" debt "+debt+" equity "+equity);
		}
		try{
			banksList=myContext.getObjects(Class.forName("gabriele.agents.Bank"));
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
		equity=productionCapital*RandomHelper.nextDoubleFromTo(Context.minFirmInitialEquityRatio,Context.maxFirmInitialEquityRatio);
		debt=productionCapital-equity;
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" production capital "+productionCapital+" debt "+debt+" equity "+equity);
		}
		try{
			banksList=myContext.getObjects(Class.forName("gabriele.agents.Bank"));
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
			aBankAccount.setAccount(-debt/numberOfBanksToBeCustomerOf);
			aBankAccount.setAllowedCredit(-debt/numberOfBanksToBeCustomerOf);
			bankAccountsList.add(aBankAccount);
			aBank.addAccount(aBankAccount);
		}

	}

	public void setupBankAccountOfNewEnteringFirm(){
		//		productionCapital=Context.parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion;
		productionCapital=0;
		productionCapacityAfterWorkforceAdjustment=0;
		firmInvestment=Context.productionOfNewEnteringFirm;
		desiredProductionCapital=firmInvestment;
		creditToAskInSetDesiredCredit=firmInvestment;
		//		desiredDemand=Context.parameterOfProductivityInProductionFuncion;
		//		desiredDemand=(int)productionCapital;
		//		equity=productionCapital*RandomHelper.nextDoubleFromTo(Context.minFirmInitialEquityRatio,Context.maxFirmInitialEquityRatio);
		//		debt=productionCapital-equity;
		desiredDemand=0;
		equity=0;
		debt=0;
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" production capital "+productionCapital+" debt "+debt+" equity "+equity);
		}
		try{
			banksList=myContext.getObjects(Class.forName("gabriele.agents.Bank"));
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
			aBankAccount.setAccount(-debt/numberOfBanksToBeCustomerOf);
			aBankAccount.setAllowedCredit(-debt/numberOfBanksToBeCustomerOf);
			bankAccountsList.add(aBankAccount);
			bankAccountsListWithNoUnpaidAmount.add(aBankAccount);
			aBank.addAccount(aBankAccount);
		}
		bestBankAccount=(BankAccount)bankAccountsList.get(0);
		bestBankAccount.setAllowedCredit(-firmInvestment);


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
		//identify the bank account where resources will be deposited if available and accounts with no unpaid amount
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

		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" n of banks with no unpaid amount "+bankAccountsListWithNoUnpaidAmount.size());
		}

		//computation and requests of new credit
		//


		double creditToAsk;
		if(desiredProductionCapital>productionCapital){
			creditToAsk=desiredProductionCapital-productionCapital+unpaidAmountInBankAccounts-cashOnHand-financialResourcesInBankAccounts;
			creditToAskInSetDesiredCredit=creditToAsk;
		}
		else{
			creditToAsk=unpaidAmountInBankAccounts-cashOnHand-financialResourcesInBankAccounts;
			creditToAskInSetDesiredCredit=creditToAsk;
		}
		if(creditToAsk>0){
			if(bankAccountsListWithNoUnpaidAmount.size()>0){
				bestBankAccount.setDesiredCredit(0.0,creditToAsk);
			}
			else{
				if(Context.verboseFlag){
					System.out.println("       credit needed but not asked because the firm has unpaid amounts in all the banks"); 
				}
			}
		}
		else{
			creditToAsk=0;
			creditToAskInSetDesiredCredit=creditToAsk;
			if(Context.verboseFlag){
				System.out.println("       credit not needed"); 
			}
		}

		//verboseFlag
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" depreciated production Capital "+productionCapital+" demand from household +firms "+(demand+ordersOfProductsForInvestmentPurpose)+" desiredDemand from household + firms "+(desiredDemand+ordersOfProductsForInvestmentPurpose)+" desiredProductionCapital "+desiredProductionCapital+" cashOnHand "+cashOnHand+" financialResourcesInBankAccounts "+financialResourcesInBankAccounts+" needed credit "+creditToAsk);
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
		if(productionCapacityAfterWorkforceAdjustment>targetLevelOfworkersProductionCapacity){
			firmRealizedInvestment=firmInvestment;
		}
		else{
			firmRealizedInvestment=productionCapacityAfterWorkforceAdjustment-productionCapital;
		}
		if(Context.verboseFlag){
			System.out.println("     firm "+identity+" productionCapacityAfterWorkforceAdjustment "+productionCapacityAfterWorkforceAdjustment+" chosen production capital "+(productionCapital+firmRealizedInvestment)+" firmRealizedInvestment "+firmRealizedInvestment+" desired demand + investment "+(desiredDemand+ordersOfProductsForInvestmentPurpose));
		}


		if(creditToAskInSetDesiredCredit>0){
			if(desiredProductionCapital>productionCapital){
				if(bankAccountsListWithNoUnpaidAmount.size()>0){
					if(Context.verboseFlag){
						System.out.println("      Best bank account does exists");
					}
					if(firmInvestment<=0){
						bestBankAccount.setAccount(bestBankAccount.getAllowedCredit());
					}
					else{  //here investment can be either 0 or positive
						if(firmRealizedInvestment>0){
							bestBankAccount.setAccount(bestBankAccount.getAllowedCredit()+firmInvestment-firmRealizedInvestment);
						}
					}
				}
				else{
					if(Context.verboseFlag){
						System.out.println("      Best bank account does not exists");
					}

				}
			}
		}
		else{ //if credit was not asked
			if(cashOnHand>0){
				if(firmRealizedInvestment>0){
					if(firmRealizedInvestment>0){
						cashOnHand+=-firmRealizedInvestment;
					}
					worstBankAccount.setAccount(worstBankAccount.getAccount()+cashOnHand);
					cashOnHand=0;
				}
			}
			else{ //if cashOnHand=0
				if(amountToWithdrawFromBanksForInvestments>0){
					if(firmRealizedInvestment>0){
						amountToWithdrawFromBanksForInvestments+=firmRealizedInvestment-firmInvestment;
						if(amountToWithdrawFromBanksForInvestments>0){
							double multiplier;
							if(financialResourcesInBankAccounts>0){
								multiplier=amountToWithdrawFromBanksForInvestments/financialResourcesInBankAccounts;
							}
							else{
								multiplier=0;
							}
							if(Context.verboseFlag){
								System.out.println("      amount to withdraw from bank accounts "+amountToWithdrawFromBanksForInvestments+" multiplier "+multiplier);
							}
							for(int i=0;i<bankAccountsList.size();i++){
								aBankAccount=(BankAccount)bankAccountsList.get(i);
								double thisBankAccount=aBankAccount.getAccount();
								if(thisBankAccount>0){
									if(Context.verboseFlag){
										System.out.println("       positive bank Account resized by a factor of "+(1-multiplier));
									}
									aBankAccount.setAccount(aBankAccount.getAccount()*(1-multiplier));
								}
							}
						}
						else{
							worstBankAccount.setAccount(worstBankAccount.getAccount()-amountToWithdrawFromBanksForInvestments);
							amountToWithdrawFromBanksForInvestments=0;
						}
					}
				}
			}
		}
		firmInvestment=firmRealizedInvestment;
		productionCapital+=firmInvestment;
	}

	public void setPossibleInvestment(){
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

		promissoryNotes=0;
		//if credit was asked in setDesiredCredit method
		if(creditToAskInSetDesiredCredit>0){
			//get new available credit
			double amountAvailableFromBestBank=0;
			if(bankAccountsListWithNoUnpaidAmount.size()>0){
				if(Context.verboseFlag){
					System.out.println("      Best bank account does exists");
				}
				amountAvailableFromBestBank=-(bestBankAccount.getAllowedCredit()-bestBankAccount.getAccount());
//				bestBankAccount.setAccount(bestBankAccount.getAllowedCredit());
			}
			else{
				if(Context.verboseFlag){
					System.out.println("      Best bank account does not exists");
				}
			
			}
				if(Context.verboseFlag){
					System.out.println("      amountAvailableFromBestBank "+amountAvailableFromBestBank);
				}
			//withdraw all the deposits and put it in the cashOnHand
			for(int i=0;i<bankAccountsList.size();i++){
				aBankAccount=(BankAccount)bankAccountsList.get(i);
				if(aBankAccount.getAccount()>0){
					cashOnHand+=aBankAccount.getAccount();
					aBankAccount.setAccount(0);
				}
			}
			if(cashOnHand<0){
				if(amountAvailableFromBestBank>=(-cashOnHand)){
					amountAvailableFromBestBank+=cashOnHand;
					cashOnHand=0;
					if(unpaidAmountInBankAccounts>0){
						if(amountAvailableFromBestBank>unpaidAmountInBankAccounts){
							for(int i=0;i<bankAccountsList.size();i++){
								aBankAccount=(BankAccount)bankAccountsList.get(i);
								double thisBankUnpaidAmount=aBankAccount.getUnpaidAmount();
								if(thisBankUnpaidAmount>0){
									aBankAccount.setUnpaidAmount(0);
									aBankAccount.setAccount(aBankAccount.getAccount()-thisBankUnpaidAmount);
								}
							}
							amountAvailableFromBestBank+=-unpaidAmountInBankAccounts;
							unpaidAmountInBankAccounts=0;
						}
						else{
							double multiplier=amountAvailableFromBestBank/unpaidAmountInBankAccounts;
							for(int i=0;i<bankAccountsList.size();i++){
								aBankAccount=(BankAccount)bankAccountsList.get(i);
								double thisBankUnpaidAmount=aBankAccount.getUnpaidAmount();
								if(thisBankUnpaidAmount>0){
									aBankAccount.setUnpaidAmount((1-multiplier)*thisBankUnpaidAmount);
									aBankAccount.setAccount(aBankAccount.getAccount()-multiplier*thisBankUnpaidAmount);
								}
							}
							amountAvailableFromBestBank=0;
						}
					}
					else{
					}
				}
				else{

					cashOnHand+=amountAvailableFromBestBank;
					amountAvailableFromBestBank=0;
					promissoryNotes=cashOnHand;
					cashOnHand=0;
					if(Context.verboseFlag){
						System.out.println("      promissory notes "+promissoryNotes);
					}
				}
				firmInvestment=amountAvailableFromBestBank;
				amountAvailableFromBestBank=0;
			}
			//if cashOnHand>=0
			else{
				double resourcesAvailable=cashOnHand+amountAvailableFromBestBank;
				if(unpaidAmountInBankAccounts>0){
					if(resourcesAvailable>unpaidAmountInBankAccounts){
						for(int i=0;i<bankAccountsList.size();i++){
							aBankAccount=(BankAccount)bankAccountsList.get(i);
							double thisBankUnpaidAmount=aBankAccount.getUnpaidAmount();
							if(thisBankUnpaidAmount>0){
								aBankAccount.setUnpaidAmount(0);
								aBankAccount.setAccount(aBankAccount.getAccount()-thisBankUnpaidAmount);
							}
						}
						resourcesAvailable+=-unpaidAmountInBankAccounts;
						unpaidAmountInBankAccounts=0;
					}
					else{
						double multiplier=resourcesAvailable/unpaidAmountInBankAccounts;
						for(int i=0;i<bankAccountsList.size();i++){
							aBankAccount=(BankAccount)bankAccountsList.get(i);
							double thisBankUnpaidAmount=aBankAccount.getUnpaidAmount();
							if(thisBankUnpaidAmount>0){
								aBankAccount.setUnpaidAmount((1-multiplier)*thisBankUnpaidAmount);
								aBankAccount.setAccount(aBankAccount.getAccount()-multiplier*thisBankUnpaidAmount);
							}
						}
						resourcesAvailable=0;
					}
				}
				else{
				}
				firmInvestment=resourcesAvailable;
				resourcesAvailable=0;
			}
			amountToWithdrawFromBanksForInvestments=0;
		}
		//if credit was not asked in setDesiredCredit method (creditToAskInSetDesiredCredit=0)
		else{
			double amountTowithdrawFromBanks=0;
			if(cashOnHand<0){
				amountTowithdrawFromBanks+=-cashOnHand;
				cashOnHand=0;
				if(desiredProductionCapital>productionCapital){
					firmInvestment=desiredProductionCapital-productionCapital;
					amountTowithdrawFromBanks+=firmInvestment;
				}
				else{
					firmInvestment=0;			
				}
				if(unpaidAmountInBankAccounts>0){
					amountTowithdrawFromBanks+=unpaidAmountInBankAccounts;
					for(int i=0;i<bankAccountsList.size();i++){
						aBankAccount=(BankAccount)bankAccountsList.get(i);
						double thisBankUnpaidAmount=aBankAccount.getUnpaidAmount();
						if(thisBankUnpaidAmount>0){
							aBankAccount.setUnpaidAmount(0);
							aBankAccount.setAccount(aBankAccount.getAccount()-thisBankUnpaidAmount);
						}
					}

				}
				else{
				}

			}
			//if cashOnHand >=0
			else{
				if(unpaidAmountInBankAccounts>0){
					for(int i=0;i<bankAccountsList.size();i++){
						aBankAccount=(BankAccount)bankAccountsList.get(i);
						double thisBankUnpaidAmount=aBankAccount.getUnpaidAmount();
						if(thisBankUnpaidAmount>0){
							aBankAccount.setUnpaidAmount(0);
							aBankAccount.setAccount(aBankAccount.getAccount()-thisBankUnpaidAmount);
						}
					}
					if(cashOnHand>unpaidAmountInBankAccounts){
						cashOnHand+=-unpaidAmountInBankAccounts;
						unpaidAmountInBankAccounts=0;
					}
					else{
						amountTowithdrawFromBanks+=(unpaidAmountInBankAccounts-cashOnHand);
						cashOnHand=0;
					}
				}
				else{
				}

				if(desiredProductionCapital>productionCapital){
					firmInvestment=desiredProductionCapital-productionCapital;
					if(cashOnHand>=firmInvestment){
						if(Context.verboseFlag){
							System.out.println("       cash on hand higher than investments. Residual cashOnHand is deposited");
						}
//						cashOnHand+=-firmInvestment;
//						worstBankAccount.setAccount(worstBankAccount.getAccount()+cashOnHand);
//						cashOnHand=0;
					}
					else{
						amountTowithdrawFromBanks+=firmInvestment-cashOnHand;
						cashOnHand=0;
					}
				}
				else{
					if(Context.verboseFlag){
						System.out.println("       positive cash on hand and no investments. All cashOnHand is deposited");
					}
					firmInvestment=0;
//					worstBankAccount.setAccount(worstBankAccount.getAccount()+cashOnHand);
//					cashOnHand=0;
				}
			}
			amountToWithdrawFromBanksForInvestments=amountTowithdrawFromBanks;
/*
			double multiplier;
			if(financialResourcesInBankAccounts>0){
				multiplier=amountTowithdrawFromBanks/financialResourcesInBankAccounts;
			}
			else{
				multiplier=0;
			}
			if(Context.verboseFlag){
				System.out.println("      amount to withdraw from bank accounts "+amountTowithdrawFromBanks+" multiplier "+multiplier);
			}
			for(int i=0;i<bankAccountsList.size();i++){
				aBankAccount=(BankAccount)bankAccountsList.get(i);
				double thisBankAccount=aBankAccount.getAccount();
				if(thisBankAccount>0){
					if(Context.verboseFlag){
						System.out.println("       positive bank Account resized by a factor of "+(1-multiplier));
					}
					aBankAccount.setAccount(aBankAccount.getAccount()*(1-multiplier));
				}
			}
*/
		}
//		productionCapital+=firmInvestment;
//the following code is needed for the correct functioning of the OfficeForStatistics.computeInvestments()
		if(desiredProductionCapital<productionCapital){
			firmInvestment=desiredProductionCapital-productionCapital;
		}


		cashOnHandAfterRefundingBank=cashOnHand;

		if(Context.verboseFlag){
			System.out.println("      Firm "+identity+" possible investment "+firmInvestment+" production Capital "+productionCapital+" desiredProductionCapital "+desiredProductionCapital+" promissory notes "+promissoryNotes);
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
				productionCapacityAfterWorkforceAdjustment+=aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
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
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" desired Demand "+desiredDemand+" orders of products for investments "+ordersOfProductsForInvestmentPurpose+" desired demand + invest goods "+(desiredDemand+ordersOfProductsForInvestmentPurpose)+" productionCapacityAfterFiring "+productionCapacityAfterWorkforceAdjustment);
		}

		}
		else{
		if(Context.verboseFlag){
			System.out.println("     Firm "+identity+" desired Demand "+desiredDemand+" orders of products for investments "+ordersOfProductsForInvestmentPurpose+" desired demand + invest goods "+(desiredDemand+ordersOfProductsForInvestmentPurpose)+" productionCapacityAfterFiring "+productionCapacityAfterWorkforceAdjustment);
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
			System.out.println("     firm "+identity+" application list size "+applicationList.size()+" productionCapacity "+productionCapacityAfterWorkforceAdjustment+" desired demand + investment "+(desiredDemand+ordersOfProductsForInvestmentPurpose));
		}
		if(hadFired){
				targetLevelOfworkersProductionCapacity=desiredProductionCapital;
		if(Context.verboseFlag){
			System.out.println("     I am not hiring because I have just fired");
		}
		}
		else{
			if(firmInvestment>=0){
				targetLevelOfworkersProductionCapacity=productionCapital+firmInvestment;
			}
			else{
				targetLevelOfworkersProductionCapacity=desiredProductionCapital;
			}
			while(productionCapacityAfterWorkforceAdjustment<targetLevelOfworkersProductionCapacity && applicationListIterator.hasNext()){
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
					productionCapacityAfterWorkforceAdjustment+=aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
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
		depreciationOfUsedCapital=(demand+ordersOfProductsForInvestmentPurpose)*Context.percentageOfUsedCapitalDepreciation;
		depreciationOfUnusedCapital=(productionCapital-demand-ordersOfProductsForInvestmentPurpose)*Context.percentageOfUnusedCapitalDepreciation;
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
					if(Context.verboseFlag){
			System.out.println("     Firm "+getIdentity()+": to refund "+totalAmountToRefund+" financial resource in bank accounts "+financialResourcesInBankAccounts+" cashOnHand "+cashOnHand);
					}
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
					if(Context.verboseFlag){
			System.out.println("      totalAmountToRefund "+totalAmountToRefund+" resourcesAvailableToRefund "+resourcesAvailableToRefund+" cashOnHand "+cashOnHand);
			}
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
		else{			double targetLevelOfworkersProductionCapacity;
			if(firmInvestment>=0){
				targetLevelOfworkersProductionCapacity=productionCapital;
			}
			else{
				targetLevelOfworkersProductionCapacity=desiredDemand;
			}
			if(productionCapacityAfterWorkforceAdjustment<targetLevelOfworkersProductionCapacity){
				for(int i=0;i<averageProductivityOfWorkersInADegree.length;i++){
					senderFirmReservationWage[i]=Context.unemploymentDole+Context.laborMarketStateToSetWage*Context.parameterOfProductivityInProductionFuncion*averageProductivityOfWorkersInADegree[i];
				}

				myOffer = new LaborOffer(this,identity,(targetLevelOfworkersProductionCapacity-productionCapacityAfterWorkforceAdjustment),senderFirmReservationWage);
				try{
					myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("gabriele.institutions.LaborMarket"))).get(0);
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
	public double getPromissoryNotes(){
		return promissoryNotes;
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

	public void stepProductInnovationProcess(){
		if(RandomHelper.nextDouble()<Context.probabilityOfAProductInnovation){
			productAbsoluteRank++;
			if(Context.verboseFlag){
				System.out.println("     Firm "+identity+" product Absolute Rank "+productAbsoluteRank);
			}
		}
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
