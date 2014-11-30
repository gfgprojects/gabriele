package sfcabm;

public class BankAccount{
	double account=0;
	double demandedCredit=0;
	double allowedCredit=0;
	String ownerType;
	Object owner;

	public BankAccount(double ac,Object ow){
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
		demandedCredit=0;
		allowedCredit=0;
		if(Context.verboseFlag){
			System.out.println("     bank account "+account+" demanded credit "+demandedCredit+" "+ownerType);
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
			System.out.println("       bank account "+account+" wage "+w+" desiredDemand "+dD+" demanded credit "+demandedCredit+" allowed "+allowedCredit+" "+ownerType);
		}
	}
	public void setAllowedCredit(double aC){
		allowedCredit=aC;
		if(Context.verboseFlag){
			System.out.println("     bank account "+account+" demanded credit "+demandedCredit+" allowed "+allowedCredit+" "+ownerType);
		}
	}

}
