package gabriele.institutions;

import gabriele.financialContracts.BankAccount;
import gabriele.Context;

public class Government {
public BankAccount bankAccount;
public double publicSurplusDesiredByCentralBank,desiredTaxRate,taxRateWithZeroPublicBalace,publicSurplus,unpaidAmount;
public Government(){
	bankAccount=new BankAccount(0,null,null);
}
public void setInitialAmountInBankAccount(double initialAnountInBankAccount){
	bankAccount.setAccount(initialAnountInBankAccount);
	if(Context.verboseFlag){
	System.out.println("      public debt "+initialAnountInBankAccount*-1);
	}
}
public void setTaxRate(int numberOfUnemployed,double taxableIncome){
	publicSurplusDesiredByCentralBank=bankAccount.getAllowedCredit()-bankAccount.getAccount();
	desiredTaxRate=(publicSurplusDesiredByCentralBank+Context.unemploymentDole*numberOfUnemployed)/taxableIncome;
	taxRateWithZeroPublicBalace=(Context.unemploymentDole*numberOfUnemployed)/taxableIncome;
	Context.taxRate=desiredTaxRate;
	if(desiredTaxRate<0){
		Context.taxRate=0;
	}
	if(desiredTaxRate>Context.maximumTaxRate){
		if(publicSurplusDesiredByCentralBank>0){
			if(taxRateWithZeroPublicBalace>Context.maximumTaxRate){
				Context.taxRate=taxRateWithZeroPublicBalace;
			}
			else{
				Context.taxRate=Context.maximumTaxRate;
			}
		}
	}
	publicSurplus=Context.taxRate*taxableIncome-Context.unemploymentDole*numberOfUnemployed;
	unpaidAmount=Math.max(0,publicSurplusDesiredByCentralBank-publicSurplus);
	bankAccount.setAccount(bankAccount.getAccount()+publicSurplus);
	if(unpaidAmount>0){
		bankAccount.setUnpaidAmount(unpaidAmount);
	}

	if(Context.verboseFlag){
		System.out.println("     publicSurplusDesiredByCentralBank "+publicSurplusDesiredByCentralBank+ " current BA "+bankAccount.getAccount()+" allowed "+bankAccount.getAllowedCredit());
		System.out.println("     taxableIncome "+taxableIncome+" numberOfUnemployed "+numberOfUnemployed+" desiredTaxRate "+desiredTaxRate+" taxRateWithZeroPublicBalace "+taxRateWithZeroPublicBalace+" taxRate "+Context.taxRate);
		System.out.println("     publicSurplus "+publicSurplus+" unpaidAmount "+unpaidAmount); 
	}
}
public BankAccount getBankAccount(){
	return bankAccount;
}

}
