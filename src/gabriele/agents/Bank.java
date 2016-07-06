package gabriele.agents;
   
import java.util.ArrayList;
import java.util.Iterator;
import repast.simphony.random.RandomHelper;

import gabriele.financialContracts.BankAccount;
import gabriele.Context;
import gabriele.agents.Consumer;
import gabriele.agents.Firm;
import gabriele.institutions.CentralBank;

public class Bank {
	ArrayList<BankAccount> accountsList=new ArrayList<BankAccount>();
	BankAccount aBankAccount;
	repast.simphony.context.Context<Object> myContext;
	Iterator bankAccountsListIterator;
	int identity;
	double sumOfHouseholdDesiredChangeInCredit=0;
	double sumOfHouseholdAllowedChangeInCredit=0;
	public double iL;
	double deposits,loans,demandedCredit,allowedCredit,equity;
	Consumer aConsumer;
	Firm aFirm;

	public Bank(int id,repast.simphony.context.Context<Object> con){
		identity=id;
		myContext=con;
		if(Context.verboseFlag){
		System.out.println("Bank "+identity+" created");
		}
	}
	public void addAccount(BankAccount ba){
		accountsList.add(ba);
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" added account");
		}
	}
	public void computeDemandedCredit(){
		demandedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			demandedCredit=demandedCredit-aBankAccount.getDemandedCredit();
		}
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" demanded credit "+demandedCredit);
		}
	}
	public void computeDeposits(){
		deposits=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getAccount()>0){
				deposits=deposits+aBankAccount.getAccount();
		}
	}
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" deposits "+deposits);
		}

	}

/**	
* Method used only once in initialization: setup bank balance sheet and resize deposits for stock-flow consistency.
*/

	public void setupBalance(){
		double initialEquityRatio=RandomHelper.nextDoubleFromTo(Context.minBankInitialEquityRatio,Context.minBankInitialEquityRatio);
		equity=demandedCredit*initialEquityRatio;
		double adjustmentFactor=demandedCredit*(1-initialEquityRatio)/deposits;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getAccount()>0){
				aBankAccount.setAccount(aBankAccount.getAccount()*adjustmentFactor);
			}
			else{
				aBankAccount.setAccount(aBankAccount.getDemandedCredit());
			}
		}
		if(Context.verboseFlag){
		System.out.println("   BANK "+identity+" COORDINATING customers' balances with my balance for consistency ");
		}
	}
	public void computeBalanceVariables(){
		deposits=0;
		loans=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getAccount()>0){
				deposits=deposits+aBankAccount.getAccount();
		}
		else{
			loans=loans-aBankAccount.getAccount();
		}
	}
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" deposits "+deposits+" loans "+loans);
		}

	}
	public void updateConsumersAccounts(){
		double tmpAccount;
		String tmpOwnerType;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			tmpAccount=aBankAccount.getAccount();
			tmpOwnerType=aBankAccount.getOwnerType();
			if(tmpOwnerType=="consumer"){
				aConsumer=(Consumer)aBankAccount.getOwner();
				if(tmpAccount>0){
						if(Context.verboseFlag){
						System.out.println("     consumer "+aConsumer.getIdentity()+": account "+tmpAccount+" interst rate +"+Context.interestRateOnDeposits);
						}
					aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnDeposits));
					aBankAccount.setDemandedCredit(0);
					aBankAccount.setAllowedCredit(0);
				}
				else{
					if(aConsumer.getIsStudentFlag()){
						if(Context.verboseFlag){
						System.out.println("     student "+aConsumer.getIdentity()+": account "+tmpAccount+" interst rate -"+Context.interestRateOnSubsidizedLoans+" rufund not Asked");
						}
						aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnSubsidizedLoans));
						aBankAccount.setDemandedCredit(aBankAccount.getAccount());
						aBankAccount.setAllowedCredit(aBankAccount.getAccount());
					}
					else{
						if(aConsumer.getIsWorkingFlag()){
						if(Context.verboseFlag){
						System.out.println("     worker "+aConsumer.getIdentity()+": account "+tmpAccount+" interst rate -"+Context.interestRateOnLoans);
						}
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnLoans));
							aBankAccount.setDemandedCredit(aBankAccount.getAccount());
							if(RandomHelper.nextDouble()>Context.probabilityToBeAskedToRefundForIndebtedWorkers){
								if(Context.verboseFlag){
									System.out.println("      refund not asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount());
							}
							else{
								if(Context.verboseFlag){
									System.out.println("      refund asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount()*(1-Context.percentageOfLoanToRefundForIndebtedWorkersIfAsked));
							}
						}
						else{
							if(Context.verboseFlag){
								System.out.println("     unemployed "+aConsumer.getIdentity()+": account "+tmpAccount+" interst rate -"+Context.interestRateOnSubsidizedLoans+" rufund not Asked");
							}
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnSubsidizedLoans));
							aBankAccount.setDemandedCredit(aBankAccount.getAccount());
							aBankAccount.setAllowedCredit(aBankAccount.getAccount());
						}
					}
				}

			}

		}
	}

	public void updateFirmsAccounts(){
		double tmpAccount;
		String tmpOwnerType;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			tmpAccount=aBankAccount.getAccount();
			tmpOwnerType=aBankAccount.getOwnerType();
			if(tmpOwnerType=="firm"){
				aFirm=(Firm)aBankAccount.getOwner();
				if(tmpAccount>0){
					if(Context.verboseFlag){
						System.out.println("     "+tmpOwnerType+" "+aFirm.getIdentity()+": account "+tmpAccount+" interst rate +"+Context.interestRateOnDeposits);
					}
					aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnDeposits));
					aBankAccount.setDemandedCredit(0);
					aBankAccount.setAllowedCredit(0);
				}
				else{
						if(Context.verboseFlag){
						System.out.println("     "+tmpOwnerType+" "+aFirm.getIdentity()+": account "+tmpAccount+" interst rate -"+Context.interestRateOnLoans);
						}
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnLoans));
							aBankAccount.setDemandedCredit(aBankAccount.getAccount());
							if(RandomHelper.nextDouble()<Context.firmsProbabilityToHaveOutstandingDebtCompletelyRenewed){
								if(Context.verboseFlag){
									System.out.println("      refund not asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount());
							}
							else{
								if(Context.verboseFlag){
									System.out.println("      refund asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount()*Context.percentageOfOutstandingCreditAllowedToFirmsWhenCreditIsNotCompletelyRenewed);
							}
				}
	
				if(Context.verboseFlag){
					System.out.println("      -----------");
				}
			}
		}

	}

//Desired credit and allowed credit are set to zero if the consumer was able to pay back what asked by bank in the Consumer.payBackBankDebt() method; Otherwise only the allowed credit is set to zero.

/**
 *Set desired credit and allowed credit to zero if the account owner is a consumer.
 */
	public void resetConsumersDemandedAndAllowedCredit(){
		demandedCredit=0;
		allowedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getOwnerType()=="consumer"){
				aBankAccount.resetDemandedCredit();
				aBankAccount.resetAllowedCredit();
/*
				if(aBankAccount.getDemandedCredit()<aBankAccount.getAllowedCredit()){
					aBankAccount.resetAllowedCredit();
				}
				else{
					aBankAccount.resetDemandedCredit();
					aBankAccount.resetAllowedCredit();
				}
*/
			}
		}
	}
	public void resetFirmsDemandedAndAllowedCredit(){
		demandedCredit=0;
		allowedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getOwnerType()=="firm"){
					aBankAccount.resetDemandedCredit();
					aBankAccount.resetAllowedCredit();
/*				if(aBankAccount.getDemandedCredit()<aBankAccount.getAllowedCredit()){
					aBankAccount.resetAllowedCredit();
				}
				else{
					aBankAccount.resetDemandedCredit();
					aBankAccount.resetAllowedCredit();
				}
*/
			}
		}
	}



/**
 *The bank decides if credit demanded by consumers is extended; the current level of credit cannot be reduced here; Recall that credit reduction was asked by bank in the updateConsumersAccounts method and consumers pay back if they have enough resources in the Consumer.payBackBankDebt() method; the desiredCredit and allowedCredit of consumers who satisfied banks requests in the Consumer.payBackBankDebt() method were put to zero in the resetConsumersDemandedAndAllowedCredit() method; So, the desired credit is different from zero for consumers who were not able to refund the bank. 
 */
	public void setAllowedConsumersCredit(){
		demandedCredit=0;
		allowedCredit=0;
		sumOfHouseholdDesiredChangeInCredit=0;
		sumOfHouseholdAllowedChangeInCredit=0;
		double anAccountAmount,anAccountDesiredCredit,anAccounAllowedCredit,multiplier;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getOwnerType()=="consumer"){
				aConsumer=(Consumer)aBankAccount.getOwner();
				anAccountAmount=aBankAccount.getAccount();
				anAccountDesiredCredit=aBankAccount.getDemandedCredit();
				demandedCredit+=-anAccountDesiredCredit;

				if(RandomHelper.nextDouble()>Context.consumersProbabilityToGetFunded){
					multiplier=Context.percentageOfCreditAllowedToConsumersWhenCreditIsNotTotallyFunded;
				}
				else{
					multiplier=1.0;
				}

				if(aConsumer.getIsStudentFlag()){
					multiplier=1.0;
				}


				if(anAccountAmount>=0){
					anAccounAllowedCredit=multiplier*anAccountDesiredCredit;
					if(anAccountDesiredCredit<anAccountAmount){
						sumOfHouseholdDesiredChangeInCredit+=anAccountDesiredCredit;
						sumOfHouseholdAllowedChangeInCredit+=anAccounAllowedCredit;
					}
					if(Context.verboseFlag){
						System.out.println(" positive account "+anAccountAmount+" desired "+anAccountDesiredCredit+ " allowed "+anAccounAllowedCredit);
					}
				}
				else{
					if(anAccountDesiredCredit<0){
						anAccounAllowedCredit=anAccountAmount+multiplier*(anAccountDesiredCredit-anAccountAmount);
					}
					else{
						anAccounAllowedCredit=0;
					}
					if(anAccountDesiredCredit<anAccountAmount){
						sumOfHouseholdDesiredChangeInCredit+=(anAccountDesiredCredit-anAccountAmount);
						sumOfHouseholdAllowedChangeInCredit+=(anAccounAllowedCredit-anAccountAmount);
					}
					if(Context.verboseFlag){
						System.out.println(" negative account "+anAccountAmount+" desired "+anAccountDesiredCredit+ " allowed "+anAccounAllowedCredit+" new allowed "+(anAccounAllowedCredit-anAccountAmount));
					}
				}
				allowedCredit+=-anAccounAllowedCredit;

		if(Context.verboseFlag){
				System.out.println("      askedCredit "+aBankAccount.getDemandedCredit());
		}
				aBankAccount.setAllowedCredit(anAccounAllowedCredit);
			}
		}
		if(Context.verboseFlag){
			System.out.println("     bank "+identity+" demanded credit "+demandedCredit+" allowed credit "+allowedCredit);
		}
	}

	public void setAllowedFirmsCredit(){
		demandedCredit=0;
		allowedCredit=0;
		double anAccountAmount,anAccountDesiredCredit,anAccounAllowedCredit,multiplier;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getOwnerType()=="firm"){
				anAccountAmount=aBankAccount.getAccount();
				anAccountDesiredCredit=aBankAccount.getDemandedCredit();
				demandedCredit=demandedCredit-anAccountDesiredCredit;
				if(RandomHelper.nextDouble()<Context.firmsProbabilityToHaveNewDemandedCreditCompletelyAllowed){
					multiplier=1.0;
				}
				else{
					multiplier=Context.percentageOfNewDemandedCreditAllowedToFirmsWhenCreditIsNotCompletelyAllowed;
				}
				if(anAccountAmount>=0){
					anAccounAllowedCredit=multiplier*anAccountDesiredCredit;
				}
				else{
					if(anAccountDesiredCredit<0){
						anAccounAllowedCredit=anAccountAmount+multiplier*(anAccountDesiredCredit-anAccountAmount);
					}
					else{
						anAccounAllowedCredit=0;
					}


				}
				allowedCredit+=-anAccounAllowedCredit;
		if(Context.verboseFlag){
				System.out.println("      askedCredit "+aBankAccount.getDemandedCredit());
		}
				aBankAccount.setAllowedCredit(anAccounAllowedCredit);
			}
		}
		if(Context.verboseFlag){
			System.out.println("     bank "+identity+" demanded credit "+demandedCredit+" allowed credit "+allowedCredit+" (only firms that asked new credit are considered)");
		}
	}
	
	public void removeExitedFirmsBankAccounts(){
		bankAccountsListIterator=accountsList.iterator();
		while(bankAccountsListIterator.hasNext()){
			aBankAccount=(BankAccount)bankAccountsListIterator.next();
			if(aBankAccount.getAccountShutDownFlag()){
				bankAccountsListIterator.remove();
		if(Context.verboseFlag){
			System.out.println("     bank "+identity+" account removed ");
		}
			}
		}

	}
	public double getLoans(){
		return loans;
	}
	public double getDeposits(){
		return deposits;
	}
	public double getEquity(){
		return equity;
	}

	public double getSumOfHouseholdDesiredChangeInCredit(){
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" sum of new desired credit "+sumOfHouseholdDesiredChangeInCredit);
		}
		return sumOfHouseholdDesiredChangeInCredit;
	}
	public double getSumOfHouseholdAllowedChangeInCredit(){
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" sum of new allowed credit "+sumOfHouseholdAllowedChangeInCredit);
		}
		return sumOfHouseholdAllowedChangeInCredit;
	}

	public int getIdentity(){
		return identity;
	}

/*
	public void setAllowedFirmsCredit(){
		demandedCredit=0;
		allowedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			demandedCredit=demandedCredit-aBankAccount.getDemandedCredit();
			if(RandomHelper.nextDouble()>0.5){
				double demCred=aBankAccount.getDemandedCredit();
				double allCred=demCred*0.5;
				aBankAccount.setAllowedCredit(allCred);
				allowedCredit=allowedCredit-allCred;
			}
			else{
				double demCred=aBankAccount.getDemandedCredit();
				double allCred=demCred*1.5;
				aBankAccount.setAllowedCredit(allCred);
				allowedCredit=allowedCredit-allCred;
			}
		}
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" demanded credit "+demandedCredit+" allowed credit "+allowedCredit);
		}
	}
*/

}
