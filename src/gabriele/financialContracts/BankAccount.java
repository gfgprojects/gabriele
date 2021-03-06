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

package gabriele.financialContracts;

import gabriele.agents.Bank;
import gabriele.agents.Firm;
import gabriele.agents.Consumer;
import gabriele.Context;

public class BankAccount{
	double account=0;
	double demandedCredit=0;
	double allowedCredit=0;
	double unpaidAmount=0;
	boolean toBeClosed=false;
	String ownerType;
	Object owner;
	Bank hostingBank;

	public BankAccount(double ac,Object ow,Bank hb){
		if(ac>0){
			account=ac;
			demandedCredit=0;
			allowedCredit=0;
		}
		else{
			account=0;
			demandedCredit=ac;
			allowedCredit=0;
		}

		owner=ow;
		hostingBank=hb;
		if(owner instanceof Consumer){
			ownerType="consumer";
			if(Context.verboseFlag){
				System.out.println("     Created bank account "+account+" demanded credit "+demandedCredit+" "+ownerType+" "+((Consumer)owner).getIdentity());
			}
		}
		if(owner instanceof Firm){
			ownerType="firm";
			if(Context.verboseFlag){
				System.out.println("     Created bank account "+account+" demanded credit "+demandedCredit+" "+ownerType+" "+((Firm)owner).getIdentity());
			}
		}

	}


	public double getDemandedCredit(){
		return demandedCredit;
	}
	public double getAllowedCredit(){
		return allowedCredit;
	}
	public double getAccount(){
		return account;
	}
	public String getOwnerType(){
		return ownerType;
	}
	public Object getOwner(){
		return owner;
	}
	public void setAccount(double ac){
		account=ac;
		if(Context.verboseFlag){
			//			System.out.println("     bank account "+account+" demanded credit "+demandedCredit+" "+ownerType);
			System.out.println("      bank account "+account);
		}
	}
	public void setDemandedCredit(double dc){
		demandedCredit=dc;
		if(Context.verboseFlag){
			//			System.out.println("     bank account "+account+" demanded credit "+demandedCredit+" "+ownerType);
			System.out.println("      demanded credit "+demandedCredit);
		}
	}

	public void setDesiredCredit(double w, double dD){
		if(account>0){
			demandedCredit=w-dD;
		}
		else{
			demandedCredit=account+w-dD;
		}
		if(demandedCredit>0){
			demandedCredit=0;
		}
		if(Context.verboseFlag){
			System.out.println("        bank account "+account+" resources send to bank "+w+" new asked credit "+dD+" demanded credit "+demandedCredit+" allowed "+allowedCredit+" "+ownerType);
		}
	}
	public void setAllowedCredit(double aC){
		allowedCredit=aC;
		if(Context.verboseFlag){
			//			System.out.println("     bank account "+account+" demanded credit "+demandedCredit+" allowed "+allowedCredit+" "+ownerType);
			System.out.println("      allowed credit "+allowedCredit);
		}
	}

	public void receiveDeposits(double dep){
		account+=dep;
		if(Context.verboseFlag){
			System.out.println("       previous account "+(account-dep)+" new deposits "+dep+" new account "+account);
		}
	}

	public void resetDemandedCredit(){
		demandedCredit=0;
	}
	public void resetAllowedCredit(){
		allowedCredit=0;
		if(Context.verboseFlag){
			System.out.println("     bank account "+account+" demanded credit "+demandedCredit+" allowed "+allowedCredit+" unpaid "+unpaidAmount+" owner type "+ownerType);
		}
	}
	public void setUnpaidAmount(double ua){
		unpaidAmount=ua;
	}
	public double getUnpaidAmount(){
		return unpaidAmount;
	}
	public void increaseUnpaidAmount(double ua){
		if(Context.verboseFlag){
			System.out.print("      bank account unpaid amount before increase "+unpaidAmount);
		}
		unpaidAmount+=ua;
		if(Context.verboseFlag){
			System.out.println(" after increase "+unpaidAmount);
		}
	}

	public void setOwner(Consumer ow){
		owner=ow;
		if(Context.verboseFlag){
			System.out.println("     bank account new owner consumer "+ow.getIdentity()+" account "+account+" demanded credit "+demandedCredit+" allowed "+allowedCredit+" "+ownerType);
		}

	}

	public void setAccountShutDown(){
		toBeClosed=true;
		if(Context.verboseFlag){
			System.out.println("     bank account will shut down: owner firm "+((Firm)owner).getIdentity()+" account "+account+" demanded credit "+demandedCredit+" allowed "+allowedCredit+" "+ownerType);
		}

	}

	public boolean getAccountShutDownFlag(){
		return toBeClosed;
	}

	public Bank getHostingBank(){
		return hostingBank;
	}

}
