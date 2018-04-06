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
