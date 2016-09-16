package gabriele.institutions;

import repast.simphony.random.RandomHelper;

import gabriele.financialContracts.BankAccount;
import gabriele.Context;

public class CentralBank {
public BankAccount governmentBankAccount;
public double publicDebt,publicDebtProductionRatio,probabilityOfAPublicDebtIncrease,desiredPublicDebtProductionRatio,desiredPublicDebt;
double randomNumber;

public void setGovernmentBankAccount(BankAccount gba){
	governmentBankAccount=gba;
}
public void setGovernmentAllowedCredit(double aggregateProduction){
	governmentBankAccount.setAccount(governmentBankAccount.getAccount()*(1+Context.interestRateOnLoans));
	publicDebt=-governmentBankAccount.getAccount();
	publicDebtProductionRatio=publicDebt/aggregateProduction;
	probabilityOfAPublicDebtIncrease=0.5*Math.exp(-0.69*publicDebtProductionRatio);
	randomNumber=RandomHelper.nextDouble();
	desiredPublicDebtProductionRatio=publicDebtProductionRatio-Context.stepInPublicDebtProductionRatio;
	if(randomNumber<2*probabilityOfAPublicDebtIncrease){
		desiredPublicDebtProductionRatio=publicDebtProductionRatio;
	}
	if(randomNumber<probabilityOfAPublicDebtIncrease){
		desiredPublicDebtProductionRatio=publicDebtProductionRatio+Context.stepInPublicDebtProductionRatio;
	}
	desiredPublicDebt=desiredPublicDebtProductionRatio*aggregateProduction;
	governmentBankAccount.setAllowedCredit(-desiredPublicDebt);

	if(Context.verboseFlag){
		System.out.println("     public debt "+publicDebt+" aggregate Production "+aggregateProduction+" public debt-aggregate production ratio "+publicDebtProductionRatio+" desiredPublicDebt "+desiredPublicDebt);
	}
}
}
