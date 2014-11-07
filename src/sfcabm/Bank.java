package sfcabm;

import java.util.ArrayList;

public class Bank {
	ArrayList<BankAccount> accountsList=new ArrayList<BankAccount>();
	BankAccount aBankAccount;
	repast.simphony.context.Context<Object> myContext;
	int identity;
	
	public double iL;
	double deposits,loans,demandedCredit,equity;

	public Bank(int id,repast.simphony.context.Context<Object> con){
		identity=id;
		myContext=con;
		System.out.println("Bank Created");
	}
	public void addAccount(BankAccount ba){
		accountsList.add(ba);
		System.out.println("     bank "+identity+" added account");
	}
	public void computeDemandedCredit(){
		demandedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			demandedCredit=demandedCredit-aBankAccount.getDemandedCredit();
		}
		System.out.println("     bank "+identity+" demanded credit "+demandedCredit);
	}
	public void computeDeposits(){
		deposits=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getAccount()>0){
				deposits=deposits+aBankAccount.getAccount();
		}
	}
		System.out.println("     bank "+identity+" deposits "+deposits);

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
		System.out.println("   BANK "+identity+" COORDINATING customers' balances with my balance for consistency ");
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
		System.out.println("     bank "+identity+" deposits "+deposits+" loans "+loans);

	}

}
