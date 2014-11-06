package sfcabm;

import java.util.ArrayList;

public class Bank {
	ArrayList<BankAccount> accountsList=new ArrayList<BankAccount>();
	BankAccount aBankAccount;
	repast.simphony.context.Context<Object> myContext;
	int identity;
	
	public double iL;
	double deposits,loans;

	public Bank(int id,repast.simphony.context.Context<Object> con){
		identity=id;
		myContext=con;
		System.out.println("Banca Creata");
	}
	public void addAccount(BankAccount ba){
		accountsList.add(ba);
		System.out.println("     banca "+identity+" aggiunto account");
	}

}
