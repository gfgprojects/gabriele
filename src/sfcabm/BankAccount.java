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
			System.out.println("     Created bank account "+account+" demanded credit "+demandedCredit+" "+ownerType+" "+((Consumer)owner).getIdentity());
		}
		if(owner instanceof Firm){
			ownerType="firm";
			System.out.println("     Created bank account "+account+" demanded credit "+demandedCredit+" "+ownerType+" "+((Firm)owner).getIdentity());
		}

	}
}
