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
					aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnDeposits));
				}
				else{
					aConsumer=(Consumer)aBankAccount.getOwner();
					if(aConsumer.getIsStudentFlag()){
						aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnSubsidizedLoans));
						aBankAccount.setAllowedCredit(aBankAccount.getAccount());
					}
					else{
						if(aConsumer.getIsWorkingFlag()){
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnLoans));
							if(RandomHelper.nextDouble()>0.5){
								aBankAccount.setAllowedCredit(aBankAccount.getAccount());
							}
							else{
								aBankAccount.setAllowedCredit(aBankAccount.getAccount()*0.9);
							}
						}
						else{
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnSubsidizedLoans));
							aBankAccount.setAllowedCredit(aBankAccount.getAccount());
						}
					}
				}

			}

		}
	}
	public void setAllowedConsumersCredit(){
		demandedCredit=0;
		allowedCredit=0;
		double anAccountDesiredCredit,anAccounAllowedCredit,multiplier;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			anAccountDesiredCredit=aBankAccount.getDemandedCredit();
			demandedCredit=demandedCredit-anAccountDesiredCredit;
			if(RandomHelper.nextDouble()>0.5){
				multiplier=0.5;
			}
			else{
				multiplier=1.5;
			}
			anAccounAllowedCredit=multiplier*anAccountDesiredCredit;
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
