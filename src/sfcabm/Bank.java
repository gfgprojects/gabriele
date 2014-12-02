package sfcabm;

import java.util.ArrayList;
import repast.simphony.random.RandomHelper;

public class Bank {
	ArrayList<BankAccount> accountsList=new ArrayList<BankAccount>();
	BankAccount aBankAccount;
	repast.simphony.context.Context<Object> myContext;
	int identity;
	
	public double iL;
	double deposits,loans,demandedCredit,allowedCredit,equity;
	Consumer aConsumer;

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
	public void setupBalance(){
		equity=demandedCredit*0.1;
		double adjustmentFactor=demandedCredit*0.9/deposits;
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
				if(tmpAccount>0){
						if(Context.verboseFlag){
						System.out.println("     consumer: account "+tmpAccount+" interst rate +"+Context.interestRateOnDeposits);
						}
					aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnDeposits));
				}
				else{
					aConsumer=(Consumer)aBankAccount.getOwner();
					if(aConsumer.getIsStudentFlag()){
						if(Context.verboseFlag){
						System.out.println("     student: account "+tmpAccount+" interst rate -"+Context.interestRateOnSubsidizedLoans+" rufund not Asked");
						}
						aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnSubsidizedLoans));
						aBankAccount.setAllowedCredit(aBankAccount.getAccount());
					}
					else{
						if(aConsumer.getIsWorkingFlag()){
						if(Context.verboseFlag){
						System.out.println("     worker: account "+tmpAccount+" interst rate -"+Context.interestRateOnLoans);
						}
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnLoans));
							if(RandomHelper.nextDouble()>0.5){
								if(Context.verboseFlag){
									System.out.println("     refund not asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount());
							}
							else{
								if(Context.verboseFlag){
									System.out.println("     refund asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount()*0.9);
							}
						}
						else{
							if(Context.verboseFlag){
								System.out.println("     unemployed: account "+tmpAccount+" interst rate -"+Context.interestRateOnSubsidizedLoans+" rufund not Asked");
							}
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnSubsidizedLoans));
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
				if(tmpAccount>0){
						if(Context.verboseFlag){
						System.out.println("     "+tmpOwnerType+": account "+tmpAccount+" interst rate +"+Context.interestRateOnDeposits);
						}
					aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnDeposits));
				}
				else{
						if(Context.verboseFlag){
						System.out.println("     "+tmpOwnerType+": account "+tmpAccount+" interst rate -"+Context.interestRateOnLoans);
						}
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnLoans));
							if(RandomHelper.nextDouble()>0.5){
								if(Context.verboseFlag){
									System.out.println("     refund not asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount());
							}
							else{
								if(Context.verboseFlag){
									System.out.println("     refund asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount()*0.9);
							}
				}
	
			}
		}

	}



	public void resetDemandedAndAllowedCredit(){
		demandedCredit=0;
		allowedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			aBankAccount.resetDemandedCredit();
			aBankAccount.resetAllowedCredit();
		}
	}


/**
 *Bank decides if credit demanded by consumers is extended; outstanding credit cannot be reduced here; Credit reduction was performed by bank in the updateConsumersAccounts method  
 */
	public void setAllowedConsumersCredit(){
		demandedCredit=0;
		allowedCredit=0;
		double anAccountAmount,anAccountDesiredCredit,anAccounAllowedCredit,multiplier;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			anAccountAmount=aBankAccount.getAccount();
			anAccountDesiredCredit=aBankAccount.getDemandedCredit();
			demandedCredit=demandedCredit-anAccountDesiredCredit;
			if(anAccountAmount>=0){
				if(RandomHelper.nextDouble()>0.5){
					multiplier=0.5;
				}
				else{
					multiplier=1.0;
				}
				anAccounAllowedCredit=multiplier*anAccountDesiredCredit;
			}
			else{
				if(RandomHelper.nextDouble()>0.5){
					multiplier=0.5;
				}
				else{
					multiplier=1.0;
				}
				anAccounAllowedCredit=anAccountAmount+multiplier*(anAccountDesiredCredit-anAccountAmount);
			

			}
			allowedCredit+=-anAccounAllowedCredit;
			aBankAccount.setAllowedCredit(anAccounAllowedCredit);
		}
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" demanded credit "+demandedCredit+" allowed credit "+allowedCredit);
		}
	}
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

}
