package gabriele.institutions;

import gabriele.financialContracts.BankAccount;
import gabriele.Context;

public class Government {
BankAccount bankAccount;

public Government(){
	bankAccount=new BankAccount(0,null,null);
}
public void setInitialAmountInBankAccount(double initialAnountInBankAccount){
	bankAccount.setAccount(initialAnountInBankAccount);
	if(Context.verboseFlag){
	System.out.println("      public debt "+initialAnountInBankAccount*-1);
	}
}
public BankAccount getBankAccount(){
	return bankAccount;
}

}
