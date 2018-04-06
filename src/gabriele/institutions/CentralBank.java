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
